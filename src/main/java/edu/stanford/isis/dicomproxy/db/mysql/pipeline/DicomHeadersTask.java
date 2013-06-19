package edu.stanford.isis.dicomproxy.db.mysql.pipeline;

import edu.stanford.isis.dicomproxy.common.ProxyFileUtils;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;

import java.io.*;

/**
 *
 *
 * @author amsnyder
 */
public class DicomHeadersTask implements Runnable
{
    private static ProxyLogger logger = ProxyLogger.getInstance();

    final File dicomInputFile;
    File outputFile;

    public DicomHeadersTask(File dicomInputFile, File outputFile){
        this.dicomInputFile=dicomInputFile;
        this.outputFile=outputFile;
    }

    @Override
    public void run()
    {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileWriter tagFileWriter = null;
        Process process = null;
        
        try{
        	//int nbLines=0;
            String[] command = {"./dcm2txt", "-w", "250", "-l", "250",dicomInputFile.getAbsolutePath() };

            ProcessBuilder pb = new ProcessBuilder( command );
            pb.directory(new File("../etc/scripts/tpl/bin"));

            process = pb.start();
            process.getOutputStream();//get the output stream.
            //Read out dir output
            is = process.getInputStream();
            isr = new InputStreamReader(is);

            br = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                //nbLines++;
            }

            //Wait to get exit value
            try {
                process.waitFor(); //keep.  
            } catch (InterruptedException e) {
                logger.warning("Couldn't get tags for: "+dicomInputFile.getAbsolutePath(),e);
            }

            boolean created = ProxyFileUtils.createDirsAndFile(outputFile);
            if(created){
                logger.info("Using file: "+outputFile.getAbsolutePath());
            }

            //write the contents of this buffer to a file.
            File tagFile = outputFile;
            tagFileWriter = new FileWriter(tagFile);
            tagFileWriter.write(sb.toString());
           // tagFileWriter.flush();
            //tagFileWriter.close();

        }catch (Exception e){
            logger.warning("DicomHeadersTask failed to create dicom tags file.",e);
        }catch (OutOfMemoryError oome){
            logger.warning("DicomHeadersTask OutOfMemoryError: ",oome);
        }
        finally{
        	
            close(tagFileWriter);
            close(br);
            close(isr);
            close(is);
            if(process!=null)
            	process.destroy();
        }
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

}
