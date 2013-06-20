/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.dicom;

import edu.stanford.isis.epadws.common.DicomTagFileUtils;
import edu.stanford.isis.epadws.common.ProxyFileUtils;
import edu.stanford.isis.epadws.server.ProxyLogger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds search results.
 */
public class RsnaSearchResultMap {

    private static RsnaSearchResultMap ourInstance = new RsnaSearchResultMap();

    /**
     *
     */
    private final Map<String,String> resultMap = new ConcurrentHashMap<String, String>();
    private String defaultResult = null;

    private final ProxyLogger log;


    public static RsnaSearchResultMap getInstance() {
        return ourInstance;
    }

    private RsnaSearchResultMap() {
        log = ProxyLogger.getInstance();
        readResultsFiles();
    }

    public Map<String,String> getResultMap(){
        return resultMap;
    }

    public String getDefaultResult(){
        return defaultResult;
    }

    /**
     * Reads *.result files from the base directory. ../resources/rsna/
     */
    private void readResultsFiles()
    {
        try{
            File[] resultFiles = getRsnaSearchFilesOfType(".results");

            for(File currResFile : resultFiles){
                log.info("Found result file: "+currResFile.getAbsolutePath());
                parseResultFile(currResFile);
            }//for

        }catch(Exception e){
            log.sever("Failed to read all RSNA results files.", e);
        }
    }//readResultsFiles


    private File[] getRsnaSearchFilesOfType(final String extension){
        try{
            File baseDir = new File("../resources/rsna");
            log.info("RSNA Search "+extension+" reading from: "+baseDir.getAbsolutePath());

            //get all the result files.
            File[] resultFiles = baseDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(extension);
                }
            });
            log.info("RsnaSearchResultMap.getRsnaSearchFilesOfType - found "
            		+ resultFiles.length+" files ending with " + extension);
            return resultFiles;
        }catch(Exception e){
            log.info("RsnaSearchResultMap.getRsnaSearchFilesOfType -" 
            		+ " failed to find files of ending with: "+extension);
            return new File[0];
        }
    }
    /**
     * Parse a result file which is expected to be in the format.
     *
     * One key per line above
     * #### RESULT ####
     * One result below.
     *
     * @param resultFile File
     */
    private void parseResultFile(File resultFile){

    	log.info("################## START Parsing RSNA file _"+resultFile.getName()+"_ ##########################");
    	try {
    		BufferedReader in = new BufferedReader(new FileReader(resultFile.getAbsolutePath()));
    		try {
    			boolean isResultSection=false;
    			String line;
    			StringBuilder resultBuffer = new StringBuilder();
    			List<String> searchKeyList = new ArrayList<String>();
    			while((line = in.readLine())!=null){

    				if(line.toLowerCase().indexOf("# result #")>0){
    					isResultSection=true;
    					continue;
    				}

    				if(isResultSection){
    					resultBuffer.append(line).append("\n");
    				}else{
    					searchKeyList.add(line.toLowerCase().trim());
    				}
    			}

    			if(resultBuffer.length()>0 && searchKeyList.size()>0){
    				for(String key : searchKeyList){
    					if( resultMap.containsKey(key) ){
    						log.info("WARNING: Overwriting: "+key+" with new result.");
    					}
    					resultMap.put(key,resultBuffer.toString());
    					log.info("RSNA Search: resultMap: key="+key+" | value="+resultBuffer.toString());
    				}
    			}else{
    				log.info("WARNING. Result file might be invalid: "+resultFile.getAbsolutePath());
    			}

    			//save if this is a default result.
    			if(resultFile.getName().toLowerCase().startsWith("default")){
    				log.info("Using default result for file: "+resultFile.getAbsolutePath() );
    				defaultResult = resultBuffer.toString();
    			}
    		} finally {
    			in.close();
    		}
    	} catch (FileNotFoundException e) {
    		log.sever("Failed to read file: "+resultFile.getAbsolutePath(),e);
    	} catch (IOException e) {
    		log.sever("I/O Error. File: "+resultFile.getAbsolutePath(),e);
    	}
    	finally {
    		log.info("################## END Parsing RSNA file ##########################");
    	}
    }


    /**
     * Get the StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired from the
     * Map and create a line to add to the rsnaResult file.
     *
     * @param dicomTagMap Map of DICOM tag key-value pairs.
     */
    public void addResult(Map<String,String> dicomTagMap){

        String studyUID = dicomTagMap.get(DicomTagFileUtils.STUDY_UID);
        String patientName = dicomTagMap.get(DicomTagFileUtils.PATIENT_NAME);
        String patientID = dicomTagMap.get(DicomTagFileUtils.PATIENT_ID);
        String examType = dicomTagMap.get(DicomTagFileUtils.MODALITY);
        String dateAcquired = dicomTagMap.get(DicomTagFileUtils.STUDY_DATE);

        if(patientID==null || patientName==null){
            log.info("found null patient value. DEBUG string is next.");
            debugDicomTagMap(dicomTagMap);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(studyUID).append(", ");
        sb.append(patientName).append(", ");
        sb.append(patientID).append(", ");
        sb.append(examType).append(", ");
        sb.append(dateAcquired);

        addResult(sb.toString());
    }

    public void debugDicomTagMap(Map<String,String> dicomTagMap){
        log.info(" dicomTagMap: "+dicomTagMap.toString());
    }

    /**
     *
     * @return String
     */
    public String debugPrintResultsMap(){
        return resultMap.toString();
    }

    /**
     * Add a line to all the results.
     * @param resultLine String
     */
    public void addResult(String resultLine){

        Set<String> resultKeys = resultMap.keySet();
        for(String key : resultKeys){
            String result = resultMap.get(key);

            if(!result.contains(resultLine)){
                if( !result.endsWith("\n") ){
                    result = result+"\n";
                }
                result = result+resultLine;
                resultMap.put(key,result);

                log.info("Updating rsnaEverything.result too.");
                log.info(result);
                log.info("add Result key="+key);
            }//if

        }//for

        log.info("addResult to RsnaSearchMap: resultLine="+resultLine);

        updateRSNAFile(resultLine);
    }

    /**
     * If "resultLine" doesn't already exist in the RSNAEverything.result file, then append it to the end of the
     * file.
     *
     * @param resultLine String The new file to add to RSNA Everything results.
     * @return boolean true is a new file was written otherwise false.
     */
    private boolean updateRSNAFile(String resultLine){

        try {

            File rsnaEverythingFile = new File("../resources/rsna/rsnaEverything.results");
            if( !rsnaEverythingFile.exists() ){
                log.info("WARNING: rsnaEverythingFile not found. Will not add the following line: "+resultLine);
            }
            String rsnaEverythingContents = ProxyFileUtils.read(rsnaEverythingFile);

            if(!rsnaEverythingContents.contains(resultLine)){
                //append the current file to the end.
                String newContents = rsnaEverythingContents;
                if( !rsnaEverythingContents.endsWith("\n") ){
                    newContents = newContents+"\n";
                }
                newContents = newContents+resultLine;

                ProxyFileUtils.overwrite(rsnaEverythingFile,newContents);
                log.info("Updated rsnaEverything.results file with: \n"+newContents);
                return true;
            }
            log.info("Not updating rsnaEverything.results file with: "+resultLine+" since it was already there.");
            return false;
        } catch (IOException e) {
            log.warning("Failed to update rsnaEverything.results file for: "+e.getMessage(),e);
            return false;
        }

    }

}
