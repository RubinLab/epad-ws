package edu.stanford.isis.epadws.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.restlet.data.CharacterSet;
import org.restlet.data.Status;
import org.restlet.resource.Get;

import edu.stanford.isis.epad.common.util.EPADConfig;
import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epadws.processing.persistence.MySqlInstance;
import edu.stanford.isis.epadws.processing.persistence.MySqlQueries;

public class SegmentationPathServerResource extends BaseServerResource
{
	private static final String MISSING_QUERY_MESSAGE = "No query in request";
	private static final String DATABASE_EXCEPTION_MESSAGE = "Exception retrieving from ePAD database";

	public SegmentationPathServerResource()
	{
		setNegotiated(false); // Disable content negotiation
	}

	@Override
	protected void doCatch(Throwable throwable)
	{
		log.warning("An exception was thrown in the segmentation path resource.", throwable);
	}

	@Get("text")
	public String query()
	{
		String queryString = getQuery().getQueryString(CharacterSet.UTF_8);
		log.info("Segmentation path query from ePAD : " + queryString);

		if (queryString != null) {
			try {
				String out = queryEPADDatabase(queryString);
				setStatus(Status.SUCCESS_OK);
				return out;
			} catch (Exception e) {
				log.warning(DATABASE_EXCEPTION_MESSAGE, e);
				setStatus(Status.SERVER_ERROR_INTERNAL);
				return DATABASE_EXCEPTION_MESSAGE + ": " + e.getMessage();
			}
		} else {
			log.info(MISSING_QUERY_MESSAGE);
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return MISSING_QUERY_MESSAGE;
		}
	}

	private String queryEPADDatabase(String queryString) throws Exception
	{
		StringBuilder out = new StringBuilder();
		String imageIdKey = getInstanceUIDFromRequest(queryString.trim());
		String[] result = null;
		if (imageIdKey != null) {
			log.info("DCMQR: " + imageIdKey);
			result = retrieveFromEpadDB(imageIdKey); // res=dcmqr(imageIdKey);
		}
		String separator = config.getParam("fieldSeparator");
		out.append("StudyUID" + separator + "SeriesUID" + separator + "ImageUID\n");
		if (result[0] != null && result[1] != null && result[2] != null)
			out.append(result[0] + separator + result[1] + separator + result[2] + "\n");

		return out.toString();
	}

	private static String getInstanceUIDFromRequest(String queryString)
	{

		log.info(queryString);
		String[] parts = queryString.split("&");
		String value = parts[0].trim();
		parts = value.split("=");
		value = parts[1].trim();
		return value;
	}

	public static String[] dcmqr(String imageIdKey) throws Exception
	{
		String study = null;
		String series = null;
		String[] res = new String[3];

		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileWriter tagFileWriter = null;

		try {
			EPADConfig pc = EPADConfig.getInstance();
			String aeTitle = pc.getParam("DicomServerAETitle");
			String dsPort = pc.getParam("DicomServerPort");

			String dcmServerTitlePort = aeTitle + "@localhost:" + dsPort;
			dcmServerTitlePort = dcmServerTitlePort.trim();

			String opt = "-q00080018=" + imageIdKey;

			log.info("command: ./dcmqr " + dcmServerTitlePort + " " + opt);
			String[] command = { "./dcmqr", dcmServerTitlePort, "-I", opt };

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(new File("../../src/dcm4che-2.0.23/bin")); // TODO Fix ?

			Process process = pb.start();
			process.getOutputStream();// get the output stream.
			// Read out dir output
			is = process.getInputStream();
			isr = new InputStreamReader(is);

			br = new BufferedReader(isr);
			String line;
			StringBuilder sb = new StringBuilder();
			boolean isPass = false;

			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");

				if (line.contains("Query Response #1 for Query Request")) {
					isPass = true;
				}

				if (line.contains("(0020,000D)")) {
					if (isPass)
						study = extractInf(line);
				}

				if (line.contains("(0020,000E)")) {
					if (isPass)
						series = extractInf(line);
				}
			}

			res[0] = study;
			res[1] = series;
			res[2] = imageIdKey;

			// Wait to get exit value
			try {
				process.waitFor(); // keep.
			} catch (InterruptedException e) {
				log.warning("Didn't qr dicom files in: " + imageIdKey, e);
			}

			log.info("qr dicom files found : " + study + " " + series);

			String cmdLineOutput = sb.toString();
			writeQRLog(cmdLineOutput);

			if (cmdLineOutput.toLowerCase().contains("error")) {
				throw new IllegalStateException("Failed for: " + imageIdKey);
			}

		} catch (Exception e) {
			log.warning("DicomHeadersTask failed to create dicom tags file.", e);
		} catch (OutOfMemoryError oome) {
			log.warning("DicomHeadersTask OutOfMemoryError: ", oome);
		} finally {
			close(tagFileWriter);
			close(br);
			close(isr);
			close(is);
		}
		return res;
	}

	public static String[] retrieveFromEpadDB(String imageIdKey) throws Exception
	{
		String study = null;
		String series = null;
		String[] res = new String[3];

		String imageIdKeyWithoutDot = imageIdKey.replaceAll("\\.", "_");

		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		String path = queries.selectEpadFilePathLike(imageIdKeyWithoutDot);

		log.info("Segmentation path found : " + path);

		if (path != null) {
			String[] tab = path.split("\\/");

			series = tab[tab.length - 2];
			study = tab[tab.length - 3];
		}

		res[0] = study;
		res[1] = series;
		res[2] = imageIdKeyWithoutDot;

		log.info("Segmentation dicom files found : " + study + " " + series);

		return res;
	}

	private static String extractInf(String line)
	{
		if (line != null) {
			String[] tab = line.split("\\[");
			String[] tab2 = tab[1].split("\\]");

			return tab2[0];
		}
		return null;
	}

	private static void close(Writer writer)
	{
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
				writer = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close writer", e);
		}
	}

	private static void close(Reader reader)
	{
		try {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close reader", e);
		}
	}

	private static void close(InputStream stream)
	{
		try {
			if (stream != null) {
				stream.close();
				stream = null;
			}
		} catch (Exception e) {
			log.warning("Failed to close stream", e);
		}
	}

	private static void writeQRLog(String contents)
	{
		String fileName = "./log/qr_" + System.currentTimeMillis() + ".log"; // TODO Fro config file
		EPADFileUtils.write(new File(fileName), contents);
	}
}
