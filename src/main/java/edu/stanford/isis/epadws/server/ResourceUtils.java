/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import edu.stanford.isis.epadws.common.DicomFormatUtil;
import edu.stanford.isis.epadws.common.DicomSeriesUID;
import edu.stanford.isis.epadws.common.DicomStudyUID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author amsnyder
 */
public class ResourceUtils {

    private static final ProxyLogger log = ProxyLogger.getInstance();

    private ResourceUtils(){}

    /**
     * Write a jpg URL to a file
     * @param jpgUrl  URL of a jpg image.
     * @param file    Filename to write this image
     */
    public static void writeJpgUrlToFile(String jpgUrl, File file)
        throws MalformedURLException, IOException
    {
        URL url = new URL(jpgUrl);
        BufferedImage bi = ImageIO.read(url);
        ImageIO.write(bi,"jpg",file);
    }


    /**
     * Create a file if it doesn't exist.
     * @param path
     * @return
     * @throws IOException
     */
    public static File generateFile(String path)
        throws IOException
    {
        File f = new File(path);
        if( !f.exists() ){
            createAncestors(f.getParentFile());
            log.debug("Creating new file: "+f.getAbsolutePath());
            f.createNewFile();
        }
        return f;
    }

    /**
     * Create this directory and all of it's ancestors. Throw an exception if this is an invalid file.
     * @param dir
     */
    private static void createAncestors(File dir)
        throws IOException
    {
        if(!dir.exists()){
            createAncestors(dir.getParentFile());
        }
        dir.mkdir();
    }

    /**
     * The base directory for the DicomProxy as a File.
     * @return File - base directory for DicomProxy.
     */
    public static File getDicomProxyBaseDir(){
        String pwd = System.getProperty("user.dir");
        // WTF String parent = pwd.substring(0,pwd.length()-4);

        return new File(pwd);
    }

    /**
     * Make the file path to a thumbnail given it's studyID and seriesID.
     * The study and series Id all have the '.' replaced with '_'.
     *
     * @param studyUID
     * @param seriesUID
     * @return String - like: resources/dicom/<studyUID>/thumbnail_<seriesUID>.jpg
     */
    public static String makeThumbnailFilePath(DicomStudyUID studyUID, DicomSeriesUID seriesUID){
        String studyDir = DicomFormatUtil.formatUidToDir(studyUID.toString());
        String seriesName = DicomFormatUtil.formatUidToDir(seriesUID.toString());

        return "resources/dicom/"+studyDir+"/thumbnail_"+seriesName+".jpg";
    }

}
