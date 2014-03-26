package edu.stanford.epad.epadws.dcm4chee;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.epadws.queries.Dcm4CheeQueries;

/**
 * Defines all operations on the Dcm4Chee database used by ePAD.
 * 
 * 
 * @author martin
 * @see Dcm4CheeQueries
 */
public interface Dcm4CheeDatabaseOperations
{
	/**
	 * Get all the studies with a study status of zero.
	 * 
	 * @return a list of studyUIDs.
	 */
	Set<String> getNewDcm4CheeSeriesUIDs();

	/**
	 * pk, study_fk, mpps_fk, inst_code_fk, series_iuid, series_no, modality, body_part, laterality, series_desc,
	 * institution, station_name, department, perf_physician, perf_phys_fn_sx, perf_phys_gn_sx perf_phys_i_name,
	 * perf_phys_p_name, pps_start, series_custom1, series_custom2, series_custom3, num_instances, src_aet, ext_retr_aet,
	 * retrieve_aets, fileset_iuid, fileset_id, availability, series_status, created_time, updated_time, series_attrs
	 */
	Map<String, String> getDcm4CheeSeriesDataWithUID(String seriesIUID);

	// typeValue one of: patientName, patientId", studyDate, accessionNum, examType
	List<Map<String, String>> dicomStudySearch(String searchType, String typeValue);

	List<Map<String, String>> findAllDicomSeriesInStudy(String studyUID);

	Set<String> findAllSeriesUIDsInStudy(String studyUID);

	Map<String, String> getPatientForDicomStudy(String studyIUID);

	List<String> getDicomStudyUIDsForPatient(String patientID);

	Map<String, String> getParentStudyForDicomSeries(String seriesIUID);

	String getStudyUIDForSeries(String seriesUID);

	List<Map<String, String>> getDICOMImageFileDescriptionsForSeries(String seriesIUID);

	List<Map<String, String>> getDicomSeriesOrder(String seriesUID);

	int getInstanceKeyForInstance(String sopInstanceUID);
}
