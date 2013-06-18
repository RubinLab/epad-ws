/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.db.mysql.impl;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The are the public calls made to the database. These method are to hide the details of those calls.
 */
public interface DbQueries {

    //ToDo: add a call to make a "order file" for a series, based on the files and instance tables.

    Set<String> getKnownStudiesInLocalStorage();

    Set<String> getSeriesForStudy(String study);

    List<String> getInstancesForSeries(String series);

    boolean markStudyForDeletion(String studyId);
    
    boolean updateNumInstancesForSeries(String seriesUID, String numInstances);
    
    boolean incrementNumInstancesForSeries(String seriesUID);

    /**
     * check study_on_fs table. If it is already there then just update the flag.
     * If it isn't there then add it to the to the database table.
     * @param studyId String
     */
    void addStudyToOnFileSystemTable(String studyId);

    List<Map<String,String>> doStudySearch(String type, String searchString);

    List<Map<String,String>> doSeriesSearch(String studyUID);

    /**
     * Methods to get the primary keys from a database.
     */
    int getIssuerKey(Map<IssuerKey,String> dbValues);
    int getPatientKey(Map<PatientKey,String> dbValues);
    int getStudyKey(String studyUID);
    int getSeriesKey(String seriesUID);
    int getInstanceKey(String sopInstanceUID);
    int getFileTableKey(String filepath);
    // have one for each table!!


    /**
     * A new Issuer must be created before a patient id can be created.
     * @param dbValues Map
     */
    void addIssuer(Map<IssuerKey, String> dbValues);

    /**
     * A new patient must be added before a study can be added.
     * @param dbValues Map
     */
    void addPatient(Map<PatientKey,String> dbValues);

    void addStudy(Map<StudyKey,String> dbValues);

    void addSeries(Map<SeriesKey,String> dbValues);

    void addInstance(Map<InstanceKey,String> dbValues);

    void addFilesTable(Map<FilesTableKey,String> dbValues);

    public enum IssuerKey{
        ENTITY_ID,
        ENTITY_UID,
        ENTITY_UID_TYPE
    }

    public enum PatientKey{
        ID,
        ID_ISSUER,
        NAME,
        BIRTHDATE,
        SEX,
        CREATED_TIME,
        UPDATED_TIME

    }

    public enum StudyKey{
        SUID,
        ID,
        DATETIME,
        ACCESSION_NO,
        REF_PHYSICIAN,
        STUDY_DESC,
        CREATED_TIME,
        FK_PATIENT,
        FK_ISSUER,

        //These optional values are used to get the patient_fk
        OPTIONAL_PATIENT_NAME,
        OPTIONAL_PATIENT_BDATE,
        OPTIONAL_PATIENT_ID
    }

    public enum SeriesKey{
        I_UID,
        NUMBER,
        MODALITY,
        BODY_PART,
        LATERALITY,
        DESC,
        INSTITUTION,
        STATION_NAME,
        DEPARTMENT,
        PHYSICIAN,
        NUM_INSTANCES,
        FK_STUDY, //used to access the study table.
        THUMBNAIL_URL
    }

    public enum InstanceKey{
        SERIES_FK,
        SOP_I_UID,
        SOP_C_UID,
        INSTANCE_NUM,
        CONTENT_TIME
    }

    public enum FilesTableKey{
        INSTANCE_FK,
        FILESYSTEM_FK,
        FILEPATH,
        FILE_MD5,
        MD5_CHECK_TIME,
        FILE_SIZE,
        CREATE_TIME,
        MARK_TO_DELETE,
        FILE_TYPE
    }

    //include some privately created ones.

    void addFilesystem( File filesystemRoot );


    /*************************************************************
     * Work List related queries
     *************************************************************/

    //CREATE work list
    boolean addWorkList(String workListName, String type, String description);

    //READ work list
    List<String> getWorkListNames();
    List<String> getWorkListItemsFor(String workListName);
    Map<String,String> getWorkListTypeFor(String workListName);
    int getWorkListNameKey(String workListName);

    //UPDATE work list
    boolean addItemToWorkListFor(String workListName, String itemName);
    boolean deleteItemInWorkListFor(String workListName, String itemName);

    //DELETE work list
    boolean deleteWorkList(String name);

    public enum workListNameKey{
        WORKLIST_NAME,
        WORKLIST_TYPE,
        WORKLIST_DESC,
        CREATED_TIME
    }

    public enum workListKey{
        WORKLIST_NAME_FK,
        ENTITY_ID
    }

}
