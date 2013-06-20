/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.dicom;

import edu.stanford.isis.epadws.common.DicomFormatUtil;
import edu.stanford.isis.epadws.common.FileKey;
import edu.stanford.isis.epadws.server.ProxyLogger;
import edu.stanford.isis.epadws.server.managers.leveling.WindowLevelFactory;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a speculative code that creates leveled jpegs on the server side. It will have the following params
 * wl -  window level or sometimes called window center.  range 0 to 4096
 * ww -  window width. range 0 to 4096
 * seriesuid -  The series UID. Something like 1.2.456.76.34
 * order - Which image in the stack it is.   range 1 to max image number.
 * direction - The direction of travel. up/down
 *
 * ex. http://rufus.stanford.edu:8325/level?cmd=create&wl=400&ww=40&order=51&direction=up&seriesuid=1.2.456.76.34
 *
 * This class will start the leveling process, and the call returns immediately.
 *
 * Another call is to get the list of leveled jpegs. It will look like this.
 *
 * ex. http://rufus.stanford.edu:8325/level?cmd=query&wl=400&ww=40&seriesuid=1.2.456.76.34
 *
 */
public class WindowLevelHandler extends AbstractHandler
{

    private static ProxyLogger logger = ProxyLogger.getInstance();

    //caches which series belongs to which study in case the request doesn't have that info.
    Map<String,String> seriesToStudyMap = new HashMap<String, String>();


    @Override
    public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        try{
            res.setContentType("text/plain");
            res.setStatus(HttpServletResponse.SC_OK);
            res.setHeader("Access-Control-Allow-Origin","*");

            request.setHandled(true);

            String cmd = req.getParameter("cmd");

            logger.info("level request cmd="+cmd);

            PrintWriter out;
            if(cmd.equals("create")){
                out = handleCreateLevelJpegs(req, res);
            }else if(cmd.equals("query")){
                out = handleQueryLeveledJpegs(req,res);
            }else{
                out = res.getWriter();
                out.print("Unrecognized cmd parameter: "+req.getQueryString());
            }

            out.flush();
        }catch (Exception e){
            logger.warning("Request failed.",e);
        }
    }

    /**
     * Put in a request to level all of the images.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return PrintWriter
     */
    private PrintWriter handleCreateLevelJpegs(HttpServletRequest req, HttpServletResponse res)
        throws IOException, InterruptedException
    {
        String wl = req.getParameter("wl");
        String ww = req.getParameter("ww");
        String seriesuid = req.getParameter("seriesuid");
        String studyuid = req.getParameter("studyuid");
        PrintWriter out = res.getWriter();
        //String direction = req.getParameter("direction");
        //String order = req.getParameter("order");

        File seriesDir = findSeriesDirectory(studyuid,seriesuid);

        //get all the dicom files in this directory.
        File[] dicomFiles = seriesDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".dcm");
            }
        });

        //ToDo: sort the dicom files into a optimized order.

        WindowLevelFactory levelFactory = WindowLevelFactory.getInstance();
        int windowWidth = Integer.parseInt(ww);
        int windowLevel = Integer.parseInt(wl);
        String seriesToLevel = DicomFormatUtil.formatDirToUid(seriesuid);

        levelFactory.setLevel( windowWidth, windowLevel, seriesToLevel );
        for(File dicomFile : dicomFiles){
            levelFactory.submitDicomFile(dicomFile,seriesToLevel);
        }


 //
        //ToDo: (a) Read the order file
        // (b) determine an optimized order of creating the files.
        // (c) delete any work in the queue creating a different window level setting.
        // (d) Add the all into the queue most important first.

        return out;
    }

    /**
     * Return the directory.
     * @param studyuid String
     * @param seriesuid String
     * @return File
     */
    private File findSeriesDirectory(String studyuid, String seriesuid){
        if(studyuid==null || "".equals(studyuid)){
            //the study was not specified, look for this series in the directories.
            studyuid = findStudyForSeries(seriesuid);
        }

        if(studyuid==null){
            throw new IllegalStateException("Failed to find a study directory for seriesuid="+seriesuid);
        }

        return new File( DicomFormatUtil.createDicomDirPath(studyuid,seriesuid) );
    }

    /**
     * Returns the study directory for this series.
     * @param seriesuid String
     * @return String
     */
    private String findStudyForSeries(String seriesuid){

        //is the answer cached?
        String cachedStudyDir = seriesToStudyMap.get(seriesuid);
        if(cachedStudyDir!=null){
            return cachedStudyDir;
        }

        File baseDicomDir = new File(DicomFormatUtil.getDicomBaseDirPath() );

        File[] studyDirs = baseDicomDir.listFiles();
        for(File currStudyDir : studyDirs){

            File[] seriesDirs = currStudyDir.listFiles();
            for(File currSeriesDir : seriesDirs){

                String seriesDir = currSeriesDir.getName();
                String seriesUid = DicomFormatUtil.formatDirToUid(seriesDir);

                if(seriesDir.equals(seriesuid) || seriesUid.equals(seriesuid)){
                    //Found it!!
                    String studyDirName = currStudyDir.getName();
                    seriesToStudyMap.put(seriesDir,studyDirName);
                    return studyDirName;
                }
            }
        }
        return null;
    }

    /**
     * Get a list of all the jpegs that currently have level images to the parameters requested.
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @return PrintWriter
     */
    private PrintWriter handleQueryLeveledJpegs(HttpServletRequest req, HttpServletResponse res)
        throws IOException
    {
        String wl = req.getParameter("wl");
        String ww = req.getParameter("ww");
        String seriesuid = req.getParameter("seriesuid");
        String studyuid = req.getParameter("studyuid");
        PrintWriter out = res.getWriter();

        File seriesDir = findSeriesDirectory(studyuid,seriesuid);

        File[] leveledJPegFiles = listLeveledJPegFiles(seriesDir, ww, wl);

        //ToDo: possibly return this in order-file order with spaces, for missing files.
        if( leveledJPegFiles.length==0 ){
            out.print("No Files found for ww="+ww+", wl="+wl);
        }else{
            for(File currJPEG : leveledJPegFiles){
                out.print(FileKey.getCanonicalPath(currJPEG)+"\n");
            }
        }

        return out;
    }

    /**
     *
     * @param ww String
     * @param wl String
     * @return String[]
     */
    File[] listLeveledJPegFiles(File seriesDir, String ww, String wl){

        String leveledDirName = "ww"+ww+"wl"+wl;

        File leveledDir = new File(seriesDir.getAbsolutePath()+"/"+leveledDirName);
        if( !leveledDir.exists() ){
            throw new IllegalArgumentException("Didn't find leveled directory :"+leveledDir.getAbsolutePath());
        }

        File[] retVal = leveledDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".jpg");
            }
        });

        return retVal;
    }


}
