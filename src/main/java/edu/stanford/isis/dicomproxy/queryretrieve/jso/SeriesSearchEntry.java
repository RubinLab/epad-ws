/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.queryretrieve.jso;

/**
 *
 * @author amsnyder
 */
public class SeriesSearchEntry {

    protected SeriesSearchEntry(){}

    public final native String getStudyInstanceUID()/*-{ return this.StudyInstanceUID; }-*/;
    public final native String getStudyDate()/*-{ return this.StudyDate; }-*/;
    public final native String getSeriesNumber()/*-{ return this.SeriesNumber; }-*/;
    public final native String getNumberOfSeriesRelatedInstances()/*-{ return this.NumberOfSeriesRelatedInstances; }-*/;
    public final native String getModalitiesInStudy()/*-{ return this.ModalitiesInStudy; }-*/;
    public final native String getPatientBirthDate()/*-{ return this.PatientBirthDate; }-*/;
    public final native String getNumberOfStudyRelatedSeries()/*-{ return this.NumberOfStudyRelatedSeries; }-*/;
    public final native String getPatientSex()/*-{ return this.PatientSex; }-*/;
    public final native String getPatientName()/*-{ return this.PatientName; }-*/;
    public final native String getDicomServerURI()/*-{ return this.DicomServerURI; }-*/;
    public final native String getNumberOfStudyRelatedInstances()/*-{ return this.NumberOfStudyRelatedInstances; }-*/;
    public final native String getStudyID()/*-{ return this.StudyID; }-*/;
    public final native String getStudyDescription()/*-{ return this.StudyDescription; }-*/;
    public final native String getSeriesInstanceUID()/*-{ return this.SeriesInstanceUID; }-*/;
    public final native String getPatientID()/*-{ return this.PatientID; }-*/;

}
