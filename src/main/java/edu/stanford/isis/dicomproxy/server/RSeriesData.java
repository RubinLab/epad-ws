/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.server;

import java.util.Map;

import org.dcm4che2.data.Tag;

import edu.stanford.isis.dicomproxy.common.DicomQueryLevel;
import edu.stanford.isis.dicomproxy.common.DicomSeriesData;
import edu.stanford.isis.dicomproxy.common.DicomSeriesUID;
import edu.stanford.isis.dicomproxy.common.DicomTag;

/**
 * DicomSeries
 *
 * @author amsnyder
 */
public class RSeriesData implements DicomSeriesData {

    Map<DicomTag,String> tagMap;

    String thumbnail=null;
    
    public RSeriesData(Map<DicomTag,String> map){
        tagMap = map;
    }

    @Override
    public DicomSeriesUID getSeriesId() {
        return new DicomSeriesUID(getTag( Tag.SeriesInstanceUID ));
    }

    @Override
    public DicomQueryLevel getType() {
        return DicomQueryLevel.SERIES;
    }

    @Override
    public String getPatientId() {
        return getTag( Tag.PatientID );
    }

    @Override
    public String getPatientName() {
        return getTag( Tag.PatientName );
    }

    @Override
    public String getStudyDate() {
        //ToDo: Change the method name, and find tags that have data.
        return getTag( Tag.SeriesDate );
    }

    @Override
    public String getExamType() {
        return getTag( Tag.Modality );
    }

    @Override
    public String getThumbnailURL() {

        if(thumbnail==null){
            return "placeholder";
        }else{
            return thumbnail;
        }
    }

    public void setThumbnailURL(String path) {
        thumbnail=path;
    }

    @Override
    public boolean hasImageData() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Header names are sent in comma-delimited format.
     * @return
     */
    public static String getHeaderColumn(){
    	
    	ProxyConfig config = ProxyConfig.getInstance();
    	String separator = config.getParam("fieldSeparator");
    	
        StringBuilder sb = new StringBuilder();
        sb.append("Series Id ").append(separator);
        sb.append(" Patient Id ").append(separator);
        sb.append(" Patient Name ").append(separator);
        sb.append(" Series Date ").append(separator);
        sb.append(" Exam Type ").append(separator);
        sb.append(" Thumbnail URL ").append(separator);
        sb.append(" SeriesDescription ").append(separator);
        sb.append(" NumberOfSeriesRelatedInstances ").append(separator);
        sb.append(" ImagesInSeries ").append(separator);
        sb.append(" PNG Not Ready ").append(separator);
        sb.append(" Body Part ").append(separator);
        sb.append(" Institution ").append(separator);
        sb.append(" Station Name ").append(separator);
        sb.append(" Department");

        return sb.toString();
    }

    /**
     * Data is sent on comma-delimited format.
     * @return
     */
    public String getDataColumn(){
    	
    	ProxyConfig config = ProxyConfig.getInstance();
    	String separator = config.getParam("fieldSeparator");
    	
        StringBuilder sb = new StringBuilder();
        sb.append(getSeriesId()).append(separator);
        sb.append(getPatientId()).append(separator);
        sb.append(getPatientName()).append(separator);
        sb.append(getStudyDate()).append(separator);
        sb.append(getExamType()).append(separator);
        sb.append(getThumbnailURL()).append(separator);
        sb.append(getTag(Tag.SeriesDescription)).append(separator);
        sb.append(getTag(Tag.NumberOfSeriesRelatedInstances)).append(separator);
        sb.append(getTag(Tag.ImagesInSeries));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tagMap).append(" -- ").append(getDataColumn());
        return sb.toString();
    }

    /**
     * utility method. This might go in an abstract class.
     * @param tagValue
     * @return String
     */
    private String getTag( int tagValue ){
        return tagMap.get( DicomTag.forInt(tagValue));
    }
}
