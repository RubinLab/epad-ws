package edu.stanford.epad.epadws.handlers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.processing.pipeline.watcher.QueueAndWatcherManager;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.xnat.XNATSessionOperations;

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

	private final QueueAndWatcherManager queueAndWatcherManager = QueueAndWatcherManager.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		PrintWriter responseStream = null;
		int statusCode;

		httpResponse.setContentType("text/plain;charset=UTF-8");
		request.setHandled(true);

		try {
			responseStream = httpResponse.getWriter();

			if (XNATSessionOperations.hasValidXNATSessionID(httpRequest)) {
				String method = httpRequest.getMethod();
				if ("GET".equalsIgnoreCase(method)) {
					try {
						String fixValue = httpRequest.getParameter("fix");
						boolean fix = fixValue != null && fixValue.equalsIgnoreCase("true");
						verifyImageGeneration(responseStream, fix);
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

	private void verifyImageGeneration(PrintWriter responseStream, boolean fix) throws SQLException, IOException
	{
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();
		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllReadyDcm4CheeSeriesUIDs();
		List<Map<String, String>> allUnprocessedDICOMFileDescriptions = new ArrayList<Map<String, String>>();

		int numberOfSeriesWithMissingEPADDatabaseEntry = 0;

		// Verify that each image in a DICOM series in DCM4CHEE has an entry for a generated PNG file in the ePAD database,
		// which indicates that the image's existence was detected. We then detect that the PNG file itself exists.
		for (String seriesUID : seriesUIDs) {
			final List<Map<String, String>> unprocessedDICOMFileDescriptionsInSeries = epadQueries
					.getUnprocessedDICOMFileDescriptionsForSeries(seriesUID);
			final int numberOfUnprocessedImages = unprocessedDICOMFileDescriptionsInSeries.size();

			if (numberOfUnprocessedImages != 0) {
				responseStream.write("Number of instances in series " + seriesUID
						+ " for which there is no ePAD database entry for a PNG file = " + numberOfUnprocessedImages + "\n");
				numberOfSeriesWithMissingEPADDatabaseEntry++;
				allUnprocessedDICOMFileDescriptions.addAll(unprocessedDICOMFileDescriptionsInSeries);
			} else {
				// responseStream.write("All instances detected for series " + seriesUID + "\n");
			}
		}
		// TODO: DICOM segmentation objects will not have PNGs. How to test? Tags should have indication.
		// See: PixelMedUtils.isDicomSegmentationObject(inputDICOMFilePath)

		int numberOfPNGFilesWithErrors = 0;
		for (String pngFileName : epadDatabaseOperations.getAllEPadFilePathsWithErrors()) {
			responseStream.write("PNG file " + pngFileName + " generation failed\n");
			numberOfPNGFilesWithErrors++;
		}

		int numberOfPNGFilesInPipeline = 0;
		for (String pngFileName : epadDatabaseOperations.getAllEPadInPipelineFilePaths()) {
			responseStream.write("PNG file " + pngFileName + " in pipeline\n");
			numberOfPNGFilesInPipeline++;
		}

		responseStream.write("Number of dcm4chee series  = " + seriesUIDs.size() + "\n");
		if (numberOfSeriesWithMissingEPADDatabaseEntry != 0)
			responseStream.write("Number of series in dcm4chee that ePAD has no record of = "
					+ numberOfSeriesWithMissingEPADDatabaseEntry + "\n");
		responseStream.write("Total number of dcm4chee images that do not have PNGs in ePAD = "
				+ allUnprocessedDICOMFileDescriptions.size() + "\n");
		responseStream.write("Total number of invalid PNG files = " + numberOfPNGFilesWithErrors + "\n");
		responseStream.write("Total number of pending PNG files = " + numberOfPNGFilesInPipeline + "\n");

		if (fix) {
			if (allUnprocessedDICOMFileDescriptions.size() != 0) {
				responseStream.write("Adding " + allUnprocessedDICOMFileDescriptions.size()
						+ " unprocessed image(s) to PNG pipeline...");
				responseStream.flush();
				queueAndWatcherManager.addToPNGGeneratorTaskPipeline("REPROCESS", allUnprocessedDICOMFileDescriptions);
				responseStream.write("All unprocessed files added\n");
			}
		} else if (allUnprocessedDICOMFileDescriptions.size() != 0)
			responseStream.write("Use fix=true to attempt to regenerate any broken PNGs\n");
	}
}
