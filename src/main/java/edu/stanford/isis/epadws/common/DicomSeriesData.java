/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

/**
 * Interfaces specific to series.
 *
 * @author amsnyder
 */
public interface DicomSeriesData extends DicomData, DicomRowData{

    /**
     * The URL to a 64x64 pixel thumbnail image.
     * @return
     */
    String getThumbnailURL();


    DicomSeriesUID getSeriesId();

    /**
     * Returns true if the Image data has been queried;
     * @return boolean true if it has queries the image data.
     */
    boolean hasImageData();
}
