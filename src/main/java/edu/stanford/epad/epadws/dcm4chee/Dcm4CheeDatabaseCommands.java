//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.dcm4chee;

/**
 * This interface defines SQL commands to work with DCM4CHEE's MySQL database.
 * 
 * 
 * @author martin
 */
public interface Dcm4CheeDatabaseCommands
{
	public static final String SELECT_FILES_FOR_SERIES = "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.created_time, f.filepath, f.file_size, st.study_iuid, s.modality from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s, pacsdb.study as st WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.study_fk=st.pk and s.series_iuid=?";
	public static final String SELECT_IMAGE_UID_FOR_SERIES = "SELECT sop_iuid from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by i.inst_no";
	public static final String SELECT_IMAGE_FOR_SERIES = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? and i.sop_iuid=?";
	public static final String SELECT_IMAGES_FOR_SERIES_ORDER_BY_INSTNO = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by cast(i.inst_no as signed)";
	public static final String SELECT_SERIES_FOR_STUDY = "SELECT s.pk, st.study_iuid, s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.pps_start, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department, s.created_time, s.updated_time from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where st.study_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_SERIES_BY_STATUS = "SELECT st.study_iuid, s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department, s.created_time, s.updated_time from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where s.series_status=? and s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_SERIES = "SELECT st.study_iuid, s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department, s.created_time, s.updated_time from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_SERIES_BY_ID = "SELECT st.study_iuid, s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department, s.created_time, s.updated_time from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where s.series_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_STUDY_FOR_PATIENT = "SELECT * from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and p.pat_id=?";
	public static final String SELECT_COUNT_STUDY_FOR_PATIENT = "SELECT COUNT(*) from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and p.pat_id=?";
	public static final String SELECT_PATIENT_FOR_STUDY = "SELECT * from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and st.study_iuid=?";
	public static final String SELECT_PARENT_STUDY_FOR_SERIES = "SELECT * from pacsdb.study as st, pacsdb.series as s WHERE st.pk=s.study_fk and s.series_iuid=?";
	public static final String PK_FOR_INSTANCE = "SELECT pk from pacsdb.instance where sop_iuid=?";
	public static final String SELECT_STUDY_AND_SERIES_FOR_INSTANCE = "SELECT i.sop_iuid, s.series_iuid, st.study_iuid from pacsdb.instance as i, pacsdb.series as s, pacsdb.study as st WHERE i.sop_iuid=? and i.series_fk=s.pk and s.study_fk=st.pk";
}
