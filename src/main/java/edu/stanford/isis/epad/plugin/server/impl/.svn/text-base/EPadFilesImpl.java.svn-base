/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epad.plugin.server.impl;

import edu.stanford.isis.dicomproxy.common.DicomFormatUtil;
import edu.stanford.isis.dicomproxy.common.FileKey;
import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.epad.plugin.server.EPadFiles;
import edu.stanford.isis.epad.plugin.server.UIdType;
import org.eclipse.jetty.server.HttpConnection;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implements ePAD Files plugin API.
 *
 * @author amsnyder
 */
public class EPadFilesImpl implements EPadFiles
{
    ProxyLogger logger = ProxyLogger.getInstance();


    @Override
    public boolean hasStudy(String studyUID) {
        List<String> allStudies = getStudies();
        return allStudies.contains(studyUID);
    }

    @Override
    public List<String> getStudies() {
        File baseDicomDir = getBaseDicomDir();

        logger.info("baseDicomDir = "+baseDicomDir.getAbsolutePath());

        File[] files = baseDicomDir.listFiles();
        List<String> retVal = new ArrayList<String>();
        for(File curr : files){
            if(curr.isDirectory()){
                retVal.add(DicomFormatUtil.formatDirToUid(curr.getName()));
            }
        }
        return retVal;
    }

    @Override
    public Map<String, String> getStudyInfo(String s) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public boolean hasSeries(String parentStudyUID, String series) {
        List<String> allSeries = getSeries(parentStudyUID);
        return allSeries.contains(series);
    }

    @Override
    public List<String> getSeries(String parentStudyUID) {
        File baseDicomDir = getBaseDicomDir();
        File studyDir = new File(baseDicomDir.getAbsolutePath()+"/"+DicomFormatUtil.formatUidToDir(parentStudyUID));

        File[] seriesDir = studyDir.listFiles();
        List<String> retVal = new ArrayList<String>();
        for(File curr : seriesDir){
            if(curr.isDirectory()){
                retVal.add(DicomFormatUtil.formatDirToUid(curr.getName()));
            }
        }
        return retVal;
    }

    @Override
    public Map<String, String> getSeriesInfo(String s) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public int hasFiles(String seriesUID, String extension) {
        return getFiles(seriesUID,extension).size();
    }

    @Override
    public List<File> getFiles(String seriesUID, final String extension) {
        String parentStudy = findStudyDirForSeries(seriesUID);
        File seriesDir = new File(DicomFormatUtil.createDicomDirPath(parentStudy,seriesUID));

        File[] filesWithExtension = seriesDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(extension);
            }
        });

        if(filesWithExtension==null){
            //empty array if nothing is found.
            return new ArrayList<File>();
        }

        return Arrays.asList(filesWithExtension);
    }

    @Override
    public List<String> getOrderOfFiles(String seriesUID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int hasAnnotations(String seriesUID) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<File> getAnnotations(String seriesUID) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean saveAnnotation(String xmlContent){
        return false;
    }

    @Override
    public boolean saveAnnotationFile(File aimXmlFile) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    public File getAimFile(String aimXmlFileName) {

        //ToDo: This needs to be implemented with exist-db instead of Berkeley XML DB.
//        get the contents from the database.
//
//        save in a temp file.
//


        //look in the ../resources/annotations/
        File dir = new File("../resources/annotations");
        if( !dir.exists() ){
            logger.info("WARNING: Couldn't find annotations dir: "+dir.getAbsolutePath());
            return null;
        }

        File[] files = dir.listFiles();
        for(File curr : files){
            if( aimXmlFileName.equalsIgnoreCase(curr.getName()) ){
                return curr;
            }
        }

        return null;
    }

    @Override
    public Map<UIdType, String> getCurrentSliceUIDs() {

        //ToDo: Will need a plugin handler which in addition for forwarding calls will
        // also need to get the current slice. Maybe this isn't needed if it is
        // included in the call.

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public File getBaseDir() {
        return new File("../");
    }

    @Override
    public String getBaseDirPath(){
        return FileKey.getCanonicalPath(getBaseDir());
    }

    /**
     * Get the base directory.
     * @return File
     */
    private File getBaseDicomDir(){
        return new File(DicomFormatUtil.getDicomBaseDirPath());
    }

    /**
     * Given a series find the study directory for it. If no study is found then
     * an empty string is returned.
     * @param seriesUID String a seriesUID
     * @return String the parent studyUID
     */
    private String findStudyDirForSeries(String seriesUID){
        List<String> allStudies = getStudies();
        for(String currStudy : allStudies) {
            List<String> allSeries = getSeries(currStudy);
            if(allSeries.contains(seriesUID)){
                return currStudy;
            }
        }
        return "";
    }

}
