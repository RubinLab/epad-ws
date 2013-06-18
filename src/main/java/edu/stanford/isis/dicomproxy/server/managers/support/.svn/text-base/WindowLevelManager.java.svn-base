/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server.managers.support;

import java.util.HashMap;
import java.util.Map;

/**
 * NOTE: THis might be redundant with WADO.
 *
 * @author amsnyder
 */
public class WindowLevelManager
{

    private static WindowLevelManager ourInstance = new WindowLevelManager();

    private static final DicomWindowSetting unleveledA =new DicomWindowSetting(4096,2048);
    private static final DicomWindowSetting unleveledB =new DicomWindowSetting(4096,1036);

    private static final DicomWindowSetting abdomen =new DicomWindowSetting(4096,2048);
    private static final DicomWindowSetting brain =new DicomWindowSetting(4096,2048);
    private static final DicomWindowSetting lung =new DicomWindowSetting(4096,2048);
    private static final DicomWindowSetting bone =new DicomWindowSetting(4096,2048);

    //this has either the "current" or "series-id" setting.
    private Map<String,DicomWindowSetting> currSettings = new HashMap<String,DicomWindowSetting>();
    private String currentSeries;

    //public static final String CURRENT = "current";

    public static WindowLevelManager getInstance() {
        return ourInstance;
    }

    private WindowLevelManager() {
    }



    public void updateSetting(int width, int level, String seriesKey){
        currentSeries = seriesKey;
        currSettings.put(seriesKey,new DicomWindowSetting(width,level));
    }

    public DicomWindowSetting getSetting(String seriesKey){
        return currSettings.get(seriesKey);
    }

    public DicomWindowSetting getSetting(){
        return currSettings.get(currentSeries);
    }

}
