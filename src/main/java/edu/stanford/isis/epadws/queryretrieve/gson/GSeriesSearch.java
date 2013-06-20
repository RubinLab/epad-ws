/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.queryretrieve.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * gson-style container class for JSON parsing of "study series results".
 *
 * @author amsndyer
 */
public class GSeriesSearch {

    String result;
    String message;
    List<Map<String,String>> list = new ArrayList<Map<String, String>>();

    public GSeriesSearch(String result, String message){
        this.result=result;
        this.message=message;
    }

    public String toEPadCsvFormat(){
        //StringBuilder sb = new StringBuilder();
        throw new UnsupportedOperationException("not implemented. See GStudySearch.java for example.");
        //return sb.toString();
    }

}
