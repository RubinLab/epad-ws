package edu.stanford.isis.epad.plugin.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author amsnyder
 */
public class PluginLogger
{
    private static Logger logger;

    private PluginLogger(){}

    public synchronized static Logger getLogger(){
        if(logger!=null){
            return logger;
        }

        logger = Logger.getLogger("EPadPluginLogger");

        FileHandler fileHandler;
        try{
            String timeStamp = createTimestamp();
            fileHandler = new FileHandler("../log/epad-plugin-"+timeStamp+".log",true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler( fileHandler );
            logger.setLevel( Level.ALL );

        }catch(IOException ioe){
            System.err.println("PluginLogger had: "+ioe.getMessage());
        }catch(SecurityException se){
            System.err.println("PluginLogger had: "+se.getMessage());
        }
        return logger;
    }

    private static String createTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
        return sdf.format(new Date());
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
