//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
