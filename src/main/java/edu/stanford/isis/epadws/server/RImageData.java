/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server;

import edu.stanford.isis.epad.common.dicom.DicomImageData;
import edu.stanford.isis.epad.common.dicom.DicomImageUID;
import edu.stanford.isis.epad.common.dicom.DicomQueryLevel;
import edu.stanford.isis.epad.common.dicom.DicomTag;

import org.dcm4che2.data.Tag;

import java.util.Map;

/**
 *
 * @author amsnyder
 */
public class RImageData implements DicomImageData, Comparable<DicomImageData>, RDataColumn {

    Map<DicomTag,String> tagMap;

    public RImageData(Map<DicomTag,String> map){
        tagMap = map;
    }

    @Override
    public int getStackOrder() {
        return new Integer( getTag( Tag.InstanceNumber ) );
    }

    @Override
    public String getRawDataURL() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DicomImageUID getSopInstanceId() {
        return new DicomImageUID(getTag( Tag.SOPInstanceUID )  );
    }

    @Override
    public DicomQueryLevel getType() {
        return DicomQueryLevel.INSTANCE;
    }

    @Override
    public String getPatientId() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPatientName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getStudyDate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getExamType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * utility method. This might go in an abstract class.
     * @param tagValue
     * @return String
     */
    private String getTag( int tagValue ){
        return tagMap.get( DicomTag.forInt(tagValue));
    }


    /**
     * Sortable by stack order
     * @param that
     * @return int (-)negative value if this is before that.
     */
    @Override
    public int compareTo(DicomImageData that) {
        return this.getStackOrder()-that.getStackOrder();
    }

    public static String getHeaderColumn() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack Order, ");
        sb.append("SOPInstance ");

        return sb.toString();
    }

    @Override
    public String getDataColumn() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStackOrder()).append(", ");
        sb.append(getSopInstanceId());

        return sb.toString();
    }
}
