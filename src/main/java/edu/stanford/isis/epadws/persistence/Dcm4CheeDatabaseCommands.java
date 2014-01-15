package edu.stanford.isis.epadws.persistence;

/**
 * This interface defines SQL commands to work with DCM4CHEE's MySQL database.
 * 
 * 
 * @author martin
 */
public interface Dcm4CheeDatabaseCommands
{
	public static final String SELECT_FILES_FOR_SERIES = "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size, st.study_iuid from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s, pacsdb.study as st WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.study_fk=st.pk and s.series_iuid=?";
	public static final String SELECT_INSTANCE_FOR_SERIES = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by i.inst_no";
	public static final String SELECT_INSTANCE_FOR_SERIES_ORDER_BY_INT = "SELECT * from pacsdb.instance as i, pacsdb.series as s where i.series_fk=s.pk and s.series_iuid=? order by cast(i.inst_no as signed)";
	public static final String SELECT_SERIES_FOR_STUDY = "SELECT s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances, s.series_status, s.body_part, s.institution, s.station_name, s.department from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where st.study_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";
	public static final String SELECT_SERIES_BY_STATUS = "SELECT * from pacsdb.series where series_status=?";
	public static final String SELECT_SERIES_BY_ID = "SELECT * from pacsdb.series where series_iuid=?";
	public static final String SELECT_STUDY_FOR_PATIENT = "SELECT * from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and patient.pat_id=?";
	public static final String SELECT_PATIENT_FOR_STUDY = "SELECT * from pacsdb.patient as p, pacsdb.study as st WHERE p.pk=st.patient_fk and st.study_iuid=?";
	public static final String SELECT_PARENT_STUDY_FOR_SERIES = "SELECT * from pacsdb.study as st, pacsdb.series as s WHERE st.pk=s.study_fk and s.series_iuid=?";
	public static final String PK_FOR_INSTANCE = "SELECT pk from pacsdb.instance where sop_iuid=?";
	public static final String SELECT_PATIENT_ATTRS = "select pat_attrs from pacsdb.patient where pat_id=?";
	public static final String SELECT_STUDY_ATTRS = "select study_attrs from pacsdb.study where study_iuid=?";
	public static final String SELECT_SERIES_ATTRS = "select series_attrs from pacsdb.series where series_iuid=?";

	// public static final String SELECT_FILES_FOR_SERIES =
	// "SELECT i.sop_iuid, i.inst_no, s.series_iuid, f.filepath, f.file_size from pacsdb.files as f, pacsdb.instance as i, pacsdb.series as s WHERE f.instance_fk=i.pk and i.series_fk=s.pk and s.series_iuid=?";
	// public static final String SELECT_SERIES_FOR_STUDY =
	// "SELECT s.series_iuid, p.pat_id, p.pat_name, st.study_datetime, s.modality, s.series_desc, s.num_instances from pacsdb.series as s, pacsdb.study as st, pacsdb.patient as p where st.study_iuid=? and s.study_fk=st.pk and st.patient_fk=p.pk";
}
