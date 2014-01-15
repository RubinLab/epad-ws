package edu.stanford.isis.epadws.processing.pipeline.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomFormatUtil;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.EPADResources;
import edu.stanford.isis.epadws.persistence.DatabaseOperations;
import edu.stanford.isis.epadws.persistence.Database;

/**
 * Task to delete a DICOM study
 * 
 * @author amsnyder
 */
public class DicomDeleteTask implements Runnable
{
	private static EPADLogger logger = EPADLogger.getInstance();
	private final String uidToDelete;
	private final boolean deleteStudy;

	public DicomDeleteTask(String uid, boolean deleteStudy)
	{
		this.uidToDelete = uid;
		this.deleteStudy = deleteStudy;
	}

	@Override
	public void run()
	{
		DatabaseOperations dbQueries = Database.getInstance().getDatabaseOperations();

		try {
			if (deleteStudy) {
				List<Map<String, String>> study2series = dbQueries.findAllSeriesInStudy(uidToDelete);
				logger.info("Found " + study2series.size() + " series in study " + uidToDelete);

				dcm4CheeDeleteDicomStudy(uidToDelete); // Must run after finding series in DCM4CHEE

				// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
				for (Map<String, String> series : study2series) {
					String seriesID = series.get("series_iuid");
					logger.info("SeriesID to delete in ePAD database: " + seriesID);
					dbQueries.deleteSeries(seriesID);
				}
				dbQueries.deleteDicomStudy(uidToDelete);
				deletePNGsforDicomStudy(uidToDelete);
			} else {
				logger.warning("Attempt at (currently unsupported) delete of individual series " + uidToDelete);
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
	 * Delete PNGs
	 * 
	 * @param uid
	 * @throws Exception
	 */
	private static void deletePNGsforDicomStudy(String studyUID) throws Exception
	{
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
		outputPath.append(DicomFormatUtil.formatUidToDir(studyUID)).append("");

		File dirToDelete = new File(outputPath.toString());
		boolean success = delete(dirToDelete);

		logger.info("Deleting the PNG for study at " + outputPath.toString() + " success = " + success);
	}

	/**
	 * Delete PNGs
	 * 
	 * @param uid
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static void deletePNGforSeries(String seriesUID) throws Exception
	{

		DatabaseOperations queries = Database.getInstance().getDatabaseOperations();
		String studyUID = queries.getStudyUIDForSeries(seriesUID);
		StringBuilder outputPath = new StringBuilder();
		outputPath.append(EPADResources.getEPADWebServerPNGDir());
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

	private static void dcm4CheeDeleteDicomStudy(String uid) throws Exception
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			logger.info("Deleting study " + uid + " files - command: ./dcmdeleteStudy " + uid);

			String[] command = { "./dcmdeleteStudy", uid };

			ProcessBuilder pb = new ProcessBuilder(command);
			String myScriptsBinDirectory = EPADResources.getEPADWebServerMyScriptsDir();
			pb.directory(new File(myScriptsBinDirectory));

			Process process = pb.start();
			process.getOutputStream();
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				logger.info("./dcmdeleteStudy: " + line);
				sb.append(line).append("\n");
			}

			try {
				int exitValue = process.waitFor(); // keep.
				logger.info("DICOM delete study exit value is: " + exitValue);
			} catch (Exception e) {
				logger.warning("Didn't delete DICOM files in: " + uid, e);
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
			String myScriptsDirectory = EPADResources.getEPADWebServerMyScriptsDir();
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
			try {
				// int exitValue = process.waitFor(); //keep.
				// long totalTime = System.currentTimeMillis() - startTime;
				// log.info("Tags exit value is: " + exitValue+" and took: "+totalTime+" ms");
			} catch (Exception e) {
				logger.warning("Didn't delete DICOM files in: " + uid, e);
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
		String logDirectory = EPADResources.getEPADWebServerLogDir();
		String fileName = logDirectory + "delete_" + System.currentTimeMillis() + ".log";
		EPADFileUtils.write(new File(fileName), contents);
	}

	private static boolean delete(File file) throws IOException
	{
		boolean success = false;
		if (file.isDirectory()) {

			if (file.list().length == 0) {
				success = file.delete();
			} else {
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					delete(fileDelete);
				}
				if (file.list().length == 0) { // Check the directory again; if empty then delete it.
					success = file.delete();
				}
			}
		} else {
			success = file.delete();
		}
		return success;
	}
}
