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

import java.io.File;

/**
 * Interfaces to start a DICOM RETRIEVE process.
 *
 * NOTE: THIS IS PRELIMINARY AND LIKELY TO CHANGE. GIVE FEEDBACK.
 *
 * @author amsnyder
 */
public interface DicomRetrieveInterface {

    /**
     * Retrieve all the images for a specific series.
     * @param seriesUID - seriesUID
     * @param destDir - destination directory for the files.
     * @return DicomRetrieveStatus
     */
    DicomRetrieveStatus retrieveAllImagesForSeries(DicomSeriesUID seriesUID, File destDir); //Do we need parent studyId?

    /**
     * Get just a single image identified by the SopInstanceUID.
     * @param imageUID
     * @param destDir - destination directory for the files.
     * @return DicomRetrieveStatus
     */
    DicomRetrieveStatus retrieveOneImage(DicomImageUID imageUID, File destDir);

}
