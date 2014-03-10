package edu.stanford.isis.epadws.dcm4chee;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epadws.processing.model.PngProcessingStatus;

public class Dcm4CheeDatabaseUtils
{
	private Dcm4CheeDatabaseUtils()
	{
	}

	/**
	 * Given a file generate the data to update the ePAD database table that contains information about that file.
	 * 
	 * @param file File
	 * @return Map of String to String. The key is the database column name.
	 */
	public static Map<String, String> createEPadFilesTableData(File file)
	{
		FileKey fileKey = new FileKey(file);
		String filePath = fileKey.toString();
		long fileSize = file.length();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance().getDcm4CheeDatabaseOperations();
		String sopInstanceUID = getSOPInstanceUIDFromPath(filePath);
		int instanceKey = dcm4CheeDatabaseOperations.getInstanceKeyForInstance(sopInstanceUID);

		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("instance_fk", "" + instanceKey);
		retVal.put("file_type", "" + getFileTypeFromName(filePath));
		retVal.put("file_path", filePath);
		retVal.put("file_size", "" + fileSize);
		retVal.put("file_md5", "n/a");
		retVal.put("file_status", "" + PngProcessingStatus.DONE.getCode());
		retVal.put("err_msg", "");

		return retVal;
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

	/**
	 * @param path Expect study/series/instance directory structure.
	 * @return Just the instance with dots notation.
	 */
	public static String getSOPInstanceUIDFromPath(String path)
	{
		String p = EPADFileUtils.fileAbsolutePathWithoutExtension(new File(path));

		p = p.replace('/', ',');
		String[] parts = p.split(",");
		int num = parts.length;

		String[] parts2 = parts[num - 1].split("-");
		return DicomFormatUtil.formatDirToUid(parts2[0]);
	}
}
