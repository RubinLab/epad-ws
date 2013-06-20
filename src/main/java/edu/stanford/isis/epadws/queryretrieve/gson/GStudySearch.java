/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.queryretrieve.gson;

import edu.stanford.isis.epadws.common.SearchResultUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson-style container class for JSON parsing of "study search results".
 *
 * @author amsnyder
 */
public class GStudySearch {

    String result;
    String message;
    List<Map<String,String>> list = new ArrayList<Map<String, String>>();

    public GStudySearch(String result, String message){
        this.result=result;
        this.message=message;
    }

    public void add(Map<String,String> map){
        list.add(map);
    }

    /**
     * Write the result into a format used by ePad.
     * @return String in format used by ePad
     */
    public String toEPadCsvFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append(new SearchResultUtils().get_STUDY_SEARCH_HEADER());
        for(Map<String,String> curr : list){
            sb.append(QRAdaptor.formatResult(curr.get("StudyInstanceUID"),"")).append(",");
            sb.append(QRAdaptor.formatResult(curr.get("PatientName"),"")).append(",");
            sb.append(QRAdaptor.formatResult(curr.get("PatientID"),"")).append(",");
            sb.append(QRAdaptor.formatResult(curr.get("ModalitiesInStudy"),"")).append(",");
            sb.append(QRAdaptor.formatResult(curr.get("StudyDate"),"")).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("r=").append(result).append("\n");
        sb.append("m=").append(message).append("\n");
        for(Map<?,?> currMap : list){
            sb.append("row:").append(currMap).append("\n");
        }
        return sb.toString();
    }
}
