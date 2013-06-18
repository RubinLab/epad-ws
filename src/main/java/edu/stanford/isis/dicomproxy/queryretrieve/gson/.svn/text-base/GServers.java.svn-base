/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * gson-style container class for JSON parsing of "study series results".
 *
 * @author amsnyder
 */
public class GServers {
    String configFile;
    String result;
    String message;
    List<Map<String,String>> list = new ArrayList<Map<String, String>>();

    public String toEPadCsvFormat(){
        throw new UnsupportedOperationException("CVS format to ePAD not designed. Maybe just pass the JSON.");
    }
}
