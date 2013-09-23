package edu.stanford.isis.epadws.processing.mysql;

/**
 * SQL queries for MySQL DCM4CHEE tables.
 * 
 * @author amsnyder
 */
public class MySqlCalls
{

	private MySqlCalls()
	{
	}

	public static final String DELETE_SERIES_FROM_SERIES_STATUS = "delete from epaddb.series_status where series_iuid=?";
	public static final String DELETE_FROM_EPAD_FILES = "delete from epaddb.epad_files where file_path like ?";

	public static final String SELECT_INSTANCE_FOR_SERIES = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by i.inst_no";
	public static final String SELECT_INSTANCE_FOR_SERIES_ORDER_BY_INT = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by cast(i.inst_no as signed)";

	// public static final String SELECT_FILES_FOR_SERIES =
	// "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.series_iuid=?";
	public static final String SELECT_FILES_FOR_SERIES = "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size, st.study_iuid from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s, pacsdb.study as st WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.study_fk=st.pk and s.series_iuid=?";
	public static final String SELECT_FILES_FOR_SERIES_ORDERED = "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size, st.study_iuid from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s, pacsdb.study as st WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.study_fk=st.pk and s.series_iuid=?  order by cast(i.inst_no as signed)";

	// public static final String SELECT_SERIES_FOR_STUDY =
	// "SELECT s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where st.study_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_SERIES_FOR_STUDY = "SELECT s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where st.study_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";

	public static final String SELECT_STUDY_BY_STATUS = "SELECT * from pacsdb.study where study_status=?";
	public static final String SELECT_SERIES_BY_STATUS = "SELECT * from pacsdb.series where series_status=?";

	public static final String SELECT_SERIES_BY_ID = "SELECT * from pacsdb.series where series_iuid=?";

	public static final String SELECT_ALL_USERS = "SELECT user_name from epaddb.users";

	public static final String SELECT_STUDY_FK_ON_FS_TABLE = "SELECT study_fk from pacsdb.study_on_fs";
	public static final String UPDATE_STUDY_STATUS_CODE = "UPDATE pacsdb.study SET study_status=? WHERE study_iuid=?";
	public static final String UPDATE_SERIES_STATUS_CODE = "UPDATE pacsdb.series SET series_status=? WHERE series_iuid=?";
	public static final String UPDATE_MARK_STUDY_FOR_DELETION = "UPDATE pacsdb.study_on_fs SET mark_to_delete='true' where study_fk=?";

	public static final String SELECT_PATIENT_FOR_STUDY = "SELECT * from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and st.study_iuid=?";
	public static final String SELECT_PARENT_STUDY_FOR_SERIES = "SELECT * from pacsdb.study as st, pacsdb.series as s WHERE st.pk=s.study_fk and s.series_iuid=?";
	public static final String SELECT_PARENT_SERIES_FOR_IMAGE = "SELECT * from pacsdb.series as s, pacsdb.instance as i WHERE s.pk=i.series_fk and i.sop_iuid=?";

	public static final String SELECT_USER_PK = "SELECT u.pk from epaddb.users as u WHERE user_name=?";

	public static final String SELECT_EPAD_FILES_FOR_SERIES = "select i.sop_iuid from epaddb.epad_files as f, pacsdb.instance as i, pacsdb.series as s where series_iuid=? and i.series_fk=s.pk and f.instance_fk=i.pk";

	public static final String INSERT_INTO_EPAD_FILES = "INSERT INTO epaddb.epad_files"
			+ "(instance_fk,file_type,file_path,file_size,file_status,err_msg,file_md5)" + "VALUES (?,?,?,?,?,?,?)";

	public static final String INSERT_INTO_USER = "INSERT INTO epaddb.users"
			+ "(user_name,user_email,user_passwd,user_expiration,user_role)" + "VALUES (?,?,?,?,?)";

	public static final String INSERT_INTO_EVENT = "INSERT INTO epaddb.events"
			+ "(user_fk,event_status,aim_uid,aim_name,patient_id,patient_name,template_id,template_name,plugin_name)"
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	public static final String SELECT_EVENTS_FOR_USER = "SELECT * from epaddb.events where user_fk=?";

	public static final String SELECT_EPAD_FILES_FOR_PATH_LIKE = "SELECT * from epaddb.epad_files where file_path LIKE ?";
	public static final String SELECT_EPAD_FILES_FOR_EXACT_PATH = "SELECT * from epaddb.epad_files where file_path=?";

	public static final String UPDATE_EPAD_FILES_FOR_PATH_LIKE = "UPDATE epaddb.epad_files SET file_status=?, file_size=?, err_msg=? where file_path like ?";
	public static final String UPDATE_EPAD_FILES_FOR_EXACT_PATH = "UPDATE epaddb.epad_files SET file_status=?, file_size=?, err_msg=? where file_path=?";

	public static final String INSERT_INTO_EPAD_SERIES_STATUS = "INSERT INTO epaddb.series_status(series_iuid,status) VALUES (?,?)";
	public static final String UPDATE_EPAD_SERIES_STATUS = "UPDATE epaddb.series_status SET status=? where series_iuid=?";
	public static final String SELECT_EPAD_SERIES_BY_STATUS = "SELECT * from epaddb.series_status where status=?";
	public static final String SELECT_EPAD_SERIES_BY_ID = "SELECT * from epaddb.series_status where series_iuid=?";

	public static final String SELECT_ORDER_QUERY = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=?";

	public static final String SELECT_PATH = "select file_path from epaddb.epad_files as f where f.file_path LIKE ? LIMIT 1";

	public static final String SELECT_ALL_PATHS = "select file_path from epaddb.epad_files";

	public static final String PK_FOR_STUDY = "SELECT pk from pacsdb.study where study_iuid=?";
	public static final String PK_FOR_SERIES = "SELECT pk from pacsdb.series where series_iuid=?";
	public static final String PK_FOR_INSTANCE = "SELECT pk from pacsdb.instance where sop_iuid=?";
	public static final String SELECT_INSTANCE_FOR_SOP_UID = "SELECT * from pacsdb.instance where sop_iuid=?";

	public static final String SELECT_PATIENT_ATTRS = "select pat_attrs from pacsdb.patient where pat_id=?";
	public static final String SELECT_STUDY_ATTRS = "select study_attrs from pacsdb.study where study_iuid=?";
	public static final String SELECT_SERIES_ATTRS = "select series_attrs from pacsdb.series where series_iuid=?";
	public static final String SELECT_INSTANCE_ATTRS = "select inst_attrs from pacsdb.instance where sop_iuid=?";

	public static final String SELECT_COORDINATION_USING_KEY = "select coordination_id, schema_name, schema_version, description "
			+ "from epaddb.coordinations as c where c.coordination_key = ?";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String INSERT_COORDINATION = "insert into epaddb.coordinations values(NULL, ?, ?, ?, ?) ";

	// coordination_id, schema, schema_version, description; column 0 is auto-increment coordination_key
	public static final String UPDATE_COORDINATION = "update epaddb.coordinations set coordination_id = ? where coordination_key = ?";

	// coordination_key, term_key, position
	public static final String INSERT_COORDINATION2TERM = "insert into epaddb.coordination2term values(?, ?, ?) ";

	// term_id, schema, schema_version, description; column 0 is auto-increment key
	public static final String INSERT_TERM = "insert into epaddb.terms values(NULL, ?, ?, ?, ?)";

	public static final String SELECT_TERM_KEY = "select term_key from epaddb.terms as t where t.term_id = ? and t.schema_name = ? and t.schema_version = ?";

}
