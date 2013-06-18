/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Some common date formats for the DicomProxy/ePad project.
 */
public class ProxyDateFormatter {

    public static final String YEAR_MONTH_DATE = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DATE_HMS = "yyyy-MM-dd hh:mm:ss";
    public static final String DAY_MONTH = "EEE, MMM d, ''yy";

    /**
     *
     * @param date Date
     * @param formatConst String
     * @return String
     */
    public static String format(Date date, String formatConst){
        SimpleDateFormat sdf = new SimpleDateFormat(formatConst);
        return sdf.format(date);
    }

    /**
     * Get the current time in a format.
     * @param formatConst String
     * @return String
     */
    public static String currentTimeInFormat(String formatConst){
        return format(new Date(System.currentTimeMillis()),formatConst);
    }

}
