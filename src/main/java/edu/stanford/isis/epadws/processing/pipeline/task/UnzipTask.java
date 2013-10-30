/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.task;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.pipeline.DicomFileUtil;
import edu.stanford.isis.epadws.processing.pipeline.UploadPipelineFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * C
 */
public class UnzipTask implements Callable<List<File>>{

    private static final EPADLogger log = EPADLogger.getInstance();
    private static final UploadPipelineFiles pipeline = UploadPipelineFiles.getInstance();

    File file;

    public UnzipTask(File file){
        this.file=file;
    }

    @Override
    public List<File> call() throws Exception {

        List<File> retVal = new ArrayList<File>();
        try{
            log.info("Checking file extension: "+file.getName());
            //Dicom files might not have extensions. Add it if missing.
            if(!EPADFileUtils.hasExtension(file)){
                if( DicomFileUtil.hasMagicWordInHeader(file)){
                    file = DicomFileUtil.addDcmExtensionToFile(file);
                }
            }

            String extension = EPADFileUtils.getExtension(file);

            if(extension.equalsIgnoreCase("dcm")){
                retVal.add(file);
                return retVal;
            }else if(extension.equalsIgnoreCase("zip")){
                EPADFileUtils.extractFolder(file.getAbsolutePath());
                //The new files will be discovered by the directory watcher, so don't return anything.
                return retVal;
            }else if(extension.equalsIgnoreCase("gz")){
                EPADFileUtils.extractFolder(file.getAbsolutePath());
                //The new files will be discovered by the directory watcher, so don't return anything.
                return retVal;
            }else if(extension.equalsIgnoreCase("tag")){
                //ignore new tag files they will get moved with dcm files.
                return retVal;
            }else{
                log.info("Ignoring file with unknown extension: "+file.getAbsolutePath());
                //retVal.add(file);
                return retVal;
            }
        }catch(Exception e){
            pipeline.addErrorFile(file,"UnzipTask error.",e);
            return retVal;
        }
    }

    //ToDo: move to file extensions. and name with lower case.

}
