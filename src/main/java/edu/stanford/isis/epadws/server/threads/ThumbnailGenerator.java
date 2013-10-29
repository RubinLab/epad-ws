/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.epadws.server.threads;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.stanford.isis.epad.common.dicom.DicomImageUID;
import edu.stanford.isis.epad.common.dicom.DicomQuery;
import edu.stanford.isis.epad.common.dicom.DicomSearchMap;
import edu.stanford.isis.epad.common.dicom.DicomSeriesUID;
import edu.stanford.isis.epad.common.dicom.DicomStudyUID;
import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epad.common.util.WadoUrlBuilder;
import edu.stanford.isis.epadws.server.ShutdownSignal;

/**
 * 
 * @author amsnyder
 */
public class ThumbnailGenerator implements Runnable
{
	private final EPADLogger log = EPADLogger.getInstance();
	private final BlockingQueue<DicomSeriesUID> inputQueue;
	private final DicomSearchMap dicomSearchMap;

	public ThumbnailGenerator(BlockingQueue<DicomSeriesUID> queue)
	{
		inputQueue = queue;
		dicomSearchMap = DicomSearchMap.getInstance();
	}

	@Override
	public void run()
	{
		try {

			ShutdownSignal signal = ShutdownSignal.getInstance();
			log.info("Starting ThumbnailGenerator process.");

			while (!signal.hasShutdown()) {

				DicomSeriesUID seriesId = inputQueue.poll(250, TimeUnit.MILLISECONDS);

				if (seriesId == null)
					continue;

				// Get the studyUID and all list of imageUIDs to prepare for the WADO call.
				DicomStudyUID studyId = dicomSearchMap.getStudyForSeries(seriesId);
				if (!dicomSearchMap.hasImagesForSeries(seriesId)) {
					// Call the DICOM server to get the images
					dicomSearchMap.putImagesForSeries(seriesId, DicomQuery.getImagesForSeries(seriesId));
				}
				List<DicomImageUID> images = dicomSearchMap.getImageUIDsForSeries(seriesId);

				// get the middle image.
				int middleIndex = images.size() / 2;

				DicomImageUID middleImageId = images.get(middleIndex);

				// Build WADO call.
				EPADConfig config = EPADConfig.getInstance();
				String host = config.getParam("DicomServerIP");
				int port = config.getIntParam("DicomServerWadoPort");
				String base = config.getParam("WadoUrlExtension");
				// String base = "/wado?";

				WadoUrlBuilder wadoUrlBuilder = new WadoUrlBuilder(host, port, base, WadoUrlBuilder.ContentType.IMAGE);

				// GET WADO call result.
				wadoUrlBuilder.setStudyUID(studyId.toString());
				wadoUrlBuilder.setSeriesUID(seriesId.toString());
				wadoUrlBuilder.setObjectUID(middleImageId.toString());

				wadoUrlBuilder.setZoomSize(128, 128);

				String wadoThumbnailUrl = wadoUrlBuilder.build();

				log.debug("Thumbnail URL: " + wadoThumbnailUrl);

				File thumbnailFile = createThumbnailFile(studyId, seriesId);
				ResourceUtils.writeJpgUrlToFile(wadoThumbnailUrl, thumbnailFile);

			}// while

		} catch (Exception e) {
			log.warning("ThumbnailGenerator had: ", e);
		}
		log.info("Stopping ThumbnailGenerator process");
	}

	/**
	 * Create the file name for a thumbnail image which is in the format:</br>
	 * 
	 * ./resource/dicom/<StudyId-hash>/thumbnail_<SeriesId-hash>.jpg
	 * 
	 * @param studyUID images parent studyUID
	 * @param seriesUID images seriesUID
	 * @return File thumbnail file for seriesUID
	 * @throws java.io.IOException if write error
	 */
	private File createThumbnailFile(DicomStudyUID studyUID, DicomSeriesUID seriesUID) throws IOException
	{
		String thumbnailFilePath = ResourceUtils.makeThumbnailFilePath(studyUID, seriesUID);

		return ResourceUtils.generateFile(thumbnailFilePath);
	}
}

/**
 * 
 * B.1
 * 
 * http://www.hospital-stmarco/radiology/wado.php?requestType=WADO &studyUID=1.2.250.1.59.40211.12345678.678910
 * &seriesUID=1.2.250.1.59.40211.789001276.14556172.67789 &objectUID=1.2.250.1.59.40211.2678810.87991027.899772.2
 * 
 * B.2
 * 
 * http://server234/script678.asp?requestType=WADO &studyUID=1.2.250.1.59.40211.12345678.678910
 * &seriesUID=1.2.250.1.59.40211.789001276.14556172.67789 &objectUID=1.2.250.1.59.40211.2678810.87991027.899772.2
 * &charset=UTF-8
 * 
 * B.3
 * 
 * Retrieving a region of a DICOM image, converted if possible in JPEG2000, with annotations burned into the image
 * containing the patient name and technical information, and mapped into a defined image size:
 * 
 * https://aspradio/imageaccess.js?requestType=WADO &studyUID=1.2.250.1.59.40211.12345678.678910
 * &seriesUID=1.2.250.1.59.40211.789001276.14556172.67789 &objectUID=1.2.250.1.59.40211.2678810.87991027.899772.2
 * &contentType=image%2Fjp2;level=1,image%2Fjpeg;q=0.5 &annotation=patient,technique &columns=400 &rows=300
 * &region=0.3,0.4,0.5,0.5 &windowCenter=-1000 &windowWidth=2500
 * 
 * 
 * 
 * 
 */

/**
 * http://rufus.stanford.edu:8080/wado?requestType=WADO&studyUID=1.2.752.24.7.19011385.453825&seriesUID=1.2.840.113704.1
 * .111.424.1207241028.11&objectUID=1.2.840.113704.1.111.3820.1207241475.1612&contentType=image/jpeg&windowCenter=100&
 * windowWidth=350&rows=512&columns=512&region=0.1,0.1,0.9,0.9
 */

