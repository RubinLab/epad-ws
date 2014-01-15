package edu.stanford.isis.epadws.persistence;

import java.util.List;
import java.util.Map;

/**
 * Defines all operations on ePAD's databases.
 * 
 * 
 * @author martin
 */
public interface DatabaseOperations extends EpadDatabaseOperations, Dcm4CheeDatabaseOperations
{
	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
	 */
	List<Map<String, String>> getUnprocessedDicomImageFileDescriptionsForSeries(String seriesIUID);
}
