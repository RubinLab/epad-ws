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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import edu.stanford.epad.common.dicom.DicomFileUtil;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.model.DicomUploadPipelineFiles;

public class UnzipTask implements Callable<List<File>>
{
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final DicomUploadPipelineFiles pipeline = DicomUploadPipelineFiles.getInstance();

	private File file;

	public UnzipTask(File file)
	{
		this.file = file;
	}

	@Override
	public List<File> call() throws Exception
	{
		List<File> retVal = new ArrayList<File>();
		try {
			log.info("Checking file extension: " + file.getName());
			// DICOM files might not have extensions. Add it if missing.
			if (!EPADFileUtils.hasExtension(file)) {
				if (DicomFileUtil.hasMagicWordInHeader(file)) {
					file = DicomFileUtil.addDcmExtensionToFile(file);
				}
			}
			String extension = EPADFileUtils.getExtension(file);

			if (extension.equalsIgnoreCase("dcm")) {
				retVal.add(file);
				return retVal;
			} else if (extension.equalsIgnoreCase("zip")) {
				EPADFileUtils.extractFolder(file.getAbsolutePath());
				// The new files will be discovered by the upload directory watcher, so don't return anything.
				return retVal;
			} else if (extension.equalsIgnoreCase("gz")) {
				EPADFileUtils.extractFolder(file.getAbsolutePath());
				// The new files will be discovered by the upload directory watcher, so don't return anything.
				return retVal;
			} else if (extension.equalsIgnoreCase("tag")) {
				// ignore new tag files they will get moved with dcm files.
				return retVal;
			} else {
				log.info("Ignoring file with unknown extension: " + file.getAbsolutePath());
				// retVal.add(file);
				return retVal;
			}
		} catch (Exception e) {
			pipeline.addErrorFile(file, "UnzipTask error.", e);
			return retVal;
		}
	}
	// ToDo: move to file extensions. and name with lower case.
}
