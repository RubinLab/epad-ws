package edu.stanford.epad.epadws.dcm4chee;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.epad.dtos.PNGFileProcessingStatus;

public class Dcm4CheeDatabaseUtils
{
	/**
	 * Given a file and an image UID, generate the data to update the ePAD database table that contains information about
	 * that file.
	 * 
	 * @param file File
	 * @return Map of String to String. The key is the database column name.
	 */
	public static Map<String, String> createEPadFilesRowData(String filePath, long fileSize, String imageUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		int instanceKey = dcm4CheeDatabaseOperations.getPrimaryKeyForImageUID(imageUID);

		Map<String, String> fileTableData = new HashMap<String, String>();
		fileTableData.put("instance_fk", "" + instanceKey);
		fileTableData.put("file_type", "" + getFileTypeFromName(filePath));
		fileTableData.put("file_path", filePath);
		fileTableData.put("file_size", "" + fileSize);
		fileTableData.put("file_md5", "n/a");
		fileTableData.put("file_status", "" + PNGFileProcessingStatus.IN_PIPELINE.getCode());
		fileTableData.put("err_msg", "");

		return fileTableData;
	}

	public static final int FILE_TYPE_UNKNOWN = 0;
	public static final int FILE_TYPE_PNG = 3;
	public static final int FILE_TYPE_TAG = 5;

	/**
	 * Get the file type from the name.
	 * 
	 * @param name String
	 * @return int
	 */
	public static int getFileTypeFromName(String name)
	{
		if (name.endsWith(".png")) {
			return Dcm4CheeDatabaseUtils.FILE_TYPE_PNG;
		} else if (name.endsWith(".tag")) {
			return Dcm4CheeDatabaseUtils.FILE_TYPE_TAG;
		}
		return Dcm4CheeDatabaseUtils.FILE_TYPE_UNKNOWN;
	}
}
