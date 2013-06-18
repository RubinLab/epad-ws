package edu.stanford.isis.dicomproxy.db.mysql.pipeline;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.isis.dicomproxy.common.ProxyFileUtils;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlInstance;
import edu.stanford.isis.dicomproxy.db.mysql.MySqlQueries;
import edu.stanford.isis.dicomproxy.db.mysql.impl.DcmDbUtils;
import edu.stanford.isis.dicomproxy.db.mysql.impl.PngStatus;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.managers.support.DicomReader;

/**
 * Given a specific input and output file generate the png file.
 *
 * @author amsnyder
 */
public class PngGeneratorTask implements GeneratorTask
{

    private static ProxyLogger logger = ProxyLogger.getInstance();

    File dicomInputFile;
    File pngOutputFile;


    public PngGeneratorTask(File dicomInputFile, File pngOutputFile){
        this.dicomInputFile=dicomInputFile;
        this.pngOutputFile=pngOutputFile;
    }


    @Override
    public void run() {
        writePackedPngs();
    }

    public void writePackedPngs()
    {
        MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();

        File inputFile = dicomInputFile;
        File outputFile = pngOutputFile;
        OutputStream outputStream = null;
        Map<String,String> epadFilesTable = new HashMap<String, String>();
        try{
            epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);

            DicomReader instance = new DicomReader(inputFile);
            String pngFilePath = outputFile.getAbsolutePath();

            logger.info("PngGeneratorTask:Creating png file: "+outputFile.getAbsolutePath());

            outputFile = new File(pngFilePath); //create the real file name here.

            //create the file.
            boolean created = ProxyFileUtils.createDirsAndFile(outputFile);
            if(created){
                logger.info("Using file: "+outputFile.getAbsolutePath());
            }

            outputStream = new FileOutputStream(outputFile);
            //ImageIO.write(instance.getPackedImage(), "png", outputStream);
            ImageIO.write(instance.getMyPackedImage(), "png", outputStream);

            logger.info("finished writing PNG file: "+outputFile);

            epadFilesTable = DcmDbUtils.createEPadFilesTableData(outputFile);
            int fileSize = getFileSize(epadFilesTable);
            queries.updateEpadFile(epadFilesTable.get("file_path"),PngStatus.DONE,fileSize,"");

           
        }catch (FileNotFoundException e){
            logger.warning("failed to create packed png for: "+inputFile.getAbsolutePath(),e);
            queries.updateEpadFile(epadFilesTable.get("file_path"),PngStatus.ERROR,0,"Dicom file not found.");

        }catch (IOException e){
            logger.warning("failed to create packed PNG for: "+inputFile.getAbsolutePath(),e);
            queries.updateEpadFile(epadFilesTable.get("file_path"),PngStatus.ERROR,0,"IO Error.");
        }catch (Exception e){
            logger.warning("FAILED to create packed PNG for: "+inputFile.getAbsolutePath(),e);
            queries.updateEpadFile(epadFilesTable.get("file_path"),PngStatus.ERROR,0,"General Exception: "+e.getMessage());
        }finally {

        	 if(inputFile.getName().endsWith(".tmp")){
        		 boolean res=inputFile.delete();
        		 logger.info("deletion of input temp dicom File : "+res);
        	 }
        	

        	 
            if(outputStream!=null){
                try{
                    outputStream.flush();
                    outputStream.close();
                    outputStream=null;
                }catch (Exception e){
                    logger.warning("Failed to close outputStream.",e);
                }
            }//if

        }
    }

    private int getFileSize(Map<String,String> epadFilesTable){
        try{
            String fileSize = epadFilesTable.get("file_size");
            return Integer.parseInt(fileSize);
        }catch (Exception e){
             logger.warning("Failed to get file.",e);
            return 0;
        }
    }



    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PngGeneratorTask[").append(" in=").append(dicomInputFile);
        sb.append(" out=").append(pngOutputFile).append("]");

        return sb.toString();
    }

    @Override
    public File getDicomInputFile() {
        return dicomInputFile;
    }

    @Override
    public String getTagFilePath() {
        return pngOutputFile.getAbsolutePath().replaceAll("\\.png",".tag");
    }

    @Override
    public String getTaskType() {
        return "Png";
    }
}
