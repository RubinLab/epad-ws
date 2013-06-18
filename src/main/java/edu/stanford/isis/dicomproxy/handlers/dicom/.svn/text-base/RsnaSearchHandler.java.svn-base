/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.handlers.dicom;

import edu.stanford.isis.dicomproxy.server.ProxyLogger;
import edu.stanford.isis.dicomproxy.server.managers.pipeline.SeriesFileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * This is a mock class for doing Searches without a DicomServer. Instead
 * it reads some "rsna*.results files on startup.
 *
 * It reads the [series_id].series file for getSeriesFromStudy calls.
 */
public class RsnaSearchHandler extends AbstractHandler
{
    private static final ProxyLogger log = ProxyLogger.getInstance();

    private final RsnaSearchResultMap data = RsnaSearchResultMap.getInstance();

    public RsnaSearchHandler(){
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws IOException, ServletException
    {
        httpResponse.setContentType("application/gson");   //switch to text/plain
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setHeader("Access-Control-Allow-Origin","*");
        request.setHandled(true);

        PrintWriter out = httpResponse.getWriter();

        String queryString = httpRequest.getQueryString();
        if(queryString!=null){

            queryString = queryString.toLowerCase().trim();

            if( isSeriesRequest(queryString) ){
                handleSeriesRequest(out, queryString);
            }else{
                handleStudyRequest(out,queryString);
            }

        }else{
            log.info("NO Query from request.");
        }
        out.flush();
    }

    private void handleStudyRequest(PrintWriter out, String queryString){

        Map<String,String> resultMap = data.getResultMap();

        if( resultMap.containsKey(queryString) ){
            String result = resultMap.get(queryString);
            out.print(  result );
            log.info("StudyRequest found result: "+result);
        }else if(queryString.contains("debug")){
            String allResults = resultMap.toString();
            out.print("debug\n");
            out.print(allResults);
            log.info("All Results: ");
            log.info(allResults);
        }else{
            log.info("RsnaSearchHandler: StudyRequest failed to find a result for queryString = _"+queryString+"_. Using DEFAULT result.");
            out.print(data.getDefaultResult());
        }
    }

    /**
     * Get all the series for a study.
     * @param out PrintWriter for the output.
     * @param queryString String - query string which contains the study id. The query
     * line looks like the following:
     *   http://[ip:port]/search?searchType=series&studyUID=[studyID].
     *
     *  The return is text in a CSV format. The first line are the keys, and all the following lines
     *  are data:
     *
     *  keys:
     *  "Series Id, Patient Id, Patient Name, Series Date, Exam Type, Thumbnail URL, Series Description,
     *  NumberOfSeriesRelatedInstances, ImagesInSeries".
     *
     *  The values are one line per series.
     *
     *  Here we will look for *.series files within the study directory. If it is there then
     *  It will read that file and add it to the result.
     */
    private void handleSeriesRequest(PrintWriter out, String queryString)
    {
        String studyIdKey = getStudyUIDFromRequest(queryString);

        List<File> seriesFiles = SeriesFileUtils.getSeriesFileFor(studyIdKey);
        String result = SeriesFileUtils.parseSeriesFiles(seriesFiles);

        log.info("RsnaSearchHandler, Series result is: \n_"+result+"_");
        out.print(result);
    }



    private static String getStudyUIDFromRequest(String queryString){
        queryString = queryString.toLowerCase();
        int index = queryString.indexOf("&studyuid=");
        String end = queryString.substring(index);
        end = end.replace('=',' ');
        String[] parts = end.split(" ");
        String key=parts[1].replace('.','_');
        log.info("key="+key+",   queryString="+queryString);
        return key;
    }


    private boolean isSeriesRequest(String queryString){
        queryString = queryString.toLowerCase().trim();
        boolean isSeries = queryString.indexOf("earchtype=series")>0;

        log.info(" isSeries="+isSeries+" for: "+queryString);
        return isSeries;
    }

}
