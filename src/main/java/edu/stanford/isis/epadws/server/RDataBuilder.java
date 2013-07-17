/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import java.util.Map;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.ResourceUtils;
import edu.stanford.isis.epad.common.dicom.DicomSeriesUID;
import edu.stanford.isis.epad.common.dicom.DicomStudyUID;
import edu.stanford.isis.epad.common.dicom.DicomTag;

/**
 *
 * @author amsnyder
 */
public class RDataBuilder {

    private RDataBuilder(){}


    public static RStudyData createStudyData(Map<DicomTag,String> map){
        //int[] required = {Tag.StudyInstanceUID, Tag.StudyDate, Tag.PatientID, Tag.PatientName, Tag.ModalitiesInStudy};

        //checkForRequiredTags(required,map);

        return new RStudyData(map);
    }

    public static RSeriesData createSeriesData(Map<DicomTag,String> map,DicomStudyUID studyUID, DicomSeriesUID seriesUID){
        RSeriesData retVal = new RSeriesData(map);

        ProxyLogger.getInstance().info("Making thumbnail for study="+studyUID+", series="+seriesUID);
        retVal.setThumbnailURL(ResourceUtils.makeThumbnailFilePath(studyUID,seriesUID));
        return retVal;
    }

    public static RImageData createImageData(Map<DicomTag,String> map){
        throw new UnsupportedOperationException("Create image data not implemented.");
    }

}
