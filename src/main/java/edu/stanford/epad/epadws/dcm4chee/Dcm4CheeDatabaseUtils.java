package edu.stanford.epad.epadws.dcm4chee;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.epad.common.util.FileKey;
import edu.stanford.epad.dtos.SeriesProcessingStatus;

public class Dcm4CheeDatabaseUtils
{
	/**
	 * Given a file generate the data to update the ePAD database table that contains information about that file.
	 * 
	 * @param file File
	 * @return Map of String to String. The key is the database column name.
	 */
	public static Map<String, String> createEPadFilesTableData(File file, String imageUID)
	{
		FileKey fileKey = new FileKey(file);
		String filePath = fileKey.toString();
		long fileSize = file.length();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		int instanceKey = dcm4CheeDatabaseOperations.getPrimaryKeyForImageUID(imageUID);

		Map<String, String> fileTableData = new HashMap<String, String>();
		fileTableData.put("instance_fk", "" + instanceKey);
		fileTableData.put("file_type", "" + getFileTypeFromName(filePath));
		fileTableData.put("file_path", filePath);
		fileTableData.put("file_size", "" + fileSize);
		fileTableData.put("file_md5", "n/a");
		fileTableData.put("file_status", "" + SeriesProcessingStatus.DONE.getCode());
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
