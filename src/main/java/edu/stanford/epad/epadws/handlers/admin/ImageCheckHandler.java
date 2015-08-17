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
package edu.stanford.epad.epadws.handlers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.processing.pipeline.task.DSOMaskPNGGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.task.SingleFrameDICOMPngGeneratorTask;
import edu.stanford.epad.epadws.processing.pipeline.watcher.QueueAndWatcherManager;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.SessionService;

/**
 * @author martin
 */
public class ImageCheckHandler extends AbstractHandler
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final String FORBIDDEN = "Forbidden method - only GET supported on image check route";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error on image check route";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error on image check route";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error on image check route";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid for image check route";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		if (request != null)
			request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();
			responseStream.write("");
			responseStream.flush();

			if (SessionService.hasValidSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					try {
						String fixValue = httpRequest.getParameter("fix");
						boolean fix = fixValue != null && fixValue.equalsIgnoreCase("true");
						String seriesUID = httpRequest.getParameter("seriesUID");
						String imageUID = httpRequest.getParameter("imageUID");
						if (seriesUID != null)
						{
							fixSeriesImages(responseStream, seriesUID, imageUID);
						}
						else 
						{
							String response = verifyImageGeneration(fix);
							responseStream.write(response);
						}
						if (fix)
						{
							String response = generateDSOMasks(true);
							responseStream.write(response);
						}
						statusCode = HttpServletResponse.SC_OK;
					} catch (IOException e) {
						statusCode = HandlerUtil.internalErrorResponse(INTERNAL_IO_ERROR_MESSAGE, e, responseStream, log);
					} catch (SQLException e) {
						statusCode = HandlerUtil.internalErrorResponse(INTERNAL_SQL_ERROR_MESSAGE, e, responseStream, log);
					}
				} else {
					statusCode = HandlerUtil.warningResponse(HttpServletResponse.SC_FORBIDDEN, FORBIDDEN, responseStream, log);
				}
			} else {
				statusCode = HandlerUtil.invalidTokenJSONResponse(INVALID_SESSION_TOKEN_MESSAGE, responseStream, log);
			}
			responseStream.flush();
		} catch (Throwable t) {
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
		}
		httpResponse.setStatus(statusCode);
	}

	public static String verifyImageGeneration(boolean fix) throws SQLException, IOException
	{
		log.info("Starting ImageCheck, fix=" + fix);
		long startTime = System.currentTimeMillis();
		String response = "";
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();
		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllReadyDcm4CheeSeriesUIDs();
		Set<DICOMFileDescription> allUnprocessedDICOMFileDescriptions = new HashSet<DICOMFileDescription>();

		int numberOfSeriesWithMissingEPADDatabaseEntry = 0;
		epadDatabaseOperations.deleteObsoleteEpadFileEntries();

		// Verify that each image in a DICOM series in DCM4CHEE has an entry for a generated PNG file in the ePAD database,
		// which indicates that the image's existence was detected. We then detect that the PNG file itself exists.
		for (String seriesUID : seriesUIDs) {
			final Set<DICOMFileDescription> unprocessedDICOMFileDescriptionsInSeries = epadQueries
					.getUnprocessedDICOMFilesInSeries(seriesUID);
			final int numberOfUnprocessedImages = unprocessedDICOMFileDescriptionsInSeries.size();
			if (numberOfUnprocessedImages != 0) {
				String modality = unprocessedDICOMFileDescriptionsInSeries.iterator().next().modality;
				if (modality != null && modality.equals("SEG"))
				{
					response = response + "Missing mask images for DSO series " + seriesUID + "\n";
					log.info("Missing mask images for DSO series " + seriesUID);
				}
				else
				{
					String message = "Number of instances in series " + seriesUID
							+ " for which there is no ePAD database entry for a PNG file = " + numberOfUnprocessedImages;
					response = response + message + "\n";
				}
				numberOfSeriesWithMissingEPADDatabaseEntry++;
				if (fix)
					QueueAndWatcherManager.getInstance().addDICOMFileToPNGGeneratorPipeline("REPROCESS", unprocessedDICOMFileDescriptionsInSeries);
				allUnprocessedDICOMFileDescriptions.addAll(unprocessedDICOMFileDescriptionsInSeries);
			} else {
				// response = response + "All instances detected for series " + seriesUID + "\n";
			}
		}

		int numberOfPNGFilesWithErrors = 0;
		for (String pngFileName : epadDatabaseOperations.getAllEPadFilePathsWithErrors()) {
			String message = "PNG file " + pngFileName + " generation failed";
			response = response + message + "\n";
			numberOfPNGFilesWithErrors++;
		}

		response = response + "Number of dcm4chee series  = " + seriesUIDs.size() + "\n";
		if (numberOfSeriesWithMissingEPADDatabaseEntry != 0)
			response = response + "Number of series in dcm4chee with missing pngs = "
					+ numberOfSeriesWithMissingEPADDatabaseEntry + "\n";
		response = response + "Total number of dcm4chee images that do not have PNGs in ePAD = "
				+ allUnprocessedDICOMFileDescriptions.size() + "\n";
		response = response + "Total number of invalid PNG files = " + numberOfPNGFilesWithErrors + "\n";

		if (fix) {
			if (allUnprocessedDICOMFileDescriptions.size() != 0) {
				response = response + "Adding " + allUnprocessedDICOMFileDescriptions.size()
						+ " unprocessed image(s) to PNG pipeline...";
				String message = "All unprocessed files added to PNG queue";
				response = response + message + "\n";
			}
		} else if (allUnprocessedDICOMFileDescriptions.size() != 0)
			response = response + "Use fix=true to attempt to regenerate any broken PNGs\n";
		if (DSOMaskPNGGeneratorTask.seriesBeingProcessed.size() > 0)
			log.info("DSO Series being processed:" + DSOMaskPNGGeneratorTask.seriesBeingProcessed);
		if (SingleFrameDICOMPngGeneratorTask.imagesBeingProcessed.size() > 0)
			log.info("DICOM Series being processed:" + SingleFrameDICOMPngGeneratorTask.imagesBeingProcessed);
		long endTime = System.currentTimeMillis();
		log.info("ImageCheck done, took " + (endTime-startTime)/1000.0 + " secs");
		return response;
	}
	

	private void fixSeriesImages(PrintWriter responseStream, String seriesUID, String imageUID) throws SQLException, IOException
	{
		log.info("Starting fixSeriesImages, seriesUID=" + seriesUID + " imageUID=" + imageUID);
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();
		Set<DICOMFileDescription> dicomFilesDescriptions = epadQueries.getDICOMFilesInSeries(seriesUID, imageUID);
		QueueAndWatcherManager.getInstance().addDICOMFileToPNGGeneratorPipeline("REPROCESS", dicomFilesDescriptions);
		log.info("Series " +  seriesUID + " added to PNG Pipeline");
		responseStream.write("Series " +  seriesUID + " added to PNG Pipeline\n");
	}
	
	public static String generateDSOMasks(boolean forEmptyFrameNos) throws Exception
	{
		String response = "";
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();
		String query = "DSOSeriesUID is not null AND DSOSeriesUID != ''";
		if (forEmptyFrameNos) query = query + " AND DSOFrameNo is null";
		List<EPADAIM> aims = epadDatabaseOperations.getAIMsByQuery(query);
		for (EPADAIM aim: aims)
		{
			SeriesReference seriesReference = new SeriesReference(null, null, null, aim.dsoSeriesUID);
			EPADSeries series = epadQueries.getSeriesDescription(seriesReference, "admin", "");
			if (series == null) continue;
			Set<DICOMFileDescription> dicomFilesDescriptions = epadQueries.getDICOMFilesInSeries(series.seriesUID, series.firstImageUIDInSeries);
			QueueAndWatcherManager.getInstance().addDICOMFileToPNGGeneratorPipeline("REPROCESS", dicomFilesDescriptions);
			log.info("DSO Series " +  aim.dsoSeriesUID + " added to PNG Pipeline");
			response = response + "DSO Series " +  aim.dsoSeriesUID + " added to PNG Pipeline\n";
		}
		return response;
	}
}
