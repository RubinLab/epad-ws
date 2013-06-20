/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.common;

/**
 *
 * @author amsnyder
 */
public enum DicomSearchType
{

    PATIENT_NAME("patientName"),
    PATIENT_ID("patientId"),
    ASSESION_NUM("accessionNum"),
    EXAM_TYPE("examType"),
    STUDY_DATE("studyDate"),
    WORK_LIST_NAME("workListName");

    private String name;

    DicomSearchType(String type){
        this.name =type;
    }

    public String toString(){
        return name;
    }

}
