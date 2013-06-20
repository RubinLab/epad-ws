/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.pipeline;


import java.io.*;
import java.util.concurrent.Callable;
import java.io.IOException;
import javax.imageio.ImageIO;

import edu.stanford.isis.epadws.server.ProxyLogger;
import edu.stanford.isis.epadws.server.managers.support.DicomReader;


/**
 * Creates a packed PNG file based. DicomReader code is from Bradley Ross.
 *
 * @see edu.stanford.isis.epadws.server.managers.support.DicomReader#main
 */
public class PackedPngTask implements Callable<File>
{
    static final ProxyLogger logger = ProxyLogger.getInstance();

    final File file;

    protected PackedPngTask(File f){
        file = f;
    }

    @Override
    public File call() throws Exception {

        File input = file;
        File outputFile = null;
        OutputStream outputStream = null;
        try{

            DicomReader instance = new DicomReader(input);
            String pngFilePath = file.getAbsolutePath().replaceAll("\\.dcm",".png");

            logger.info("Creating png file: "+pngFilePath);

            outputFile = new File(pngFilePath); //create the real file name here.
            outputStream = new FileOutputStream(outputFile);
            ImageIO.write(instance.getPackedImage(), "png", outputStream);

        }catch (FileNotFoundException e) {
            logger.warning("failed to create packed png for: "+file.getAbsolutePath(),e);
        }catch (IOException e) {
            logger.warning("failed to create packed PNG for: "+file.getAbsolutePath(),e);
        }finally {
            if(outputStream!=null){
                try{
                    outputStream.close();
                    outputStream=null;
                }catch (Exception e){}
            }
        }
        return outputFile;
    }

}
