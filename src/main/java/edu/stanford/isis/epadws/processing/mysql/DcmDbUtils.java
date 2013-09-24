package edu.stanford.isis.epadws.processing.mysql;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ProxyFileUtils;
import edu.stanford.isis.epadws.processing.model.PngStatus;

/**
 * Move utility classes to here.
 * 
 * @author alansnyder
 */
public class DcmDbUtils
{
	private DcmDbUtils()
	{
	}

	public static Map<String, String> addErrorMsg(Map<String, String> map, PngStatus pngStatus, String errMsg)
	{
		map.put("file_status", "" + pngStatus.getCode());
		map.put("err_msg", errMsg);
		return map;
	}

	/**
	 * Given a file generate the data to update the ePAD database table that contains information about that file.
	 * 
	 * @param outputFile File
	 * @return Map of String to String. The key is the database column name.
	 */
	public static Map<String, String> createEPadFilesTableData(File outputFile)
	{
		FileKey fileKey = new FileKey(outputFile);
		String filePath = fileKey.toString();
		long fileSize = outputFile.length();
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String sopInstanceUID = getSOPInstanceUIDFromPath(filePath);
		int instanceKey = queries.getInstanceKey(sopInstanceUID);

		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("instance_fk", "" + instanceKey);
		retVal.put("file_type", "" + getFileTypeFromName(filePath));
		retVal.put("file_path", filePath);
		retVal.put("file_size", "" + fileSize);
		retVal.put("file_md5", "n/a");
		retVal.put("file_status", "" + PngStatus.DONE.getCode());
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
			return DcmDbUtils.FILE_TYPE_PNG;
		} else if (name.endsWith(".tag")) {
			return DcmDbUtils.FILE_TYPE_TAG;
		}
		return DcmDbUtils.FILE_TYPE_UNKNOWN;
	}

	/**
	 * @param path Expect study/series/instance directory structure.
	 * @return Just the instance with dots notation.
	 */
	public static String getSOPInstanceUIDFromPath(String path)
	{
		String p = ProxyFileUtils.fileAbsolutePathWithoutExtension(new File(path));

		p = p.replace('/', ',');
		String[] parts = p.split(",");
		int num = parts.length;

		String[] parts2 = parts[num - 1].split("-");
		return DicomFormatUtil.formatDirToUid(parts2[0]);
	}
}
