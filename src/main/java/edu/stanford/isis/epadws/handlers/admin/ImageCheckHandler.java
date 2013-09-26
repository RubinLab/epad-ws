package edu.stanford.isis.epadws.handlers.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.processing.pipeline.QueueAndWatcherManager;

/**
 * @author martin
 */
public class ImageCheckHandler extends AbstractHandler
{
	private static final String FORBIDDEN_MESSAGE = "Forbidden method - only GET supported!";
	private static final String INTERNAL_ERROR_MESSAGE = "Internal server error";
	private static final String INTERNAL_IO_ERROR_MESSAGE = "Internal server IO error";
	private static final String INTERNAL_SQL_ERROR_MESSAGE = "Internal server SQL error";
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private final QueueAndWatcherManager queueAndWatcherManager = QueueAndWatcherManager.getInstance();

	@Override
	public void handle(String s, Request request, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException
	{
		String method = httpServletRequest.getMethod();
		PrintWriter out = httpServletResponse.getWriter();

		httpServletResponse.setContentType("text/plain;charset=UTF-8");
		request.setHandled(true);

		if ("GET".equalsIgnoreCase(method)) {
			try {
				log.info("Received request");
				verifyImageGeneration(out);
				httpServletResponse.setStatus(HttpServletResponse.SC_OK);
			} catch (IOException e) {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warning(INTERNAL_IO_ERROR_MESSAGE, e);
				out.print(createJSONErrorResponse(INTERNAL_IO_ERROR_MESSAGE, e));
			} catch (SQLException e) {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warning(INTERNAL_SQL_ERROR_MESSAGE, e);
				out.print(createJSONErrorResponse(INTERNAL_SQL_ERROR_MESSAGE, e));
			} catch (Exception e) {
				httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warning(INTERNAL_ERROR_MESSAGE, e);
				out.print(createJSONErrorResponse(INTERNAL_ERROR_MESSAGE, e));
			}
		} else {
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			log.info(FORBIDDEN_MESSAGE);
			out.print(createJSONErrorResponse(FORBIDDEN_MESSAGE));
		}
		out.flush();
	}

	private void verifyImageGeneration(PrintWriter out) throws SQLException, IOException
	{
		final MySqlQueries dbQueries = MySqlInstance.getInstance().getMysqlQueries();
		final List<String> seriesIUIDs = dbQueries.getNewSeries();
		List<Map<String, String>> allUnprocessedDICOMImageFileDescriptions = new ArrayList<Map<String, String>>();

		int numberOfSeriesWithMissingEPADDatabaseEntry = 0;
		int numberOfMissingPNGFiles = 0;

		// Verify that each image in a DICOM series in DCM4CHEE has an entry for a generated PNG file in the ePAD database,
		// indicating that the images existence was correctly detected. Below, we then detect that the PNG file itself
		// exists.
		for (String seriesIUID : seriesIUIDs) {
			final List<Map<String, String>> unprocessedDICOMImageFileDescriptionsInSeries = dbQueries
					.getUnprocessedDICOMImageFileDescriptions(seriesIUID);
			final int numberOfUnprocessedImages = unprocessedDICOMImageFileDescriptionsInSeries.size();

			if (numberOfUnprocessedImages != 0) {
				out.write("Number of instances in series " + seriesIUID
						+ " for which there is no ePAD database entry for a PNG file = " + numberOfUnprocessedImages + "\n");
				numberOfSeriesWithMissingEPADDatabaseEntry++;
				allUnprocessedDICOMImageFileDescriptions.addAll(unprocessedDICOMImageFileDescriptionsInSeries);
			} else {
				// out.write("All instances detected for series " + seriesIUID + "\n");
			}

			// Each file description is a map with keys: sop_iuid, inst_no, series_iuid, filepath, file_size.
			// for (Map<String, String> unprocessedImageFileDescription : unprocessedImageFileDescriptions) {
			// final String sop_iuid = unprocessedImageFileDescription.get("sop_iuid");
			// final String series_iuid = unprocessedImageFileDescription.get("series_iuid");
			// final String inst_no = unprocessedImageFileDescription.get("inst_no");
			// final String filepath = unprocessedImageFileDescription.get("filepath");
			// final String message = "DICOM instance with no ePAD database entry: sop_iuid: " + sop_iuid + ", series_iuid: "
			// + series_iuid + ", inst_no: " + inst_no + ", filepath: " + filepath;
			// out.write(message + "\n");
			// }
		}
		// Verify existence of all PNG files listed in the ePAD database (in epaddb.epad_files table).
		// TODO: DICOM segmentation objects will not have PNGs. How to test? Tags should have indication.
		// See: PixelMedUtils.isDicomSegmentationObject(inputDICOMFilePath)
		List<String> pngFileNames = dbQueries.selectEpadFilePath();
		// out.write("The following PNG files listed in ePAD database do not exist in the file system:\n");
		for (String pngFileName : pngFileNames) {
			File pngFile = new File(pngFileName);
			if (!pngFile.isFile()) {
				String message = pngFileName;
				out.write(message + " \n");
				// numberOfMissingPNGFiles++;
			}
		}
		out.write("Number of series in DICOM database = " + seriesIUIDs.size() + "\n");
		if (numberOfSeriesWithMissingEPADDatabaseEntry != 0)
			out.write("Number of series with missing ePAD database entries = " + numberOfSeriesWithMissingEPADDatabaseEntry
					+ "\n");
		out.write("Total number of unprocessed instances " + allUnprocessedDICOMImageFileDescriptions.size() + "\n");
		out.write("Total number of PNG files = " + pngFileNames.size() + "\n");
		if (numberOfMissingPNGFiles != 0)
			out.write("Number of missing PNG files = " + numberOfMissingPNGFiles + "\n");

		if (allUnprocessedDICOMImageFileDescriptions.size() != 0) {
			out.write("Adding " + allUnprocessedDICOMImageFileDescriptions.size() + " unprocessed images to PNG pipeline...");
			out.flush();
			queueAndWatcherManager.addToPNGGeneratorTaskPipeline(allUnprocessedDICOMImageFileDescriptions);
		}
	}

	private String createJSONErrorResponse(String errorMessage)
	{
		return "{ \"error\": \"" + errorMessage + "\"}";
	}

	private String createJSONErrorResponse(String errorMessage, Exception e)
	{
		return "{ \"error\": \"" + errorMessage + ": " + e.getMessage() + "\"}";
	}
}
