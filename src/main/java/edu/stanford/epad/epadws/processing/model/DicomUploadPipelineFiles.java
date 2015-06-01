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
package edu.stanford.epad.epadws.processing.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.FileKey;

/**
 * This class keeps track of all the files that have caused an error in the pipeline.
 * 
 */
public class DicomUploadPipelineFiles
{
	private static final EPADLogger log = EPADLogger.getInstance();

	/**
	 * Keeps track of files that have been put in the pipeline.
	 */
	private final Map<FileKey, DicomUploadFile> inPipelineMap = new HashMap<FileKey, DicomUploadFile>();

	/**
	 * Keeps track of files that had an error in the pipeline, so they are not run again.
	 */
	private final Map<FileKey, DicomUploadFileErrorDescription> uploadErrorFiles = new ConcurrentHashMap<FileKey, DicomUploadFileErrorDescription>();

	private static DicomUploadPipelineFiles ourInstance = new DicomUploadPipelineFiles();

	public static DicomUploadPipelineFiles getInstance()
	{
		return ourInstance;
	}

	private DicomUploadPipelineFiles()
	{
	}

	/**
	 * Add a file to the pipeline.
	 * 
	 * @param currFileKey - FileKey
	 */
	public void addToPipeline(FileKey currFileKey)
	{
		inPipelineMap.put(currFileKey, new DicomUploadFile(currFileKey.getFile()));
	}

	/**
	 * Return a list of all the error files.
	 * 
	 * @return List
	 */
	public List<DicomUploadFileErrorDescription> getErrorFiles()
	{
		return new ArrayList<DicomUploadFileErrorDescription>(uploadErrorFiles.values());
	}

	/**
	 * This method is called if an error occurs somewhere in the pipeline and
	 * 
	 * @param file - File file that had error
	 * @param errorMessage - String the error message
	 * @param e - Exception
	 */
	public void addErrorFile(File file, String errorMessage, Exception e)
	{

		String expMessage = e.getMessage();
		log.info("Adding error file: " + file.getAbsolutePath() + " for: " + errorMessage + " , " + expMessage);

		FileKey uploadFileKey = new FileKey(file);
		uploadErrorFiles.put(uploadFileKey, new DicomUploadFileErrorDescription(uploadFileKey, errorMessage, e));
		inPipelineMap.remove(uploadFileKey);
	}

	/**
	 * True if this is a known error file.
	 * 
	 * @param fileKey FileKey
	 * @return boolean - true if this is file had an error.
	 */
	public boolean isKnownErrorFile(FileKey fileKey)
	{
		return uploadErrorFiles.containsKey(fileKey);
	}

	/**
	 * Return true if this file is in the pipeline.
	 * 
	 * @param fileKey FileKey
	 * @return boolean true if this file in in the pipeline.
	 */
	public boolean isInPipeline(FileKey fileKey)
	{
		return inPipelineMap.containsKey(fileKey);
	}

}
