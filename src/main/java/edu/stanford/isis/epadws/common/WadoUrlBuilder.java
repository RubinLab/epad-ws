/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Build a Wado URL.
 *
 * @author amsnyder
 */
public class WadoUrlBuilder {

    private static final int INVALID = -1;
    private static final float INVALID_FLOAT = -1.0f;

    private final String hostPath;
    private final int port;
    private final String baseParam;
    private final Type contentType;

    private String studyUID=null;
    private String seriesUID=null;
    private String objectUID=null;

    private int windowLevel=INVALID;
    private int windowWidth=INVALID;

    private float zoomA = INVALID_FLOAT;
    private float zoomB = INVALID_FLOAT;
    private float zoomC = INVALID_FLOAT;
    private float zoomD = INVALID_FLOAT;

    private int zoomHeight=INVALID;
    private int zoomWidth=INVALID;

    public WadoUrlBuilder(String host, int port,String base ,Type type){
        hostPath = host;
        this.port = port;
        baseParam = base;
        contentType = type;
    }

    public void setUIDs(String studyUID, String seriesUID, String objectUID){
        setStudyUID(studyUID);
        setSeriesUID(seriesUID);
        setObjectUID(objectUID);
    }

    public void setStudyUID(String studyUID){
        this.studyUID = studyUID;
    }

    public void setSeriesUID(String seriesUID){
        this.seriesUID=seriesUID;
    }

    public void setObjectUID(String objectUID){
        this.objectUID=objectUID;
    }

    /**
     * Set window level/width.
     * @param level - DICOM image window level
     * @param width - DICOM image window width
     */
    public void setWindowLevel(int level, int width){

        throwExceptionIfNotImage();
        windowLevel=level;
        windowWidth=width;
    }

    /**
     * Set a zoom region.
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public void setZoomRegion(float a, float b, float c, float d){

        throwExceptionIfNotImage();
        zoomA=a;
        zoomB=b;
        zoomC=c;
        zoomD=d;
        checkZoomValues();
    }

    /**
     * Throw an IllegalArgumentException if the region settings are wrong.
     */
    private void checkZoomValues() {
        if( !inRangeZeroToOne(zoomA) ||
            !inRangeZeroToOne(zoomB) ||
            !inRangeZeroToOne(zoomC) ||
            !inRangeZeroToOne(zoomD)) {
            throw new IllegalArgumentException("Zoom value not with range 0.0 to 1.0");
        }

        if( (zoomA>=zoomC) ||
            (zoomB>=zoomD))
        {
            throw new IllegalArgumentException("Value relations incorrect: "
                    +zoomA+"<"+zoomC+" , "+zoomB+"<"+zoomD);
        }

    }

    /**
     *
     * @param value float
     * @return boolean - false if out of range, true if in range.
     */
    private static boolean inRangeZeroToOne(float value){
        if(value<0.0f) return false;
        if(value>1.0f) return false;
        return true;
    }

    /**
     * Set a pixel width and height for a zoom region.
     * @param height
     * @param width
     */
    public void setZoomSize(int height, int width){

        throwExceptionIfNotImage();
        zoomHeight = height;
        zoomWidth = width;

    }

    private void throwExceptionIfNotImage(){
        if(contentType!=Type.IMAGE){
            throw new IllegalStateException("Only images should have window level/width set! type="+contentType.name());
        }
    }

    private void throwExceptionIfInvalidUID(){
        if(studyUID==null){
            throw new IllegalStateException("Missing studyUID");
        }
        if(seriesUID==null){
            throw new IllegalStateException("Missing seriesUID");
        }
        if(objectUID==null){
            throw new IllegalStateException("Missing objectUID");
        }
    }

    /**
     * Build or throw an exception on a failure.
     * @return
     */
    public String build()
        throws UnsupportedEncodingException
    {
        throwExceptionIfInvalidUID();

        StringBuilder sb = new StringBuilder();
        sb.append("http://").append(hostPath);

        if(port!=INVALID){
            sb.append(":").append(port);
        }

        sb.append(baseParam);
        if(!baseParam.endsWith("?")){
            sb.append("?");
        }

        sb.append("requestType=WADO");
        sb.append("&studyUID=").append(studyUID);
        sb.append("&seriesUID=").append(seriesUID);
        sb.append("&objectUID=").append(objectUID);
        sb.append("&contentType=").append(contentType.urlEncodedContentType());

        if(contentType==Type.IMAGE){
            buildWindowLevel(sb);
            buildZoomRegion(sb);
            buildZoomSize(sb);
        }

        String raw = sb.toString();
        //String urlEncoded = java.net.URLEncoder.encode(raw,"UTF-8");

        return raw;
    }

    private void buildWindowLevel(StringBuilder sb){
        if(windowLevel!=INVALID){
            sb.append("&windowCenter=").append(windowLevel);
            sb.append("&windowWidth=").append(windowWidth);
        }
    }

    private void buildZoomRegion(StringBuilder sb){
        if(zoomA!=INVALID_FLOAT){
            sb.append("&region=").append(zoomA).append(",").append(zoomB);
            sb.append(",").append(zoomC).append(",").append(zoomD);
        }

    }

    private void buildZoomSize(StringBuilder sb){
        if(zoomHeight!=INVALID){
            sb.append("&rows=").append(zoomHeight);
            sb.append("&columns=").append(zoomWidth);
        }
    }


    /**********************
     * Return type to expect from the call.
     *
     *
     */
    public static enum Type{
        FILE("application/dicom"),
        IMAGE("image/jpeg"),
        TEXT("text/html"),
        XML("text/xml");

        Type(String type){
            contentType=type;
        }

        String contentType;

        private String contentType(){
            return contentType;
        }

        /**
         * Get the content type, but URL encode it first.
         * @return String content that is URL encoded.
         * @throws UnsupportedEncodingException
         */
        public String urlEncodedContentType()
            throws UnsupportedEncodingException
        {
            return URLEncoder.encode(contentType(),"UTF-8");
        }

        @Override
        public String toString(){
            return contentType();
        }
    }

}
