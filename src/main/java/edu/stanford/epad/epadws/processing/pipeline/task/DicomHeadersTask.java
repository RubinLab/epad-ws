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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * @author amsnyder
 */
public class DicomHeadersTask implements Runnable
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private final String seriesUID;
	private final File dicomInputFile;
	private final File outputFile;

	public DicomHeadersTask(String seriesUID, File dicomInputFile, File outputFile)
	{
		this.seriesUID = seriesUID;
		this.dicomInputFile = dicomInputFile;
		this.outputFile = outputFile;
	}

	@Override
	public void run()
	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
		FileWriter tagFileWriter = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		Process process = null;

		try {
			String[] command = { "./dcm2txt", "-w", "250", "-l", "250", dicomInputFile.getAbsolutePath() };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomBinDir = EPADConfig.getEPADWebServerDICOMBinDir();

			processBuilder.directory(new File(dicomBinDir));
			process = processBuilder.start();
			process.getOutputStream();

			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			StringBuilder log = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.append("./dcm2txt: " + line).append("\n");
			}

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				logger.info(log.toString());
				logger.warning("Couldn't get tags for series " + seriesUID + "; dicom=" + dicomInputFile.getAbsolutePath() + " tagFile:"  + outputFile.getAbsolutePath(), e);
			}

			EPADFileUtils.createDirsAndFile(outputFile);
			File tagFile = outputFile;
			tagFileWriter = new FileWriter(tagFile);
			tagFileWriter.write(sb.toString());
		} catch (Exception e) {
			logger.warning("DicomHeadersTask failed to create DICOM tags for series " + seriesUID + " dicom FIle:" + dicomInputFile.getAbsolutePath() + " : " + outputFile.getAbsolutePath(), e);
		} catch (OutOfMemoryError oome) {
			logger.warning("DicomHeadersTask for series " + seriesUID + " out of memory: ", oome);
		} finally {
			IOUtils.closeQuietly(tagFileWriter);
			IOUtils.closeQuietly(br);

			if (process != null)
				process.destroy();
		}
	}
}
