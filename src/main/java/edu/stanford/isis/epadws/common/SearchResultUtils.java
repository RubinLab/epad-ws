/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

import edu.stanford.isis.epadws.server.ProxyConfig;

/**
 *
 *
 * @author amsnyder
 */
public class SearchResultUtils {

	String separator = null;
	
    public SearchResultUtils(){
        ProxyConfig config = ProxyConfig.getInstance();
        separator = config.getParam("fieldSeparator");
    }

    public String get_STUDY_SEARCH_HEADER(){
    	//public static final String STUDY_SEARCH_HEADER = "StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired\n";
         String STUDY_SEARCH_HEADER =
        	"StudyUID"
        			+separator+
        	"Patient Name"
        			+separator+
        	"Patient ID"
        			+separator+
        	"Exam Type"
        			+separator+
        	"Date Acquired" 
        			+separator+
        	"PNG Not Ready"
        			+separator+
        	"Series Count" 
        			+separator+
        	"First SeriesUID"
        			+separator+
        	"First Series Date Acquired"
        			+separator+
        	"Study Accession Number"
        			+separator+
        	"Images Count"
        			+separator+
        	"StudyID"
        			+separator+
        	"Study Description"
        			+separator+
        	"Physician Name"
        			+separator+
        	"Patient Birthdate" 
        			+separator+
        	"Patient Sex\n"; 
         
         return STUDY_SEARCH_HEADER;
    }
    
    public String get_EVENT_SEARCH_HEADER(){
    	//public static final String STUDY_SEARCH_HEADER = "StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired\n";
         String EVENT_SEARCH_HEADER =
        	"event_number"
        			+separator+
        	"event_status"
        			+separator+
        	"Date"
        			+separator+
        	"aim_uid"	
        			+separator+
        	"aim_name"
        			+separator+
        	"patient_id"
        			+separator+
        	"patient_name"
        			+separator+
        	"template_id"
        			+separator+
        	"template_name"
        			+separator+
        	"plugin_name\n";

         return EVENT_SEARCH_HEADER;
    }
    
    
    
    public String get_ALL_SERIES_IN_STUDY_HEADER(){
    	String ALL_SERIES_IN_STUDY_HEADER = "";
    	return ALL_SERIES_IN_STUDY_HEADER;
    }


}
