/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.jso;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
//import com.google.gwt.gson.client.JSONObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import edu.stanford.isis.dicomproxy.common.SearchResultUtils;
import edu.stanford.isis.dicomproxy.queryretrieve.jso.*;

/**
 * Turn JSON responses into current format used by ePad client.
 * NOTE: Although we will eventually want ePad client to handle this type of response.
 *
 * @author amsnyder
 */
public class QRAdaptorClient {

    public static String adaptStudySearchResult(String jsonResponse){

        JSONValue jsonValue = JSONParser.parseLenient(jsonResponse);
        StudySearch studySearch = (StudySearch)jsonValue.isObject().getJavaScriptObject();

        StringBuilder sb = new StringBuilder();
        sb.append(new SearchResultUtils().get_STUDY_SEARCH_HEADER());

        if( !isSuccess(studySearch.getResult())){
            logFailure(studySearch.getMessage());
        }

        JsArray<StudySearchEntry> rows = studySearch.getList();
        for(int i=0; i<rows.length(); i++){
            StudySearchEntry entry =rows.get(i);

            sb.append(formatResult(entry.getStudyInstanceUID()," ")).append(", ");
            sb.append(formatResult(entry.getPatientName()," ")).append(", ");
            sb.append(formatResult(entry.getPatientID()," ")).append(", ");
            sb.append(formatResult(entry.getModalitiesInStudy()," ")).append(", ");
            sb.append(formatResult(entry.getStudyDate()," ")).append("\n");
        }

        return sb.toString();
    }

    public String adaptSeriesSearchResult(String jsonResponse){

        JSONValue jsonValue = JSONParser.parseLenient(jsonResponse);
        SeriesSearch seriesSearch = (SeriesSearch)jsonValue.isObject().getJavaScriptObject();
        if( !isSuccess(seriesSearch.getResult())){
            logFailure(seriesSearch.getMessage());
        }

        throw new UnsupportedOperationException("not implemented");
    }

    public String adaptServersResult(String jsonResponse){

        JSONValue jsonValue = JSONParser.parseLenient(jsonResponse);
        Servers servers = (Servers)jsonValue.isObject().getJavaScriptObject();
        if( !isSuccess(servers.getResult())){
            logFailure(servers.getMessage());
        }

        JsArray<ServersEntry> serversList =  servers.getList();
        for(int i=0; i<serversList.length(); i++){
            ServersEntry entry = serversList.get(i);

        }

        //ToDo: define what this should be, it is used in the drop-down list.


        throw new UnsupportedOperationException("not implemented");
    }

    private static boolean isSuccess(String result){
        return "success".equalsIgnoreCase(result);
    }

    private static void logFailure(String message){

    }

    private static String formatResult(String result, String defaultValue){
        if(result==null){
            return defaultValue;
        }
        return result;
    }

}
