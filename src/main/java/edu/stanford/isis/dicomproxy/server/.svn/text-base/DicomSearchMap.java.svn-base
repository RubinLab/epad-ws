/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server;

import edu.stanford.isis.dicomproxy.common.DicomImageData;
import edu.stanford.isis.dicomproxy.common.DicomImageUID;
import edu.stanford.isis.dicomproxy.common.DicomSeriesUID;
import edu.stanford.isis.dicomproxy.common.DicomStudyUID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds all the information known about the relationships between Studies, Series, and Images.
 * <p>
 * Since this relationship don't change this information is kept even when the cached data is deleted.
 *
 * @author amsnyder
 */
public class DicomSearchMap {
    private static DicomSearchMap ourInstance = new DicomSearchMap();

    final Map<DicomSeriesUID,DicomStudyUID> seriesMap = new ConcurrentHashMap<DicomSeriesUID,DicomStudyUID>();

    final Map<DicomSeriesUID,List<DicomImageData>> imagesMap = new ConcurrentHashMap<DicomSeriesUID,List<DicomImageData>>();

    public static DicomSearchMap getInstance() {
        return ourInstance;
    }


    private DicomSearchMap() {
    }

    /**
     * The the parent (studyId) for this seriesId.
     * @param seriesUID
     * @return DicomStudyUID
     */
    public DicomStudyUID getStudyForSeries(DicomSeriesUID seriesUID){
        return seriesMap.get(seriesUID);
    }

    /**
     * Add a Study to Series relationship mapping.
     * @param seriesUID a DicomSeriesId
     * @param studyUID  a DicomStudyId
     */
    public void put(DicomSeriesUID seriesUID,DicomStudyUID studyUID){
        seriesMap.put(seriesUID,studyUID);
    }

    /**
     * Returns true if the images for this series are known.
     * @param seriesUID seriesId
     * @return true if all images for this series are known.
     */
    public boolean hasImagesForSeries(DicomSeriesUID seriesUID){
        return imagesMap.containsKey(seriesUID);
    }

    /**
     * Returns the list if DicomImageUIDs for this series.
     * @param seriesUID
     * @return List<DicomImageUID> a list of images for this series.
     */
    public List<DicomImageData> getImagesForSeries(DicomSeriesUID seriesUID){
        return imagesMap.get(seriesUID);
    }

    /**
     * Get just the list of ImageUIDs in sorted order.
     * @param seriesUID
     * @return List<DicomImageUID>
     */
    public List<DicomImageUID> getImageUIDsForSeries(DicomSeriesUID seriesUID){

        List<DicomImageUID> retVal = new ArrayList<DicomImageUID>();
        for(DicomImageData data : getImagesForSeries(seriesUID)){
            retVal.add(data.getSopInstanceId());
        }
        return retVal;
    }

    /**
     * Save the list of sorted dicom images for this series.
     * @param images  List<DicomImageUID> this list must be sorted from 1 to n.
     */
    public void putImagesForSeries(DicomSeriesUID seriesId, List<DicomImageData> images){
        imagesMap.put(seriesId,images);
    }
}
