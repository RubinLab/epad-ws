/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.gson;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Testing JSON parsing on server-side.
 *
 * @author amsnyder
 */
public class Z_QRAdaptorServerTest {

    public static void main(String[] args){

        //Create a QueryRetrieve Example
        Gson gson = new Gson();

        GStudySearch studySearch = new GStudySearch("success","This is a test");
        Map<String,String> row1 = new HashMap<String, String>();
        row1.put("first-name","joe");
        row1.put("last-name","smith");
        studySearch.add(row1);
        Map<String,String> row2 = new HashMap<String, String>();
        row2.put("first-name","jane");
        row2.put("last-name","doe");
        studySearch.add(row2);

        String json = gson.toJson(studySearch);

        System.out.println(json);

        GStudySearch studySearch2 = gson.fromJson(json, GStudySearch.class);
        System.out.println(" -- de-serialized result --");
        System.out.println(studySearch2);

        /* Removed because it was using experimental GWT-dependent JSON handling code
        System.out.println(" -- de-serialize actual return value -- ");
        GStudySearch studySearch3 = gson.fromJson(Z_QRAdaptorClientTest.studyResult, GStudySearch.class);
        System.out.println(studySearch3);

        System.out.println(" -- in ePad usable format -- ");
        System.out.println(studySearch3.toEPadCsvFormat());
        */
    }

}
