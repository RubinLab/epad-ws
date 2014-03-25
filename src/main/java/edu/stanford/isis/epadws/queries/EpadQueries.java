package edu.stanford.isis.epadws.queries;

import java.util.List;
import java.util.Map;

import edu.stanford.epad.dtos.DCM4CHEESeries;
import edu.stanford.epad.dtos.EPADDatabaseSeries;

/**
 * 
 * @author martin
 */
public interface EpadQueries
{
	/**
	 * pk, study_fk, mpps_fk, inst_code_fk, series_iuid, series_no, modality, body_part, laterality, series_desc,
	 * institution, station_name, department, perf_physician, perf_phys_fn_sx, perf_phys_gn_sx, perf_phys_i_name,
	 * perf_phys_p_name, pps_start, series_custom1, series_custom2, series_custom3, num_instances, src_aet, ext_retr_aet,
	 * retrieve_aets, fileset_iuid, fileset_id, availability, series_status, created_time, updated_time, series_attrs
	 */
	List<DCM4CHEESeries> getNewDcm4CheeSeriesWithStatus(int statusCode);

	EPADDatabaseSeries peformEPADSeriesQuery(String seriesIUID);

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	 */
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID);
}
