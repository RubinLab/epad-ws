/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.managers.pipeline;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.LockFileUtils;
import edu.stanford.isis.epad.common.util.ProxyFileUtils;
import edu.stanford.isis.epad.common.util.RsnaSearchResultMap;


public class MoverTask implements Callable<File> {

    static final ProxyLogger log = ProxyLogger.getInstance();

    static final UploadPipelineFiles pipeline = UploadPipelineFiles.getInstance();

    final File file;

    public MoverTask(File file){
        this.file=file;
    }

    @Override
    public File call() throws Exception {

        try{
            //Read the tags.
            File tagFile = new File(DicomTagFileUtils.createTagFilePath(file.getAbsolutePath()));
            Map<String,String> tags = DicomTagFileUtils.readTagFile(tagFile);

            //Determine the directory.  i.e. study/series.
            String studyId = DicomTagFileUtils.getTag(DicomTagFileUtils.STUDY_UID,tags);
            String seriesId = DicomTagFileUtils.getTag(DicomTagFileUtils.SERIES_UID,tags);
            String sopInstanceId = DicomTagFileUtils.getTag(DicomTagFileUtils.SOP_INST_UID,tags);

            //create the move-to directory structure.
            String toDirPathBase = DicomFormatUtil.createDicomDirPath(studyId, seriesId);
            String imageFileName = DicomFormatUtil.formatUidToDir(sopInstanceId);

            File movedTagFile = new File(toDirPathBase+"/"+imageFileName+".tag");
            File movedFile = new File(toDirPathBase+"/"+imageFileName+".dcm");

            //write the file to this directory path.
            ProxyFileUtils.checkAndMoveFile(tagFile, movedTagFile);
            LockFileUtils.lockDir(new File(toDirPathBase), LockFileUtils.LockType.PIPELINE);
            ProxyFileUtils.checkAndMoveFile(file, movedFile);

            //ToDo: delete RSNA search mode when done.
            RsnaSearchResultMap resultMap = RsnaSearchResultMap.getInstance();
            resultMap.addResult(tags);

            //write the series file if it doesn't exist already.
            writeSeriesFileIfNeeded(tags, studyId, seriesId);

            //write JPG and or 16-bit Png files.
            createImageFiles(tags, movedFile);

            return movedFile;

        }catch(Exception e){
            pipeline.addErrorFile(file,"MoverTask error.",e);
            e.printStackTrace();//temp
            return null;
        }
    }//call


    /**
     * Creates a jpeg and/or a two-channel png file for each .dicom file.
     * @param tags Map of String keys to String value. Contains the tags in the DICOM file.
     * @param dcmFile the dicom file used to create an image.
     */
    private void createImageFiles(Map<String,String> tags,File dcmFile){

        ThumbnailManager thumbnailManager = ThumbnailManager.getInstance();
        thumbnailManager.writeJpgFile(tags,dcmFile);

        //ToDo: If we are not doing this then remove pixelMed.jar file from project.

    }//createJpgFile


    /**
     * It a *.series file for series doesn't already exist then write one.
     * @param tags Map of the header files and values.
     * @param studyId String
     * @param seriesId String
     */
    private void writeSeriesFileIfNeeded(Map<String,String> tags, String studyId, String seriesId){

        File studyDir = new File(DicomFormatUtil.createDicomDirPath(studyId));

        String seriesFileName = DicomFormatUtil.formatUidToDir(seriesId);
        File seriesFile = new File(studyDir+"/"+seriesFileName+".series");
        if(seriesFile.exists()){
            //log.info("series file exists for: "+seriesFileName);
            return;
        }

        debugMap(tags,5);
        String content = SeriesFileUtils.createSeriesFileContentFromDicomTags(tags);
        log.info("writing series file: "+seriesFile.getAbsolutePath()+" \n "+content);

        ProxyFileUtils.overwrite(seriesFile, content);
    }

    /**
     * Just for debugging the tags map, since different flavors of Map<String,String> are in the program.
     * @param tags Map
     * @param maxTags int
     */
    private static void debugMap(Map<String,String> tags, int maxTags){

        int nTags = tags.size();
        Set<String> keys = tags.keySet();
        if(nTags<maxTags){
            maxTags=nTags;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#tags=").append(nTags).append(", ");
        int i=0;
        for(String currKey : keys){
            i++;
            sb.append("key[").append(i).append("]=").append(currKey).append(", ");

            if(i>maxTags){break;}
        }//for
        log.info("debugMap:  "+sb.toString());
    }//debugMap

}
