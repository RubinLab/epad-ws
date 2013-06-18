/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server;

import edu.stanford.isis.dicomproxy.common.DicomImageData;
import edu.stanford.isis.dicomproxy.common.DicomSearchResult;
import edu.stanford.isis.dicomproxy.common.DicomSearchType;
import edu.stanford.isis.dicomproxy.common.DicomSeriesUID;
import edu.stanford.isis.dicomproxy.dcm4chee.demos.DicomQuery;

import java.util.List;

/**
 * ProxyManager controls the activities of the Proxy and
 * has a "database" of the current status.
 *
 * @author amsnyder
 */
public class ProxyManager {
    private static ProxyManager ourInstance = new ProxyManager();

    public static ProxyManager getInstance() {
        return ourInstance;
    }

    private ProxyManager() {
    }

    /**
     * Call the DICOM server to get a search result.
     * @param searchParam  String like ExamType=MR, or PatientID=...
     * @return DicomSearchResult
     */
    public DicomSearchResult getSearchResult(DicomSearchType searchType, String searchParam){

        return DicomQuery.searchForStudies(searchType, searchParam);

    }

    /**
     * Call the DICOM server(s) to get a search result for a series.
     * @param studyUID - String like:
     * @param remoteAddr - String used to determine which user.
     * @return DicomSearchResult has information about the series for this studyId
     */
    public DicomSearchResult getSeriesSearchResult(String studyUID, String remoteAddr){

        return DicomQuery.searchForSeries(studyUID, remoteAddr);

    }


    /**
     *
     * @param seriesUID
     * @return
     */
    public List<DicomImageData> getImagesForSeries(DicomSeriesUID seriesUID){
        return DicomQuery.getImagesForSeries(seriesUID);
    }
}
