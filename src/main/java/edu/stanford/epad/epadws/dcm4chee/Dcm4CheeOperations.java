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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

/**
 * Operations on Dcm4Chee (as distinct from operations directly on DCM4CHEE's database).
 * 
 * 
 * @author martin
 * @see Dcm4CheeDatabaseOperations
 */
public class Dcm4CheeOperations
{
	private static EPADLogger log = EPADLogger.getInstance();

	public static void dcmsnd(File inputDirFile, boolean throwException) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;

		try {
			String aeTitle = EPADConfig.aeTitle;
			String dicomServerIP = EPADConfig.dicomServerIP;
			String dicomServerPort = EPADConfig.dicomServerPort;
			String dicomServerTitleAndPort = aeTitle + "@" + dicomServerIP + ":" + dicomServerPort;

			dicomServerTitleAndPort = dicomServerTitleAndPort.trim();

			String dirPath = inputDirFile.getAbsolutePath();
			if (pathContainsSpaces(dirPath))
				dirPath = escapeSpacesInDirPath(dirPath);

			File dir = new File(dirPath);
			int nbFiles = -1;
			if (dir != null) {
				String[] filePaths = dir.list();
				if (filePaths != null)
					nbFiles = filePaths.length;
			}
			log.info("./dcmsnd: sending " + nbFiles + " file(s) - command: ./dcmsnd " + dicomServerTitleAndPort + " "
					+ dirPath);

			String[] command = { "./dcmsnd", dicomServerTitleAndPort, dirPath };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String dicomScriptsDir = EPADConfig.getEPADWebServerDICOMBinDir();
			processBuilder.directory(new File(dicomScriptsDir));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmsend output: " + line);
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM send exit value is: " + exitValue);
			} catch (InterruptedException e) {
				log.warning("Didn't send DICOM files in: " + inputDirFile.getAbsolutePath(), e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error"))
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
		} catch (Exception e) {
			log.warning("DicomSendTask failed to send DICOM files: " + e.getMessage());
			if (e instanceof IllegalStateException && throwException) {
				throw e;
			}
			if (throwException) {
				throw new IllegalStateException("DicomSendTask failed to send DICOM files", e);
			}
		} catch (OutOfMemoryError oome) {
			log.warning("DicomSendTask out of memory: ", oome);
			if (throwException) {
				throw new IllegalStateException("DicomSendTask out of memory: ", oome);
			}
		} finally {
			IOUtils.closeQuietly(tagFileWriter);
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * TODO This does not work. The ./dcmdeleteSeries script invoked the dcm4chee twiddle command but it appears that the
	 * moveSeriesToTrash operation it calls has no effect in this version of dcm4chee. See:
	 * http://www.dcm4che.org/jira/browse/WEB-955
	 */
	public static boolean deleteSeries(String seriesUID, String seriesPk)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean success = false;

		try {
			log.info("Deleting series " + seriesUID);

			String[] command = { "./dcmdeleteSeries", seriesPk };
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			String myScriptsDirectory = EPADConfig.getEPADWebServerMyScriptsDir();
			processBuilder.directory(new File(myScriptsDirectory));
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				log.info("./dcmdeleteSeries: " + line);
			}
			try {
				int exitValue = process.waitFor();
				if (exitValue == 0) {
					log.info("Deleted DICOM series " + seriesUID + " pk:" + seriesPk);
					success = true;
				} else {
					log.warning("Failed to delete DICOM series " + seriesUID + "; exitValue=" + exitValue + "\n" + sb.toString());
				}
			} catch (Exception e) {
				log.warning("Failed to delete DICOM series " + seriesUID, e);
			}

			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (IOException e) {
			log.warning("Failed to delete DICOM series " + seriesUID, e);
		} finally {
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(isr);
			IOUtils.closeQuietly(is);
		}
		return success;
	}

	public static void deleteStudy(String studyUID)
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			log.info("Deleting study " + studyUID + " files - command: ./dcmdeleteStudy " + studyUID);

			String[] command = { "./dcmdeleteStudy", studyUID };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsBinDirectory = EPADConfig.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsBinDirectory));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				log.info("./dcmdeleteStudy: " + line);
				sb.append(line).append("\n");
			}

			try {
				int exitValue = process.waitFor();
				log.info("DICOM delete study exit value is: " + exitValue);
			} catch (Exception e) {
				log.warning("Failed to delete DICOM study " + studyUID, e);
			}
			String cmdLineOutput = sb.toString();

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}
		} catch (Exception e) {
			log.warning("Failed to delete DICOM study " + studyUID, e);
		}
	}

	private static String parseError(String output)
	{
		String result = "";
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					result = result + currLine;
				}
			}
		} catch (Exception e) {
			log.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		if (result.length() > 0)
			return result;
		else
			return output;
	}

	/**
	 * 
	 * @param dirPath String
	 * @return boolean true if this path contains spaces.
	 */
	private static boolean pathContainsSpaces(String dirPath)
	{
		return dirPath.indexOf(' ') > 0;
	}

	/**
	 * 
	 * @param path String
	 * @return String
	 */
	private static String escapeSpacesInDirPath(String path)
	{
		StringBuilder sb = new StringBuilder();
		String[] parts = path.split(" ");
		boolean isFirst = true;
		for (String part : parts) {
			if (!isFirst) {
				sb.append("\\ ");
			}
			sb.append(part);
			isFirst = false;
		}
		return sb.toString();
	}
}
