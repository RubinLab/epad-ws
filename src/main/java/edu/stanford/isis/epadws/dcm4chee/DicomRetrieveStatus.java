/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.dcm4chee.demos;

import edu.stanford.isis.epad.common.dicom.DicomImageUID;
import edu.stanford.isis.epad.common.dicom.DicomSeriesUID;
import edu.stanford.isis.epad.common.dicom.DicomStudyUID;

import java.util.Date;
import java.util.List;

/**
 * Thread-safe class that keeps the current status of a Dicom Retrieve.
 * It should:
 *  (a) list the UID of images that have been downloaded.
 *  (b) list "started", "finished" status, and if possible percent complete.
 *  (c) list the UIDs of the series and study associated with this retrieve.
 *  (d) when the process is done, list process time, start time and finish time.
 *  (e) if the data is available list the total bytes transferred.
 *  (f) if the process ends with an error list the error.
 *
 *  @author amsnyder
 */
public interface DicomRetrieveStatus {

    List<DicomImageUID> currentDownloadImageList();

    boolean isFinished();

    int percentComplete();

    DicomSeriesUID getSeriesUID();

    DicomStudyUID getStudyUID();

    Date getStartTime();
    Date getFinishedTime();
    long getCompletionTimeInMilliSec();
    long getTotalBytesTransfered();

    boolean hadError();
    String getError();

}
