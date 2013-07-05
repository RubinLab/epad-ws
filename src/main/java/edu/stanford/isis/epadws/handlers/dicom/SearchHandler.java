/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.handlers.dicom;

import edu.stanford.isis.epadws.common.*;
import edu.stanford.isis.epadws.server.*;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * Handles all search request APIs.
 *
 * @author amsnyder
 */
public class SearchHandler extends AbstractHandler
{
    private final ProxyManager proxyManager;
    private static final ProxyLogger log = ProxyLogger.getInstance();
    private ProxyWorkers proxyWorkers = ProxyWorkers.getInstance();

    public SearchHandler(ProxyManager proxyManager){
        this.proxyManager = proxyManager;
    }

    @Override
    public void handle(String s, Request request,
                       HttpServletRequest httpRequest,
                       HttpServletResponse httpResponse)
            throws IOException, ServletException
    {
        httpResponse.setContentType("application/gson"); //switch to text/plain
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.setHeader("Access-Control-Allow-Origin","*");
        request.setHandled(true);

        PrintWriter out = httpResponse.getWriter();
        try{
            if(isImageSearch(httpRequest)){
                performImageSearch(httpRequest,out);
            }else if( isSeriesSearch(httpRequest) ){
                performSeriesSearch(httpRequest,out);
            }else{
                performStudySearch(httpRequest,out);
            }

        }catch(Exception e){
            out.print("ERROR: Search Failed. See proxy log for details. message="+e.getMessage());
            log.warning("DICOM Search Failed",e);
            e.printStackTrace();
        }
        out.flush();

    }

    private void performImageSearch(HttpServletRequest httpRequest, PrintWriter out){
        String remoteAddr = httpRequest.getRemoteAddr();
        String seriesid = httpRequest.getParameter("seriesUID");

        log.info("Image search. remoteAddr="+remoteAddr);

        List<DicomImageData> images = proxyManager.getImagesForSeries(new DicomSeriesUID(seriesid));

        //sort the images.
        Collections.sort(images, new DicomImageSorter());

        out.print(RImageData.getHeaderColumn()+"\n");
        for(DicomImageData currImage : images){
            RImageData currRImage = (RImageData) currImage;
            out.print(currRImage.getDataColumn()+"\n");
        }

    }

    /**
     * Do a search for "series" and start a process to get more data.
     * @param httpRequest HttpServletRequest
     * @param out PrintWriter
     */
    private void performSeriesSearch(HttpServletRequest httpRequest, PrintWriter out) {

        String remoteAddr = httpRequest.getRemoteAddr();
        String studyid = httpRequest.getParameter("studyUID");

        log.info("Series search. remoteAddr="+remoteAddr);

        DicomSearchResult searchResult = proxyManager.getSeriesSearchResult(studyid,remoteAddr);

        addStudyToDataDownloadStack(new DicomStudyUID(studyid),remoteAddr);

        List<DicomSeriesData> series = searchResult.getSeriesForStudyId(studyid);

        out.print(RSeriesData.getHeaderColumn()+"\n");
        for(DicomSeriesData currSeries : series){
            RSeriesData d = (RSeriesData)currSeries;
            out.print(d.getDataColumn()+"\n");

            addSeriesToThumbnailCreatorQueue(d);
        }
    }

    /**
     * Add this seriesId to the thumbnail generator queue to start the process of
     * creating a thumbnail image.
     * @param seriesData RSeriesData
     */
    private void addSeriesToThumbnailCreatorQueue(RSeriesData seriesData){
        DicomSeriesUID seriesId = seriesData.getSeriesId();
        proxyWorkers.createThumbnailForSeries(seriesId);
    }


    private void addStudyToDataDownloadStack(DicomStudyUID studyId, String remoteAddr){
        proxyWorkers.showInterestInStudy(studyId,remoteAddr);
    }

    /**
     * Do a search for "Studies".
     * @param httpRequest -
     * @param out -
     */
    private void performStudySearch(HttpServletRequest httpRequest, PrintWriter out) {
        DicomSearchType searchType = getSearchType(httpRequest);

        log.info("Study search request for type: "+searchType);

        DicomSearchResult searchResult = proxyManager.getSearchResult(searchType,getSearchParam(searchType,httpRequest));
        SearchResultCache cache = SearchResultCache.getInstance();
        cache.setMostRecent((SearchResult)searchResult,httpRequest.getRemoteAddr());


        List<DicomStudyData> studies = searchResult.getStudies();
        log.info("Search for: "+searchType+" found "+studies.size()+" results.");

        //out.print("StudyUID, Patient Name, Patient ID, Exam Type, Date Acquired\n");
        out.print(new SearchResultUtils().get_STUDY_SEARCH_HEADER());
        for(DicomStudyData currStudy : studies){

            String pName = currStudy.getPatientName();
            //currStudy.getNumberOfSeries(); //ToDo: get the number of series for this Study.
            String pId = currStudy.getPatientId();
            String modality = currStudy.getExamType();
            String studyDate = currStudy.getStudyDate();
            //ToDo: getStudyTime();
            String studyUID = currStudy.getStudyId().toString(); //This is a hidden field for the table.

            out.print(studyUID+","+pName+","+pId+","+modality+","+studyDate+"\n");
        }
    }

    /**
     * True if this is a "series" search.
     * @param httpRequest
     * @return
     */
    private boolean isSeriesSearch(HttpServletRequest httpRequest) {

        String searchType = httpRequest.getParameter("searchtype");
        if(searchType!=null){
            return searchType.equalsIgnoreCase("series");
        }

        return false;
    }

    /**
     * True if this is a "images" search.
     * @param httpRequest
     * @return
     */
    private boolean isImageSearch(HttpServletRequest httpRequest) {
        String searchType = httpRequest.getParameter("searchtype");
        if(searchType!=null){
            return searchType.equalsIgnoreCase("images");
        }

        //log.debug("Not an image search: searchtype="+searchType);

        return false;
    }


    /**
     *
     * @param httpRequest
     * @return
     */
    private DicomSearchType getSearchType(HttpServletRequest httpRequest) {
        for(DicomSearchType curr : DicomSearchType.values()){

            if( (httpRequest.getParameter(curr.toString())!=null) ){
                return curr;
            }
        }
        throw new IllegalArgumentException("Request missing search parameter. Req="+httpRequest.toString());
    }


    /**
     *
     * @param searchType
     * @param httpRequest
     * @return
     */
    private String getSearchParam(DicomSearchType searchType, HttpServletRequest httpRequest){
        return httpRequest.getParameter(searchType.toString());
    }

}
