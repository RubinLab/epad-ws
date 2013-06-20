package edu.stanford.isis.epadws.db.mysql.pipeline;

import edu.stanford.isis.epadws.common.DicomFormatUtil;
import edu.stanford.isis.epadws.common.WadoUrlBuilder;
import edu.stanford.isis.epadws.db.mysql.MySqlInstance;
import edu.stanford.isis.epadws.db.mysql.MySqlQueries;
import edu.stanford.isis.epadws.db.mysql.impl.DcmDbUtils;
import edu.stanford.isis.epadws.db.mysql.impl.PngStatus;
import edu.stanford.isis.epadws.db.mysql.model.ProcessingState;
import edu.stanford.isis.epadws.db.mysql.model.SeriesOrder;
import edu.stanford.isis.epadws.db.mysql.model.SeriesOrderStatus;
import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;
import edu.stanford.isis.epadws.server.ShutdownSignal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Task to watch series.
 *
 * @author amsnyder
 */
public class SeriesWatcher implements Runnable {

    ProxyLogger logger = ProxyLogger.getInstance();

    final BlockingQueue<SeriesOrder> seriesQueue;
    final BlockingQueue<GeneratorTask> generatorTaskQueue;

    final SeriesOrderTracker seriesOrderTracker;

    private String dcm4cheeRootDir;

    final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();

    ProxyConfig config = ProxyConfig.getInstance();

    public SeriesWatcher(BlockingQueue<SeriesOrder> seriesQueue, BlockingQueue<GeneratorTask> pngTaskQueue){
        this.seriesQueue=seriesQueue;
        this.generatorTaskQueue =pngTaskQueue;
        this.seriesOrderTracker = SeriesOrderTracker.getInstance();

        dcm4cheeRootDir = ProxyConfig.getInstance().getParam("dcm4cheeDirRoot");
    }

    @Override
    public void run() {

        int loopCount=0;
        MySqlQueries mySqlQueries = MySqlInstance.getInstance().getMysqlQueries();
        while ( !shutdownSignal.hasShutdown() ){
            loopCount++;
            try{
                SeriesOrder seriesOrder = seriesQueue.poll(5000, TimeUnit.MILLISECONDS);

                if(seriesOrder!=null){
                    //add new seriesOrder to the map.
                    seriesOrderTracker.add(new SeriesOrderStatus(seriesOrder));
                }

                //walk through the map
                if(seriesOrderTracker.getStatusSet().size()>0){
                    logger.info("SeriesOrderTracker has: "+seriesOrderTracker.getStatusSet().size()+" series.");
                }else{
                    if(loopCount%5==0){
                        logger.info("SeriesOrderTracker running, but has no series.");
                    }
                }
                for(SeriesOrderStatus currStatus : seriesOrderTracker.getStatusSet()){
                    // (look for newly arrived images.)
                    SeriesOrder currSO =currStatus.getSeriesOrder();
                    List<Map<String,String>> newImageList = mySqlQueries.getUnprocessedPngFilesSeries(currSO.getSeriesUID());

                    logger.info("[TEMP] newImageList size: "+newImageList.size());
                    if(newImageList.size()>0){
                        logger.info("[TEMP] "+newImageList.size()+" images added.");
                        currSO.updateImageList(newImageList);
                        currStatus.registerActivity();

                        currStatus.setState(ProcessingState.IN_PIPELINE);
                        addToGeneratorTaskPipeline(newImageList);
                    }
                }//for

                // (look for series that are finished and remove them)
                for(SeriesOrderStatus currStatus : seriesOrderTracker.getStatusSet()){
                    if(currStatus.isDone()){
                        seriesOrderTracker.remove(currStatus);
                    }
                }

            }catch(Exception e){
                logger.warning("Exception SeriesWatcher thread.",e);
            }
        }//while

    }//run


    private void addToGeneratorTaskPipeline(List<Map<String, String>> newImageList){
    	
        MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
        for(Map<String,String> currImage : newImageList)
        {
            //get the input file path.
            String inputFilePath = getInputFilePath(currImage);
            File inputFile = new File(inputFilePath);
           
            //if the file does not exist (stored on another file system)
            if(!inputFile.exists()){
            	//we create a temp file
            	try {
					File temp=File.createTempFile(currImage.get("sop_iuid"), ".tmp");
					feedFileWithDicomFromWado(temp, currImage);
					inputFile = temp;
				} catch (IOException e) {
					logger.warning("Exception when creating temp image : "+currImage.get("sop_iuid"),e);
				}       	
            }

            //get the output file path.
            String outputFilePath = createOutputFilePathForDicomImage(currImage);

            logger.info("Checking epad_files table for: "+outputFilePath);
            if( !queries.hasEpadFile(outputFilePath) ){

                if( PixelMedUtils.isDicomSegmentationObject(inputFilePath) ){
                	//Generate slices of PNG mask
                	logger.info("SeriesWatcher has: "+currImage.get("sop_iuid")+" dicom segmentation object.");
                    processDicomSegmentationObject(outputFilePath, inputFilePath);
                }else{
                    //generate PNG file.
                	logger.info("SeriesWatcher has: "+currImage.get("sop_iuid")+" dicom object.");
                    createPngFileForDicomImage(outputFilePath,inputFile);
                }

            }//if
        }//for

    }//addToPgnPipeline


    private void feedFileWithDicomFromWado(File temp, Map<String,String> currImage){
		
		//we use wado to get the dicom image		
        String host = config.getParam("NameServer");
        int port = config.getIntParam("DicomServerWadoPort");
        String base = config.getParam("WadoUrlExtension");
      
        WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host,port,base,WadoUrlBuilder.Type.FILE);

        //GET WADO call result.
        wadoUrlBuilder.setStudyUID(currImage.get("study_iuid"));
        wadoUrlBuilder.setSeriesUID(currImage.get("series_iuid"));
        wadoUrlBuilder.setObjectUID(currImage.get("sop_iuid"));

        try {
			String wadoUrl = wadoUrlBuilder.build();
			logger.info("Build wadoUrl = "+wadoUrl);
			
			//--Get the Dicom file from the server
			HttpClient client = new HttpClient();

			GetMethod method = new GetMethod(wadoUrl);
			// Execute the GET method
			int statusCode = client.executeMethod(method);
			
			if(statusCode != -1 ) {
				//Get the result as stream
				InputStream res = method.getResponseBodyAsStream();
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(temp);
				
				int read = 0;
				byte[] bytes = new byte[4096];
			 
				while ((read = res.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				res.close();
				out.flush();
				out.close();
			}
				
			
		} catch (UnsupportedEncodingException e) {
			logger.warning("Not able to build wado url for : "+temp.getName(),e);
		} catch (HttpException e) {
			logger.warning("Not able to get the wado image : "+temp.getName(),e);
		} catch (IOException e) {
			logger.warning("Not able to write the temp dicom image : "+temp.getName(),e);
		}
    }
    
    
    private void processDicomSegmentationObject(String outputFilePath, String inputFilePath){
        logger.info("Segmentation Object found: "+inputFilePath);

        File inFile = new File(inputFilePath);
        File outFile = new File(outputFilePath);
      
        DicomSegmentObjectGeneratorTask dsoTask = new DicomSegmentObjectGeneratorTask(inFile,outFile);
        generatorTaskQueue.offer(dsoTask);
    }

    private void createPngFileForDicomImage(String outputFilePath, File inputFile){
                logger.info("Offering to PngTaskQueue: out="+outputFilePath+" in="+inputFile.getAbsolutePath() );

                File outputFile = new File(outputFilePath);
                MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
                insertEpadFile(queries, outputFile);

                PngGeneratorTask pngTask = new PngGeneratorTask(inputFile, outputFile);
                generatorTaskQueue.offer(pngTask);
    }


    private void insertEpadFile(MySqlQueries queries, File outputFile) {
        Map<String,String> epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);
        epadFilesTable.put("file_status", ""+ PngStatus.IN_PIPELINE.getCode());
        queries.insertEpadFile(epadFilesTable);
    }


    String getInputFilePath(Map<String,String> currImage){
        //NOTE: Maybe we want to get this from the 'files' directory.

//        StringBuilder sb = new StringBuilder();
//        sb.append("[TEMP] PNG Creation. ");
//        sb.append("file_size=").append(currImage.get("file_size"));
//        sb.append(", inst_no=").append(currImage.get("inst_no"));
//        sb.append(", sop_iuid=").append(currImage.get("sop_iuid"));
//        sb.append(", series_iuid=").append(currImage.get("series_iuid"));
//        sb.append(", filepath=").append(currImage.get("filepath"));
//        logger.info(sb.toString());

        String retVal = getDcm4cheeRootDir()+currImage.get("filepath");

        return retVal;
    }

    /**
     *
     * @param currImage Map of String to String
     * @return String
     */
    String createOutputFilePathForDicomImage(Map<String, String> currImage){

        String seriesIUID = currImage.get("series_iuid");

        MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
        String studyUID = queries.getStudyUIDForSeries(seriesIUID);

        String imageUID = currImage.get("sop_iuid");

        StringBuilder outputPath = new StringBuilder();
        outputPath.append("../resources/dicom/");
        outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
        outputPath.append(DicomFormatUtil.formatUidToDir(seriesIUID)).append("/");
        outputPath.append(DicomFormatUtil.formatUidToDir(imageUID)).append(".png");

        return outputPath.toString();
    }

    /**
     * Add a forward slash if it is missing.
     * @return String
     */
    public String getDcm4cheeRootDir(){
        if(dcm4cheeRootDir.endsWith("/")){
            return dcm4cheeRootDir;
        }
        return dcm4cheeRootDir+"/";
    }

}
