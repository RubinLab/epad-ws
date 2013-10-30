/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.processing.pipeline;

import java.util.Map;

import edu.stanford.isis.epad.common.dicom.DicomTagFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;

/**
 * Small class used by order files. Represents one line in a file. This can parse and write a single line in an order
 * file.
 */
public class ImageOrder implements Comparable<ImageOrder>
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final int order;
	private final String imageUID;
	private final Map<String, String> tags; // TODO keep all the tags.

	public ImageOrder(int instanceNumber, String imageUID, Map<String, String> tags)
	{
		this.imageUID = imageUID;
		this.order = instanceNumber;
		this.tags = tags;
	}

	/**
	 * Print out in the format used in the order file.
	 * 
	 * @return String [1_2_753_457754_16432.dcm] 45
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		String dash = imageUID.replace('.', '_');
		sb.append("[").append(dash).append(".dcm").append("]").append(" ").append(order);
		return sb.toString();
	}

	/**
	 * Replace toString with this after the meeting. Below is an example of what is can look like.
	 * 
	 * @return String 1_2_753_457754_16432.dcm , 45 , -14.50 , 0834
	 */
	public String toNewFormatString()
	{
		StringBuilder sb = new StringBuilder();
		String dash = imageUID.replace('.', '_');
		sb.append(dash).append(".dcm").append(",");
		sb.append(order).append(",");
		sb.append(tags.get(DicomTagFileUtils.SLICE_LOCATION)).append(","); // sliceLocation
		sb.append(tags.get(DicomTagFileUtils.CONTENT_TIME));// .append("  "); //content-time
		// sb.append("ToDo-imagePosition").append(" , "); //imagePosition

		return sb.toString();
	}

	public static String toNewFormatHeader()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("filename").append(",");
		sb.append("order").append(",");
		sb.append(DicomTagFileUtils.SLICE_LOCATION).append(",");
		sb.append(DicomTagFileUtils.CONTENT_TIME);
		return sb.toString();
	}

	@Override
	public int compareTo(ImageOrder imageOrder)
	{

		int retVal = order - imageOrder.order;

		if (retVal == 0) {
			try {
				// if the order value seems to be the same differ to the tags to break the tie.
				int thisInstanceNum = this.getInstanceNumFromTag();
				int thatInstanceNum = imageOrder.getInstanceNumFromTag();
				int instCompare = thisInstanceNum - thatInstanceNum;
				if (instCompare == 0) {
					log.info("Two files have the same order value. DEBUG INFO TO FOLLOW");
					this.printDebugTags();
					imageOrder.printDebugTags();
				}
				return instCompare;
			} catch (Exception e) {
				log.warning("ImageOrder compare", e);
			}
		}

		return retVal;
	}

	private void printDebugTags()
	{
		log.info(toNewFormatString());
	}

	private int getInstanceNumFromTag()
	{
		return Integer.parseInt(tags.get(DicomTagFileUtils.INSTANCE_NUMBER).trim());
	}

	/**
	 * Get the order which should be the same as the Instance Number DICOM tag.
	 * 
	 * @return int
	 */
	public int getOrder()
	{
		return order;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this) {
			return true;
		}

		if (!(o instanceof ImageOrder)) {
			return false;
		}
		ImageOrder that = (ImageOrder)o;

		return (that.order == this.order && this.imageUID.equals(that.imageUID));
	}

	@Override
	public int hashCode()
	{
		int result = 31;
		result = 11 * result + imageUID.hashCode();
		result = 13 * result + order;

		return result;
	}

}
