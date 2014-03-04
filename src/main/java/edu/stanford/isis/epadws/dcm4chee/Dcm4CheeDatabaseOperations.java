package edu.stanford.isis.epadws.dcm4chee;

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
	List<String> getNewDicomSeries();

	// typeValue one of: patientName, patientId", studyDate, accessionNum, examType
	List<Map<String, String>> dicomStudySearch(String searchType, String typeValue);

	List<Map<String, String>> findAllDicomSeriesInStudy(String studyUID);

	List<String> findAllSeriesUIDsInStudy(String studyUID);

	Map<String, String> getPatientForDicomStudy(String studyIUID);

	List<String> getDicomStudyUIDsForPatient(String patientID);

	Map<String, String> getParentStudyForDicomSeries(String seriesIUID);

	String getDicomStudyUIDForSeries(String seriesUID);

	List<Map<String, String>> getDICOMImageFileDescriptionsForSeries(String seriesIUID);

	List<Map<String, String>> getDicomSeriesOrder(String seriesUID);

	int getInstanceKeyForInstance(String sopInstanceUID);
}
