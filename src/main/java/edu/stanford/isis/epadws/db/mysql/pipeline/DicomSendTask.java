package edu.stanford.isis.epadws.db.mysql.pipeline;

import edu.stanford.isis.epadws.common.ProxyFileUtils;
import edu.stanford.isis.epadws.server.ProxyConfig;
import edu.stanford.isis.epadws.server.ProxyLogger;

import java.io.*;

/**
 *
 *
 * @author amsnyder
 */
public class DicomSendTask implements Runnable
{
    private static ProxyLogger logger = ProxyLogger.getInstance();

    final File inputDir;

    public DicomSendTask(File inputDir){
        this.inputDir = inputDir;
    }

    @Override
    public void run()
    {
        try{
            dcmsnd(inputDir, false);
        }catch (Exception e){
           logger.warning("run had: "+e.getMessage(),e);
        }

    }

    /**
     *
     * @param dirPath String
     * @return boolean true if this path contains spaces.
     */
    private static boolean pathContainsSpaces(String dirPath){
        return dirPath.indexOf(' ')>0;
    }

    /**
     *
     * @param path String
     * @return String
     */
    private static String escapeSpacesInDirPath(String path){
        StringBuilder sb = new StringBuilder();

        String[] parts = path.split(" ");
        boolean isFirst=true;
        for(String part : parts){
            if( !isFirst ){
                sb.append("\\ ");
            }
            sb.append(part);
            isFirst=false;
        }
        return sb.toString();
    }

    public static void dcmsnd(File inputDirFile, boolean throwException) throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileWriter tagFileWriter = null;

        try{
            ProxyConfig pc = ProxyConfig.getInstance();
            String aeTitle = pc.getParam("DicomServerAETitle");
            String dsPort = pc.getParam("DicomServerPort");

            String dcmServerTitlePort = aeTitle+"@localhost:"+dsPort;
            dcmServerTitlePort = dcmServerTitlePort.trim();

            String dirPath = inputDirFile.getAbsolutePath();
            if( pathContainsSpaces(dirPath) ){
                dirPath = escapeSpacesInDirPath(dirPath);
            }
            
            //Get the number of files
            File dir=new File(dirPath);
            int nbFiles = -1;
            if(dir!=null){
            	String[] filePaths = dir.list();
            	if(filePaths!=null)
            		nbFiles = filePaths.length;
            }
            
            logger.info("Sending "+nbFiles+" files - command: ./dcmsnd "+dcmServerTitlePort+" "+dirPath);

            String[] command = {"./dcmsnd", dcmServerTitlePort, dirPath };

            ProcessBuilder pb = new ProcessBuilder( command );
            pb.directory(new File("../etc/scripts/tpl/bin"));

            Process process = pb.start();
            process.getOutputStream();//get the output stream.
            //Read out dir output
            is = process.getInputStream();
            isr = new InputStreamReader(is);

            br = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }


            //Wait to get exit value
            try {
                process.waitFor(); //keep.
//                long totalTime = System.currentTimeMillis() - startTime;
//                log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
            } catch (InterruptedException e) {
                logger.warning("Didn't send dicom files in: "+inputDirFile.getAbsolutePath(),e);
            }


            String cmdLineOutput = sb.toString();
            writeUploadLog(cmdLineOutput);

            if(cmdLineOutput.toLowerCase().contains("error")){
                throw new IllegalStateException("Failed for: "+parseError(cmdLineOutput));
            }

        }catch (Exception e){

            if(e instanceof IllegalStateException && throwException){
                throw e;
            }

            logger.warning("DicomHeadersTask failed to create dicom tags file.",e);
            if(throwException){
                throw new IllegalStateException("DicomHeadersTask failed to create dicom tags file.",e);
            }
        }catch (OutOfMemoryError oome){
            logger.warning("DicomHeadersTask OutOfMemoryError: ",oome);
            if(throwException){
                throw new IllegalStateException("DicomHeadersTask OutOfMemoryError: ",oome);
            }
        }
        finally{
            close(tagFileWriter);
            close(br);
            close(isr);
            close(is);
        }
    }

    private static String parseError(String output){
        try{
            String[] lines = output.split("\n");
            for(String currLine : lines){
                if(currLine.toLowerCase().contains("error")){
                    return currLine;
                }
            }
        }catch (Exception e){
            logger.warning("DicomSendTask.parseError had: "+e.getMessage()+" for: "+output,e);
        }
        return output;
    }

    /**
     * Log the result of this upload to the log directory.
     * @param contents String
     */
    private static void writeUploadLog(String contents){
        String fileName="../log/upload_"+System.currentTimeMillis()+".log";
        ProxyFileUtils.write(new File(fileName), contents);
    }


    private static void close(Writer writer){
        try{
            if(writer!=null){
                writer.flush();
                writer.close();
                writer=null;
            }
        }catch (Exception e){
            logger.warning("Failed to close writer",e);
        }
    }

    private static void close(Reader reader){
        try{
            if(reader!=null){
                reader.close();
                reader=null;
            }
        }catch (Exception e){
            logger.warning("Failed to close reader",e);
        }
    }

    private static void close(InputStream stream){
        try{
            if(stream!=null){
                stream.close();
                stream=null;
            }
        }catch (Exception e){
            logger.warning("Failed to close stream",e);
        }
    }


    /**
     * For testing.
     * @param args String[]
     */
    public static void main(String[] args){
        System.out.println("testing spaces in directory. ");
        String dirWithoutSpace="/home/epad/cureData/9/9_40_456/";
        String dirWithSpace="/home/epad/cureData/9/9 40 456/";

        if( pathContainsSpaces(dirWithoutSpace) ){
            System.out.print("TEST FAILED. pathContainsSpaces error");
        }

        if( pathContainsSpaces(dirWithSpace) ){
            String newDirPath = escapeSpacesInDirPath(dirWithSpace);
            System.out.println("Expected: /home/epad/cureData/9\\ 40\\ 456/  Actual: "+newDirPath);
        }
        System.out.println("DONE.");
    }

}
