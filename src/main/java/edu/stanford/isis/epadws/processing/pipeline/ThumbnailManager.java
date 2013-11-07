/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.model.DicomTagFileUtils;
import edu.stanford.isis.epadws.processing.model.ThumbnailFileUtil;

/**
 * This class manages the creation of thumbnails in the /resources/dicom directory.
 * 
 * On start-up it will look at the the series, to see if any have missing thumbnail files. If a thumbnail file is
 * missing for a series it will read the order file to select which dicom file is used to generate the thumbnail. This
 * file is the put into a the "generateThumbnail" queue.
 * 
 * When a new series is uploaded, the mover stage of the pipeline will ask this class if that file should be used to
 * generate a thumbnail. This class will use the following rules to respond.
 * 
 * (a) If it is the first dcm file for that series then respond yes.
 * 
 * From that point it keeps track of the InstanceNumbers to select a dicom file near the middle of the stack. It will
 * keep track of the min, and max InstanceNumbers along with the most recent used to generate a thumbnail. If the most
 * recent falls outside of the middle 25% of the min/max range then it will look for a new file that is closer to the
 * most recent one to the middle of the range.
 * 
 */
public class ThumbnailManager
{
	private static final EPADLogger logger = EPADLogger.getInstance();
	private static final ThumbnailManager ourInstance = new ThumbnailManager();

	private final Map<FileKey, ThumbnailInfo> thumbnailMap;
	private static BlockingQueue<File> thumbnailQueue = null;
	private static final String iconsDirectory = ResourceUtils.getEPADWebServerIconsDir();
	private static final String genericThumbnailPath = iconsDirectory + "genericThumbnail.jpg";

	public static ThumbnailManager getInstance()
	{
		return ourInstance;
	}

	/**
	 * This is called on startup and is only used once to get the queue for thumbnails.
	 * 
	 * @param tQ BlockingQueue of File
	 * @return ThumbnailManager
	 */
	public static ThumbnailManager getFirstInstance(BlockingQueue<File> tQ)
	{
		thumbnailQueue = tQ;
		return getInstance();
	}

	private ThumbnailManager()
	{
		thumbnailMap = new ConcurrentHashMap<FileKey, ThumbnailInfo>();

		init();
	}

	private void init()
	{
		// inspect the resource directory for thumbnails.
		// List<File> studyDirs = ProxyFileUtils.getDirectoriesIn(new File(ResourceUtils.getEPADWebServerPNGDir()));
		// List<File> dicomFile
		// create missing thumbnails.
	}

	/**
	 * Submit to the queue to create a JPG file.
	 * 
	 * @param tags Map
	 * @param dcmFile File
	 */
	public void writeJpgFile(Map<String, String> tags, File dcmFile)
	{
		logger.info("ThumbnailManager.writeJpgFile.");

		try {
			thumbnailQueue.offer(dcmFile, 100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.warning("Had: " + e.getMessage() + "for " + dcmFile.getAbsolutePath(), e);
		}
	}

	// ToDo: Uncomment if we are using 2-channel PNG files. It depends on PixelMed jar files though.
	/**
	 * Submit to the queue to create the PNG file.
	 * 
	 * @param tags Map
	 * @param dcmFile File
	 */
	public void writeTwoChannelPngFile(Map<String, String> tags, File dcmFile)
	{

		try {

			// ToDo: Just the PackPNGTask call working on this.

		} catch (Exception e) {
			logger.warning("Failed to write 2 channel PNG file.", e);
		}
	}

	@SuppressWarnings("unused")
	private static File createPngFile(String dcmFilePath)
	{
		return new File(dcmFilePath.replaceAll(".dcm", ".png"));
	}

	// //Get back to this later.
	// private static byte[] createTwoChannelPngData(byte[] rawDicom, TransferSyntax transferSyntax, float slope, float
	// intercept, int xSize, int ySize)
	// {
	//
	// byte[] retVal = new byte[rawDicom.length];
	// for(int x=0; x<xSize; x++){
	// for(int y=0; y<ySize; y++){
	// int index = getIndex(x,y) ;
	// int val = Math.round(forceRange((slope*rawDicom[index]-intercept),0.0f,65535.0f));
	// //retVal[index] = val;
	// }//for
	// }//for
	// return retVal;
	// }

	/**
	 * 
	 * @param x int
	 * @param y int
	 * @return int
	 */
	@SuppressWarnings("unused")
	private static int getIndex(int x, int y)
	{
		return (x * y) + x;
	}

	@SuppressWarnings("unused")
	private static float forceRange(float value, float min, float max)
	{
		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}
		return value;
	}

	/**
	 * Shrinks a jpeg file down to create the thumbnail.
	 * 
	 * @param tags Map of String to Strings
	 * @param dcmFile File
	 */
	public void writeThumbnailIfNeeded(Map<String, String> tags, File dcmFile)
	{
		String studyId = tags.get(DicomTagFileUtils.STUDY_UID);
		String seriesId = tags.get(DicomTagFileUtils.SERIES_UID);

		String tPath = ThumbnailFileUtil.createThumbnailPath(studyId, seriesId);
		File thumbnailFile = new File(tPath);

		logger.info("writeThumbnailIfNeeded is checking: " + thumbnailFile.getAbsolutePath());

		FileKey thumbnailKey = new FileKey(thumbnailFile);
		ThumbnailInfo tInfo;
		if (!thumbnailMap.containsKey(thumbnailKey)) {
			tInfo = new ThumbnailInfo(studyId, seriesId);
			thumbnailMap.put(thumbnailKey, new ThumbnailInfo(studyId, seriesId));
			putGenericIcon(thumbnailFile);
		} else {
			tInfo = thumbnailMap.get(thumbnailKey);
		}
		String instanceNumber = tags.get(DicomTagFileUtils.INSTANCE_NUMBER);
		if (tInfo.updateInstanceNumber(Integer.parseInt(instanceNumber))) {
			// the update says we need a new thumbnail, to get this file from the studyId, seriesId and instance number.
			String thumbnailPath = ThumbnailFileUtil.createThumbnailPath(studyId, seriesId);
			String jpgFilePath = EPADFileUtils.replaceExtensionWith(dcmFile, "jpg");
			String returnMsg = ThumbnailFileUtil.shrinkJpegFile(jpgFilePath, thumbnailPath, 32);
			if ("".equals(returnMsg)) {
				logger.info("wrote: " + thumbnailPath);
			} else {
				logger.info("WARNING! Failed to write thumbnail.  " + returnMsg);
			}
		}
	}

	/**
	 * Copy the generic icon there until something better is ready.
	 * 
	 * @param thumbnailFile File
	 */
	private void putGenericIcon(File thumbnailFile)
	{
		try {
			File genericIcon = new File(genericThumbnailPath);
			FileUtils.copyFile(genericIcon, thumbnailFile);
		} catch (IOException e) {
			logger.warning("Failed to place generic icon to: " + thumbnailFile.getAbsolutePath(), e);
		}
	}

	static class ThumbnailInfo
	{
		public final String studyId;
		public final String seriesId;
		public final String thumbnailPath;

		public ThumbnailStatus status;
		private int minOrder = -1;
		private int maxOrder = -1;
		private int lastOrder = -1;

		ThumbnailInfo(String studyId, String seriesId)
		{
			this.studyId = studyId;
			this.seriesId = seriesId;
			this.thumbnailPath = ThumbnailFileUtil.createThumbnailPath(studyId, seriesId);
		}

		/**
		 * Update the min/max values for this series. Return true if it needs to be updated.
		 * 
		 * @param instanceNumber int
		 * @return boolean true if this file should be added to the queue to create a thumbnail.
		 */
		public boolean updateInstanceNumber(int instanceNumber)
		{
			if (minOrder == -1) {
				minOrder = maxOrder = lastOrder = instanceNumber;
				return true;
			}

			if (instanceNumber > maxOrder) {
				maxOrder = instanceNumber;
				return shouldUpdateWithThisInstanceNumber(instanceNumber);
			}

			if (instanceNumber < minOrder) {
				minOrder = instanceNumber;
				return shouldUpdateWithThisInstanceNumber(instanceNumber);
			}
			return false;
		}

		private boolean shouldUpdateWithThisInstanceNumber(int instanceNumber)
		{
			if (isBetterNeeded()) {
				if (shouldUseThisInstanceForThumbnail(instanceNumber)) {
					lastOrder = instanceNumber;
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		/**
		 * Compare the maxOrder, minOrder and lastOrder. If the lastOrder is outside an acceptable range then return true,
		 * and set the internal values.
		 * 
		 * Use the equation. (max-last)/(last-min) It should be near one.
		 * 
		 * @return boolean
		 */
		private boolean isBetterNeeded()
		{
			float max = maxOrder;
			float min = minOrder;
			float last = lastOrder;

			float ratio = (max - last) / (last - min);

			if (ratio < 0.42) {
				return true;
			} else if (ratio > 2.3) {
				return true;
			}
			return false;
		}

		/**
		 * Determine if the this instance is closer to the mid point than lastOrder
		 * 
		 * @param instanceNumber int
		 * @return boolean true if the instanceNumber is closer to the mid-point than the current thumbnail.
		 */
		private boolean shouldUseThisInstanceForThumbnail(int instanceNumber)
		{
			int midPoint = ((maxOrder - minOrder) / 2) + minOrder;

			return Math.abs(midPoint - instanceNumber) < Math.abs(midPoint - lastOrder);
		}

		public void setStatus(ThumbnailStatus tStatus)
		{
			status = tStatus;
		}
	}

	static enum ThumbnailStatus {
		NO_THUMBNAIL, PRE_EXISTING_THUMBNAIL, NEW_SERIES_HAS_GOOD_THUMBNAIL, NEW_SERIES_NEEDS_BETTER_THUMBNAIL;
	}
}
