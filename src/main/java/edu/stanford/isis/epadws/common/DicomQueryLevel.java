/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

/**
 * @author amsnyder
 */
public enum DicomQueryLevel {

    STUDY("edu.stanford.isis.dicomproxy.DicomStudyData",true),
    SERIES("edu.stanford.isis.dicomproxy.DicomSeriesData",true),
    INSTANCE("edu.stanford.isis.dicomproxy.DicomImageData",false);

    String className;
    boolean hasDicomRowData;

    DicomQueryLevel(String className, boolean hasRowData){
        this.className = className;
        hasDicomRowData = hasRowData;
    }

    /**
     *
     * @return Class type of class this should be cast to.
     * @throws ClassNotFoundException
     */
    Class<?> cast()
        throws ClassNotFoundException
    {
        return Class.forName(className);
    }

    /**
     * @return true if type has DicomRowData interface.
     */
    boolean hasDicomRowData(){
        return hasDicomRowData;
    }
}
