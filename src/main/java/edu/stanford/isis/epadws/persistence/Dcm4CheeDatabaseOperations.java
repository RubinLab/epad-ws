package edu.stanford.isis.epadws.persistence;

import java.util.List;
import java.util.Map;

/**
 * Defines all operations on the Dcm4Chee database used by ePAD.
 * 
 * 
 * @author martin
 */
public interface Dcm4CheeDatabaseOperations
{
	/**
	 * Get all the studies with a study status of zero.
	 * 
	 * @return a list of studyUIDs.
	 */
	List<String> getNewSeries();

	List<Map<String, String>> studySearch(String type, String searchString);

	List<Map<String, String>> findAllSeriesInStudy(String studyUID);

	Map<String, String> getPatientForStudy(String studyIUID);

	List<String> getStudyIDsForPatient(String patientID);

	Map<String, String> getParentStudyForSeries(String seriesIUID);

	String getStudyUIDForSeries(String seriesUID);

	List<Map<String, String>> getDICOMImageFileDescriptionsForSeries(String seriesIUID);

	List<Map<String, String>> getDicomSeriesOrder(String seriesUID);

	int getInstanceKeyForInstance(String sopInstanceUID);
}
