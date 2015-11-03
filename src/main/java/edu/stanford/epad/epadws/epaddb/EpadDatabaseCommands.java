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
package edu.stanford.epad.epadws.epaddb;

/**
 * This interface defines SQL commands to work with ePAD's MySQL database.
 * 
 * 
 * @author martin
 */
public interface EpadDatabaseCommands
{
	public static final String DELETE_ALL_FROM_SERIES_STATUS = "delete from epaddb.series_status";

	public static final String DELETE_SERIES_FROM_SERIES_STATUS = "delete from epaddb.series_status where series_iuid=?";

	public static final String SELECT_EPAD_IMAGE_UIDS_FOR_SERIES = "select i.sop_iuid from epaddb.epad_files as f, pacsdb.instance as i, pacsdb.series as s where series_iuid=? and i.series_fk=s.pk and f.instance_fk=i.pk";
	public static final String INSERT_INTO_EVENT = "INSERT INTO epaddb.events"
			+ "(username,event_status,aim_uid,aim_name,patient_id,patient_name,template_id,template_name,plugin_name,project_id,project_name,series_uid,study_uid,error)"
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String SELECT_EVENTS_FOR_SESSIONID = "SELECT * from epaddb.events where username=? ORDER BY pk";
	public static final String DELETE_EVENTS_FOR_SESSIONID = "DELETE from epaddb.events where username=? and pk <= ?";
	public static final String SELECT_EVENTS_FOR_AIMID = "SELECT * from epaddb.events where aim_uid=? ORDER BY pk desc";
	public static final String DELETE_OLD_EVENTS = "DELETE from epaddb.events where created_time < ?";
	
	public static final String INSERT_INTO_EPAD_SERIES_STATUS = "INSERT INTO epaddb.series_status(series_iuid,status) VALUES (?,?)";
	public static final String UPDATE_EPAD_SERIES_STATUS = "UPDATE epaddb.series_status SET status=? where series_iuid=?";
	public static final String UPDATE_EPAD_SERIES_TAGS = "UPDATE epaddb.series_status SET default_tags=? where series_iuid=?";
	public static final String GET_EPAD_SERIES_TAGS = "SELECT default_tags from epaddb.series_status where series_iuid=?";
	public static final String SELECT_EPAD_SERIES_BY_ID = "SELECT * from epaddb.series_status where series_iuid=?";
	public static final String SELECT_DCM4CHE_STUDY_BY_ID = "SELECT study_iuid,study_desc,study_datetime,accession_no from pacsdb.study where study_iuid=?";
	public static final String SELECT_STATUS_FOR_SERIES_BY_ID = "SELECT status from epaddb.series_status where series_iuid=?";
	public static final String SELECT_STATUS_AND_CREATED_TIME_FOR_SERIES_BY_ID = "SELECT status,created_time from epaddb.series_status where series_iuid=?";

	public static final String SELECT_ALL_EPAD_FILE_PATHS_WITH_STATUS = "select file_path from epaddb.epad_files where file_status = ?";
	public static final String SELECT_EPAD_FILES_FOR_EXACT_PATH = "SELECT * from epaddb.epad_files where file_path=?";
	public static final String UPDATE_EPAD_FILES_FOR_EXACT_PATH = "UPDATE epaddb.epad_files SET file_status=?, file_size=?, err_msg=? where file_path=?";
	public static final String DELETE_ALL_FROM_EPAD_FILES = "delete from epaddb.epad_files";
	public static final String DELETE_FROM_EPAD_FILES = "delete from epaddb.epad_files where file_path like ?";
	public static final String INSERT_INTO_EPAD_FILES = "INSERT INTO epaddb.epad_files"
			+ "(instance_fk,file_type,file_path,file_size,file_status,err_msg,file_md5)" + "VALUES (?,?,?,?,?,?,?)";
	public static final String SELECT_EPAD_FILE_PATH_FOR_IMAGE = "SELECT file_path from epaddb.epad_files as f, pacsdb.instance as i where i.sop_iuid=? and i.pk = f.instance_fk";
	public static final String SELECT_EPAD_FILE_PATH_BY_IMAGE_UID = "SELECT file_path from epaddb.epad_files as f where file_path like ?";

	public static final String SELECT_COORDINATION_USING_KEY = "select coordination_id, schema_name, schema_version, description "
			+ "from epaddb.coordinations as c where c.coordination_key = ?";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String INSERT_COORDINATION = "insert into epaddb.coordinations values(NULL, ?, ?, ?, ?) ";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String UPDATE_COORDINATION = "update epaddb.coordinations set coordination_id = ? where coordination_key = ?";

	// coordination_key, term_key, position
	public static final String INSERT_COORDINATION2TERM = "insert into epaddb.coordination2term values(?, ?, ?) ";

	// term_id, schema, schema_version, description; column 0 is auto-increment key
	public static final String INSERT_COORDINATION_TERM = "insert into epaddb.terms values(NULL, ?, ?, ?, ?)";

	public static final String SELECT_COORDINATION_TERM_KEY = "select term_key from epaddb.terms as t where t.term_id = ? and t.schema_name = ? and t.schema_version = ?";

	public static final String SELECT_COORDINATION_BY_ID = "select coordination_id,t.term_id,t.schema_name,t.description from terms t,coordination2term c2,coordinations c where c.coordination_key=c2.coordination_key and t.term_key=c2.term_key and coordination_id like ? order by coordination_id,position";
	
	public static final String CLEANUP_OBSOLETE_EPAD_FILES = "delete from epad_files where instance_fk not in (select pk from pacsdb.instance)";

	public static final String SELECT_DISTINCT_EPADS = "select distinct host from epaddb.epadstatistics";
}
