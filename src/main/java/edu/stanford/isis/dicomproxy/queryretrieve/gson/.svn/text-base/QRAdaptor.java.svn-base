/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.gson;

import com.google.gson.Gson;


/**
 * JSON parse on the server side implemented using gson library.
 * The JSON format arrives from the DicomInterface.war file and is translated
 * into a format used by ePAD. See the SearchHandlers for example.
 *
 * In the long run we want to switch to just passing the JSON and changing ePAD to
 * deal with it.
 *
 * @author amsnyder
 */
public class QRAdaptor
{

    public static String adaptStudySearchResult(String jsonString){
        Gson gson = new Gson();
        GStudySearch studySearch = gson.fromJson(jsonString, GStudySearch.class);
        return studySearch.toEPadCsvFormat();
    }

    public static String adaptSeriesSearchResult(String jsonString){
        Gson gson = new Gson();
        GSeriesSearch seriesSearch = gson.fromJson(jsonString, GSeriesSearch.class);
        return seriesSearch.toEPadCsvFormat();
    }

    public static String adaptServersResult(String jsonString){
        Gson gson = new Gson();
        GServers servers = gson.fromJson(jsonString,GServers.class);
        return servers.toEPadCsvFormat();
    }

    public static String formatResult(String result, String defaultValue){
        if(result==null){
            return defaultValue;
        }
        return result;
    }

}
