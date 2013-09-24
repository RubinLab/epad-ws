package edu.stanford.isis.epadws.processing.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.ProxyFileUtils;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epad.common.ResourceUtils;
import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;

/**
 * Task to delete a DICOM study
 * 
 * @author amsnyder
 */
public class DicomDeleteTask implements Runnable
{
	private static ProxyLogger logger = ProxyLogger.getInstance();

	final String uidToDelete;
	final boolean deleteStudy;

	public DicomDeleteTask(String uid, boolean deleteStudy)
	{
		this.uidToDelete = uid;
		this.deleteStudy = deleteStudy;
	}

	@Override
	public void run()
	{
		MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();

		try {
			if (deleteStudy) {
				// Delete from dcm4chee
				dcmDeleteStudy(uidToDelete);

				logger.info("StudyIDXXX: " + uidToDelete);

				List<Map<String, String>> study2series = dbQueries.doSeriesSearch(uidToDelete);

				for (Map<String, String> series : study2series) {
					logger.info("SeriesID: " + series.get("series-id"));
				}

				// Delete the entries in the table
				dbQueries.doDeleteStudy(uidToDelete);

				// Delete the files
				deletePNGforStudy(uidToDelete);
			} else {
				// Not supported in the current version of dcm4chee
				/*
				 * //To avoid to fire the png generation pipeline dbQueries.updateSeriesStatusCodeEx(77,uidToDelete); //Delete
				 * from dcm4chee dcmDeleteSeries(uidToDelete); //Delete the entries in the table
				 * dbQueries.doDeleteSeries(uidToDelete); //Delete the files deletePNGforSeries(uidToDelete);
				 */

			}
		} catch (Exception e) {
			logger.warning("run had: " + e.getMessage(), e);
		}

	}

	/**
	 * Delete pngs success
	 * 
	 * @param uid
	 * @throws Exception
	 */

	private static void deletePNGforStudy(String studyUID) throws Exception
	{

		StringBuilder outputPath = new StringBuilder();
		outputPath.append(ResourceUtils.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		logger.info("Deleting the PNG for study at " + outputPath.toString() + " success = " + success);
	}

	/**
	 * Delete pngs
	 * 
	 * @param uid
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void deletePNGforSeries(String seriesUID) throws Exception
	{

		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String studyUID = queries.getStudyUIDForSeries(seriesUID);

		StringBuilder outputPath = new StringBuilder();
		outputPath.append(ResourceUtils.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("/");
		outputPath.append(DicomFormatUtil.formatUidToDir(seriesUID)).append("/");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		logger.info("Deleting the PNG for series at " + outputPath.toString() + " success = " + success);
	}

	/**
	 * Delete from DCM4CHEE
	 * 
	 * @param uid
	 * @throws Exception
	 */

	private static void dcmDeleteStudy(String uid) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			logger.info("Deleting study " + uid + " files - command: ./dcmdeleteStudy " + uid);

			String[] command = { "./dcmdeleteStudy", uid };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsBinDirectory = ResourceUtils.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsBinDirectory));

			Process process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
			is = process.getInputStream();
			isr = new InputStreamReader(is);

			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

			// Wait to get exit value
			try {
				// int exitValue = process.waitFor(); //keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (Exception e) {
				logger.warning("Didn't delete dicom files in: " + uid, e);
			}

			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}

		} catch (Exception e) {
			logger.warning("Didn't delete dicom files in: " + uid, e);
		}
	}

	/**
	 * Delete from DCM4CHEE
	 * 
	 * @param uid
	 * @throws Exception
	 */

	@SuppressWarnings("unused")
	private static void dcmDeleteSeries(String uid) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			logger.info("Deleting series " + uid + " files - command: ./dcmdeleteSeries " + uid);

			String[] command = { "./dcmdeleteSeries", uid };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsDirectory = ResourceUtils.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsDirectory));

			Process process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
			is = process.getInputStream();
			isr = new InputStreamReader(is);

			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

			// Wait to get exit value
			try {
				// int exitValue = process.waitFor(); //keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (Exception e) {
				logger.warning("Didn't delete dicom files in: " + uid, e);
			}

			String cmdLineOutput = sb.toString();
			writeDeleteLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + parseError(cmdLineOutput));
			}

		} catch (Exception e) {
			logger.warning("Didn't delete dicom files in: " + uid, e);
		}
	}

	private static String parseError(String output)
	{
		try {
			String[] lines = output.split("\n");
			for (String currLine : lines) {
				if (currLine.toLowerCase().contains("error")) {
					return currLine;
				}
			}
		} catch (Exception e) {
			logger.warning("DicomSendTask.parseError had: " + e.getMessage() + " for: " + output, e);
		}
		return output;
	}

	/**
	 * Log the result of this delete to the log directory.
	 * 
	 * @param contents String
	 */
	private static void writeDeleteLog(String contents)
	{
		String logDirectory = ResourceUtils.getEPADWebServerLogDir();
		String fileName = logDirectory + "delete_" + System.currentTimeMillis() + ".log";
		ProxyFileUtils.write(new File(fileName), contents);
	}

	private static boolean delete(File file) throws IOException
	{
		boolean success = false;
		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {
				success = file.delete();
				// System.out.println("Directory is deleted : " + file.getAbsolutePath());
			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					success = file.delete();
					// System.out.println("Directory is deleted : " + file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			success = file.delete();
			// System.out.println("File is deleted : " + file.getAbsolutePath());
		}
		return success;
	}
}
