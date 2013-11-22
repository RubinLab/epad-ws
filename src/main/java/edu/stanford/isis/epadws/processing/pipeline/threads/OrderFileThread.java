/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline.threads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.model.DicomImageOrder;
import edu.stanford.isis.epadws.processing.model.DicomOrderFileUtils;
import edu.stanford.isis.epadws.processing.model.DicomSeriesFileUtils;

/**
 * This thread updates order files about once per seconds as new data arrives.
 */
public class OrderFileThread implements Runnable
{
	private static final int ORDER_FILE_SLEEP_TIME_MS = 2000;

	private static final EPADLogger logger = EPADLogger.getInstance();
	private final ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
	private final ExecutorService exec;
	private final BlockingQueue<File> inQueue;

	public OrderFileThread(ExecutorService exec, BlockingQueue<File> orderFileQueue)
	{
		this.exec = exec;
		this.inQueue = orderFileQueue;
	}

	@Override
	public void run()
	{
		logger.info("Starting OrderFileRunnable thread.");

		try {
			do { // Empty the queue.
				List<File> allMoveFiles = new ArrayList<File>();
				inQueue.drainTo(allMoveFiles);
				Map<String, List<OrderFileEntry>> orderFileMap = organizeBySeriesName(allMoveFiles);

				long startTime = System.currentTimeMillis();
				Set<String> allOrderFilePath = orderFileMap.keySet();
				for (String currOrderFilePath : allOrderFilePath) {
					logger.info("currOrderFilePath=" + currOrderFilePath);
					List<OrderFileEntry> currFiles = orderFileMap.get(currOrderFilePath);
					Collections.sort(currFiles);
					updateOrderFileWith(currOrderFilePath, currFiles);
					updateSeriesFileWithImageCount(currOrderFilePath);
				}
				if (allOrderFilePath.size() > 0) {
					logger.info("Updating " + allOrderFilePath.size() + " series took: "
							+ (System.currentTimeMillis() - startTime) + "ms. ");
				}
				if (shutdownSignal.hasShutdown()) {
					break;
				}
				TimeUnit.MILLISECONDS.sleep(ORDER_FILE_SLEEP_TIME_MS);
			} while (!shutdownSignal.hasShutdown());
		} catch (Exception e) {
			logger.severe("OrderFile Process had error.", e);
		} finally {
			logger.info("Stopping OrderFileRunnable thread.");
		}
	}

	public ExecutorService getExecutorService()
	{
		return exec;
	}

	/**
	 * 
	 * @param orderFilePath String the path to the order file, which needs to be converted into a path to the series file.
	 */
	private static void updateSeriesFileWithImageCount(String orderFilePath)
	{

		String seriesFilePath = "";
		try {
			// get number of files in directory.
			String seriesDirPath = convertOrderPathToSeriesDir(orderFilePath);

			int nDcmFiles = EPADFileUtils.countFilesWithEnding(seriesDirPath, ".dcm");

			// convert the path name.
			seriesFilePath = convertOrderPathToSeriesPath(orderFilePath);

			// write a new series file by reading in the current file and replacing just "images in series"
			Map<String, String> dataMap = DicomSeriesFileUtils.readSeriesFile(seriesFilePath);
			dataMap.put(DicomSeriesFileUtils.IMAGES_IN_SERIES, "" + nDcmFiles);
			logger.info("Updating _" + seriesFilePath + "_ with IMAGES_IN_SERIES=" + nDcmFiles);
			DicomSeriesFileUtils.writeSeriesFile(seriesFilePath, dataMap);

		} catch (Exception e) {
			logger.warning("Failed to update series file: " + seriesFilePath, e);
		}

	}// updateSeriesFileWithImageCount

	/**
	 * Given a orderFilePath return just the parent directory.
	 * 
	 * from: ./resources/dicom/1_2_3_456/series_1_4_789.order to: ./resources/dicom/1_2_3_456/1_4_789
	 * 
	 * @param orderFilePath String ex. ./resources/dicom/1_2_3_456/series_1_4_789.order
	 * @return String ex. ./resources/dicom/1_2_3_456/1_4_789
	 */
	private static String convertOrderPathToSeriesDir(String orderFilePath)
	{
		orderFilePath = orderFilePath.replaceAll("series_", "");
		return orderFilePath.replaceAll("\\.order", "");
	}

	/**
	 * Convert an order file path into a series file path. Just need to change the name
	 * 
	 * from: series_1_2_840_113619_2_55_3_2819284057_197_1234280964_798_4.order
	 * 
	 * to: 1_2_840_113619_2_55_3_2819284057_197_1234280964_798_4.series
	 * 
	 * @param orderFilePath String
	 * @return String
	 */
	private static String convertOrderPathToSeriesPath(String orderFilePath)
	{
		String retVal = orderFilePath.replaceAll("series_", "");
		return retVal.replaceAll("\\.order", ".series");
	}

	/**
	 * Give the orderFilePath, determine where it is in the directory structure.
	 * 
	 * @param orderFilePath - String
	 * @param entriesToAdd - List<OrderFileEntry>
	 * @throws IOException tries to read order files
	 */
	private static void updateOrderFileWith(String orderFilePath, List<OrderFileEntry> entriesToAdd) throws IOException
	{
		if (entriesToAdd.size() == 0) {
			throw new IllegalStateException("No entries for orderFilePath=" + orderFilePath);
		}

		File orderFile = new File(orderFilePath);
		if (!orderFile.exists()) {
			orderFile.createNewFile();
		}

		// we need to read the order-file, insert the new lines and then write it out.
		List<DicomImageOrder> imageOrderList = DicomOrderFileUtils.readOrderFileFromTags(orderFile);
		List<DicomImageOrder> updatedOrderList = insertNewImages(imageOrderList, entriesToAdd);
		try {
			DicomOrderFileUtils.writeOrderFile(updatedOrderList, orderFile);
		} catch (IOException ioe) {
			logger.warning("Failed to update: " + orderFile.getAbsolutePath(), ioe);
		}
	}// updateOrderFileWith

	@SuppressWarnings("unused")
	private static void debugImageOrderList(List<DicomImageOrder> imageOrderList)
	{
		logger.info("===DEBUG IMAGE ORDER.   size=" + imageOrderList.size());
		for (DicomImageOrder curr : imageOrderList) {
			logger.info(" image-order=" + curr.toString());
		}
	}

	/**
	 * Merge the image order lists from the file on the hard drive with those just arriving.
	 * 
	 * @param fileList - List
	 * @param entriesToAdd - List
	 * @return List of ImageOrder
	 */
	private static List<DicomImageOrder> insertNewImages(List<DicomImageOrder> fileList, List<OrderFileEntry> entriesToAdd)
	{
		Set<DicomImageOrder> fileSet = new HashSet<DicomImageOrder>(fileList);
		for (OrderFileEntry currEntry : entriesToAdd) {
			int order = currEntry.getOrder();
			String sopInstanceID = currEntry.tagsMap.get(DicomTagFileUtils.SOP_INST_UID);
			fileSet.add(new DicomImageOrder(order, sopInstanceID, currEntry.tagsMap));
		}

		List<DicomImageOrder> retVal = new ArrayList<DicomImageOrder>(fileSet);
		retVal = DicomOrderFileUtils.removeDuplicateInstanceNumbers(retVal);
		Collections.sort(retVal);

		return retVal;
	}

	/**
	 * Reads all new the *.tag files in the queue to determine which series they belong to, and fill out a data-structure
	 * 
	 * @param allMoveFiles List of Files
	 * @return Map of Strings to List of OrderFileEntry
	 */
	private Map<String, List<OrderFileEntry>> organizeBySeriesName(List<File> allMoveFiles)
	{

		Map<String, List<OrderFileEntry>> retVal = new HashMap<String, List<OrderFileEntry>>();
		for (File currFile : allMoveFiles) {
			// logger.debug("organizeBySeriesName: "+currFile.getAbsolutePath());
			Map<String, String> currTagsMap;
			// read the tag file.
			try {
				File currTagFile = new File(DicomTagFileUtils.createTagFilePath(currFile.getAbsolutePath()));
				currTagsMap = DicomTagFileUtils.readTagFile(currTagFile);
				OrderFileEntry orderFileEntry = createOrderFileEntry(currTagsMap, currFile);

				String currStudyUID = currTagsMap.get(DicomTagFileUtils.STUDY_UID);
				String currSeriesUID = currTagsMap.get(DicomTagFileUtils.SERIES_UID);
				File currOrderFile = DicomOrderFileUtils.createOrderFile(currStudyUID, currSeriesUID);
				String key = currOrderFile.getAbsolutePath();

				List<OrderFileEntry> entryList = retVal.get(key);
				if (entryList == null) {
					if (!orderFileExists(key)) {
						// create order file.
						File orderFile = new File(key);
						orderFile.createNewFile();
						logger.info("Created OrderFile: " + orderFile.getAbsolutePath());
					}
					entryList = DicomOrderFileUtils.readOrderFile(key, currTagsMap, currFile.getName());
					// logger.info("entryList size="+entryList.size());
					retVal.put(key, entryList);
				}// if

				entryList.add(orderFileEntry);
				// logger.info("entryList[2] size="+entryList.size());
				retVal.put(key, entryList);
			} catch (IOException e) {
				logger.warning("Failed to read file: " + currFile.getAbsolutePath(), e);
			}
		}// for
		return retVal;
	}

	/**
	 * Return true if the order file exists.
	 * 
	 * @param filePath String path to file.
	 * @return boolean true if the file exists.
	 */
	private boolean orderFileExists(String filePath)
	{
		File f = new File(filePath);
		return f.exists();
	}

	public static OrderFileEntry createOrderFileEntry(Map<String, String> tagsMap, File dicomFile)
	{
		return new OrderFileEntry(dicomFile, tagsMap);
	}

	/**
	 * An internal data-structure to update the order entry file.
	 */
	public static class OrderFileEntry implements Comparable<OrderFileEntry>
	{
		private final File dcmFile;
		private final Map<String, String> tagsMap;
		private final int order;

		public OrderFileEntry(File dicomFile, Map<String, String> tagsMap)
		{
			dcmFile = dicomFile;
			this.tagsMap = tagsMap;
			order = getOrder();
		}

		private int getOrder()
		{
			int retVal = -1;
			String instanceNumber = "n/a";
			try {
				instanceNumber = tagsMap.get(DicomTagFileUtils.INSTANCE_NUMBER);
				retVal = Integer.parseInt(instanceNumber);
			} catch (Exception e) {

				String seriesNumber = tagsMap.get(DicomTagFileUtils.SERIES_NUMBER);
				String acqNumber = tagsMap.get(DicomTagFileUtils.ACQUISITION_NUMBER);

				logger.warning("Invalid order value from: " + dcmFile.getAbsolutePath() + " value=" + instanceNumber
						+ " seriesNumber=" + seriesNumber + " acqNumber=" + acqNumber, e);
				logger.debug(debugAllKeys());
			}
			return retVal;
		}

		@Override
		public int compareTo(OrderFileEntry orderFileEntry)
		{
			return this.order - orderFileEntry.order;
		}

		/**
		 * 
		 * @return String
		 */
		public String debugAllKeys()
		{
			Set<String> allKeys = tagsMap.keySet();
			StringBuilder sb = new StringBuilder();
			for (String currKey : allKeys) {
				sb.append("_").append(currKey).append("_, ");
			}// for
			return sb.toString();
		}

	}// class OrderFileEntry

}// class OrderFileRunnable
