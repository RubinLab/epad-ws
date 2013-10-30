/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.FileKey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class keeps track of all the files that have caused an error in the pipeline.
 *
 */
public class UploadPipelineFiles {

    private static final EPADLogger log = EPADLogger.getInstance();

    /**
     * Keeps track of files that have been put in the pipeline.
     */
    private final Map<FileKey,DicomUploadFile> inPipelineMap = new HashMap<FileKey, DicomUploadFile>();

    /**
     * Keeps track of files that had an error in the pipeline, so they are not run again.
     */
    private final Map<FileKey,UploadErrorFile> uploadErrorFiles = new ConcurrentHashMap<FileKey, UploadErrorFile>();

    private static UploadPipelineFiles ourInstance = new UploadPipelineFiles();

    public static UploadPipelineFiles getInstance() {
        return ourInstance;
    }

    private UploadPipelineFiles() {
    }

    /**
     * Add a file to the pipeline.
     * @param currFileKey - FileKey
     */
    public void addToPipeline(FileKey currFileKey){
        inPipelineMap.put(currFileKey, new DicomUploadFile(currFileKey.getFile()));
    }

    /**
     * Return a list of all the error files.
     * @return List
     */
    public List<UploadErrorFile> getErrorFiles(){
        return new ArrayList<UploadErrorFile>(uploadErrorFiles.values());
    }

    /**
     * This method is called if an error occurs somewhere in the pipeline and
     * @param file - File file that had error
     * @param errorMessage - String the error message
     * @param e - Exception
     */
    public void addErrorFile(File file, String errorMessage, Exception e){

        String expMessage = e.getMessage();
        log.info("Adding error file: "+file.getAbsolutePath()+" for: "+errorMessage+" , "+expMessage);

        FileKey uploadFileKey = new FileKey(file);
        uploadErrorFiles.put(uploadFileKey, new UploadErrorFile(uploadFileKey, errorMessage, e));
        inPipelineMap.remove(uploadFileKey);
    }

    /**
     * True if this is a known error file.
     * @param fileKey FileKey
     * @return boolean - true if this is file had an error.
     */
    public boolean isKnownErrorFile(FileKey fileKey){
        return uploadErrorFiles.containsKey(fileKey);
    }

    /**
     * Return true if this file is in the pipeline.
     * @param fileKey FileKey
     * @return boolean true if this file in in the pipeline.
     */
    public boolean isInPipeline(FileKey fileKey){
        return inPipelineMap.containsKey(fileKey);
    }

}
