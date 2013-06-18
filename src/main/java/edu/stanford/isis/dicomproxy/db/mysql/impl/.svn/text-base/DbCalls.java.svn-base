/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */

package edu.stanford.isis.dicomproxy.db.mysql.impl;

/**
 * Central place to keep all database calls.
 */
public class DbCalls {

    private DbCalls(){}

    /**
     * Insert type calls below.
     */

    public static final String PS_INSERT_ISSUER = "INSERT INTO issuer(" +
            "entity_id, entity_uid, entity_uid_type) " +
            "VALUES(?,?,?)";

    public static final String PS_INSERT_PATIENT = "INSERT INTO patient(" +
            "merge_fk, pat_id, pat_id_issuer, pat_name, pat_birthdate, pat_sex, created_time, updated_time)" +
            " VALUES(?,?,?,?,?,?,?,?)";

    public static final String PS_INSERT_STUDY = "INSERT INTO study(" +
            "patient_fk ,accno_issuer_fk ,study_uid ,study_id ,study_datetime ,accession_no ," +
            "ref_physician ,study_desc ,created_time) " +
            "VALUES(?,?,?,?,?,?,?,?,?)";

    public static final String PS_INSERT_SERIES = "INSERT INTO series(" +
            "study_fk ,series_iuid ,series_no ,modality ,body_part ,laterality ," +
            "series_desc ,institution ,station_name ,department ,perf_physicisan ,num_instances,thumbnail_url) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    //ToDo: include a "thumbnail_url" in the PS_INSERT_SERIES.

    public static final String PS_INSERT_INSTANCE = "INSERT INTO instance(" +
            "series_fk ,sop_iuid ,sop_cuid ,ints_no ,content_time) " +
            "VALUES(?,?,?,?,?)";

    public static final String PS_INSERT_FILES = "INSERT INTO files(" +
            "instance_fk ,filesystem_fk ,filepath ,file_md5 ,md5_check_time ," +
            "file_size ,created_time ,mark_to_delete ,file_type) " +
            "VALUES(?,?,?,?,?,?,?,?,?)";

    public static final String PS_INSERT_FILESYSTEM = "INSERT INTO filesystem(dirpath) VALUES(?)";

    public static final String PS_INSERT_STUDY_ON_FS = "INSERT INTO study_on_fs(" +
            "study_fk ,filesystem_fk ,access_time ,mark_to_delete) " +
            "VALUES(?,?,?,?)";

    public static final String PS_INSERT_ANNOTATIONS = "INSERT INTO annotations(" +
            "instance_fk ,aim_uid) " +
            "VALUES(?,?)";


    /**
     * select statements to get keys.
     */
    public static final String PK_FOR_ISSUER  = "SELECT pk from issuer where entity_uid=?";
    public static final String PK_FOR_PATIENT = "SELECT pk from patient where pat_id=?";
    public static final String PK_FOR_STUDY   = "SELECT pk from study where study_uid=?";
    public static final String PK_FOR_SERIES  = "SELECT pk from series where series_iuid=?";
    public static final String PK_FOR_INSTANCE = "SELECT pk from instance where sop_iuid=?";
    public static final String PK_FOR_FILE = "SELECT pk from files where filepath=?";

    /**
     * select methods.
     */
    public static final String SELECT_INSTANCE_FOR_SERIES = "SELECT * from instance where instance.series_fk=series.pk"+
            " and series_iuid=? order by ints_no";

    public static final String SELECT_SERIES_FOR_STUDY = "SELECT s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.thumbnail_url, s.series_desc, s.num_instances from series as s, study as st, patient as p where st.study_uid=? and s.study_fk=st.pk and st.patient_fk=p.pk";

    public static final String SELECT_STUDY_FK_ON_FS_TABLE = "SELECT study_fk from study_on_fs";


    public static final String UPDATE_MARK_STUDY_FOR_DELETION = "UPDATE study_on_fs SET mark_to_delete='true' where study_fk=?";
    
    public static final String UPDATE_NUM_INSTANCES_IN_SERIES = "UPDATE series SET num_instances=? WHERE series_iuid=?";
    
    public static final String INCREMENT_NUM_INSTANCES_IN_SERIES = "UPDATE series SET num_instances=num_instances+1 WHERE series_iuid=?";


    /**
     * Work List related queries.
     */

    /** create **/
    public static final String PS_INSERT_WORKLIST = "INSERT INTO worklist(worklist_name_fk, entity_id) VALUES(?,?)";
    public static final String PS_INSERT_WORKLIST_NAME = "INSERT INTO worklist_name(worklist_name, worklist_type, worklist_desc, created_time) VALUES(?,?,?,?)";

    /** read **/
    public static final String SELECT_ALL_WORK_LISTS = "SELECT worklist_name from worklist_name order by created_time";
    public static final String PK_FOR_WORKLIST_NAME = "SELECT pk from worklist_name where worklist_name=?";
    public static final String SELECT_ITEMS_FOR_WORK_LIST = "SELECT wl.entity_id from worklist as wl, worklist_name as wln where wln.worklist_name=? and wln.pk=wl.worklist_name_fk";
    public static final String SELECT_TYPE_FOR_WORK_LIST = "SELECT worklist_type, worklist_desc, created_time from worklist_name where worklist_name=?";

    /** update **/
    public static final String PS_DELETE_WORKLIST = "DELETE FROM worklist where worklist_name_fk=? and entity_id=?";

    /** delete **/
    public static final String PS_DELETE_WORKLIST_NAME = "DELETE FROM worklist_name where worklist_name=?";




//    public static final String NAME = "INSERT INTO <table>() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
//    public static final String NAME = "INSERT INTO <table>() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
//    public static final String NAME = "INSERT INTO <table>() VALUES(?,?,?,?,?,?,?,?,?,?,?)";
//    public static final String NAME = "INSERT INTO <table>() VALUES(?,?,?,?,?,?,?,?,?,?,?)";

//    public static final String NAME = "SELECT pk from <table1> where <value>=?";
//    public static final String NAME = "SELECT pk from <table1> where <value>=?";

}
