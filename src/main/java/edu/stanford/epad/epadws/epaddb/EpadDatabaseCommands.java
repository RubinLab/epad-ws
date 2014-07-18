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
			+ "(username,event_status,aim_uid,aim_name,patient_id,patient_name,template_id,template_name,plugin_name)"
			+ "VALUES (?,?,?,?,?,?,?,?,?)";
	public static final String SELECT_EVENTS_FOR_SESSIONID = "SELECT * from epaddb.events where username=? ORDER BY pk";
	public static final String DELETE_EVENTS_FOR_SESSIONID = "DELETE from epaddb.events where username=? and pk <= ?";

	public static final String INSERT_INTO_EPAD_SERIES_STATUS = "INSERT INTO epaddb.series_status(series_iuid,status) VALUES (?,?)";
	public static final String UPDATE_EPAD_SERIES_STATUS = "UPDATE epaddb.series_status SET status=? where series_iuid=?";
	public static final String SELECT_EPAD_SERIES_BY_ID = "SELECT * from epaddb.series_status where series_iuid=?";
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
}
