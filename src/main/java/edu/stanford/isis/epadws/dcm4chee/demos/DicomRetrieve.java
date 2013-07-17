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
 *
 */
public class DicomRetrieve implements DicomRetrieveInterface {
    @Override
    public DicomRetrieveStatus retrieveAllImagesForSeries(DicomSeriesUID seriesUID, File destDir) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DicomRetrieveStatus retrieveOneImage(DicomImageUID imageUID, File destDir) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
