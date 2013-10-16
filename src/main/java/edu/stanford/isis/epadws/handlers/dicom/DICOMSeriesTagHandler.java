package edu.stanford.isis.epadws.handlers.dicom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDictionary;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;

import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.processing.mysql.MySqlInstance;
import edu.stanford.isis.epadws.processing.mysql.MySqlQueries;
import edu.stanford.isis.epadws.xnat.XNATUtil;

public class DICOMSeriesTagHandler extends AbstractHandler
{
	private static final ProxyLogger log = ProxyLogger.getInstance();

	private static final String INTERNAL_ERROR_MESSAGE = "Internal error on delete";
	private static final String INVALID_SESSION_TOKEN_MESSAGE = "Session token is invalid";
	private static final String MISSING_SERIES_IUID_MESSAGE = "No Series IUID parameter in request";

	@Override
	public void handle(String s, Request request, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws IOException, ServletException
	{
		PrintWriter out = httpResponse.getWriter();

		httpResponse.setContentType("text/plain");
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		request.setHandled(true);

		if (XNATUtil.hasValidXNATSessionID(httpRequest)) {
			String seriesIUID = httpRequest.getParameter("series_iuid");
			if (seriesIUID != null) {
				boolean useBase64 = true;
				String contentType = httpRequest.getParameter("type");
				if ("text".equals(contentType))
					useBase64 = false;
				log.info("DICOMSeriesTagHandler, series_iuid=" + seriesIUID);
				try {
					handleDICOMSeriesTagQuery(out, seriesIUID, useBase64);
					httpResponse.setStatus(HttpServletResponse.SC_OK);
				} catch (IOException e) {
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(INTERNAL_ERROR_MESSAGE + ": " + e.getMessage());
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (DicomException e) {
					log.warning(INTERNAL_ERROR_MESSAGE, e);
					out.print(INTERNAL_ERROR_MESSAGE + ": " + e.getMessage());
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (Throwable t) {
					log.warning(INTERNAL_ERROR_MESSAGE, t);
					out.print(INTERNAL_ERROR_MESSAGE + ": " + t.getMessage());
					httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			} else {
				log.info(MISSING_SERIES_IUID_MESSAGE);
				httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				out.append(MISSING_SERIES_IUID_MESSAGE);
			}
		} else {
			log.info(INVALID_SESSION_TOKEN_MESSAGE);
			out.append(INVALID_SESSION_TOKEN_MESSAGE);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		out.flush();
		out.close();
	}

	private void handleDICOMSeriesTagQuery(PrintWriter out, String seriesIUID, boolean useBase64) throws IOException,
			SQLException, DicomException
	{
		String studyIUID = getStudyIuidFromSeriesId(seriesIUID);
		String patId = getPatientIdFromStudyIuid(studyIUID);
		if (useBase64) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			InputStream seriesStream = getSeriesTagsAsStream(seriesIUID);
			try {
				addToByteArrayBuffer(buffer, seriesStream);
				InputStream imageStream = getInstanceTagsForSeriesAsStream(seriesIUID);
				addToByteArrayBuffer(buffer, imageStream);
				InputStream studyStream = getStudyTagsAsStream(studyIUID);
				addToByteArrayBuffer(buffer, studyStream);
				InputStream patientStream = getPatientTagsAsStream(patId);
				addToByteArrayBuffer(buffer, patientStream);
				String base64Result = base64Encode(buffer);
				out.print(base64Result);
			} finally {
				buffer.close();
				seriesStream.close();
			}
		} else {
			String seriesTags = getSeriesTags(seriesIUID);
			String imageTags = getInstanceTagsForSeries(seriesIUID);
			String studyTags = getStudyTags(studyIUID);
			String patientTags = getPatientTags(patId);
			String result = seriesTags + imageTags + studyTags + patientTags;
			log.info("SeriesTagHandler: result=" + result);
			out.print(result);
		}
	}

	/**
	 * To convert InputStream to byte[]. NOTE: Apache/IO has a nice utility to do this, but we don't have it here, so
	 * doing this instead.
	 * 
	 * @param buffer ByteArrayOutputStream
	 * @param is InputStream
	 * @return ByteArrayOutputStream
	 * @throws IOException - on error
	 */
	private void addToByteArrayBuffer(ByteArrayOutputStream buffer, InputStream is) throws IOException
	{
		if (is != null) {
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
		}
	}

	/**
	 * 
	 * @param buffer ByteArrayOutputStream
	 * @return String that is base64 encoded result of
	 * @throws IOException on error
	 */
	private String base64Encode(ByteArrayOutputStream buffer) throws IOException
	{
		buffer.flush();
		return DatatypeConverter.printBase64Binary(buffer.toByteArray());
	}

	private static String getStudyIuidFromSeriesId(String seriesIUID)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		Map<String, String> tags = queries.getParentStudyForSeries(seriesIUID);
		return tags.get("study_iuid");
	}

	private static InputStream getStudyTagsAsStream(String studyIUID)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		return queries.getStudyAttrsAsStream(studyIUID);
	}

	private static String getStudyTags(String studyIUID) throws IOException, DicomException
	{
		StringBuilder sb = new StringBuilder();
		InputStream stream = getStudyTagsAsStream(studyIUID);

		AttributeList attributeList = new AttributeList();
		attributeList.read(new DicomInputStream(stream));

		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0005));// Specific Character Set -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0020));// Study Date -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0030));// Study Time -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0050));// Accession Number -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0051));// Issuer of Accession Number Sequence -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0090));// Referring Physician Name -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x1030));// Study Description -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x1032));// Procedure Code Seq -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0020, 0x000d));// Study Instance UID -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0020, 0x0010));// Study ID -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0032, 0x000a));// Study Status ID -->

		return sb.toString();
	}

	private static String getPatientIdFromStudyIuid(String studyIUID)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		Map<String, String> tags = queries.getPatientForStudy(studyIUID);
		return tags.get("pat_id");
	}

	private static InputStream getPatientTagsAsStream(String patId)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		return queries.getPatientAttrsAsStream(patId);
	}

	private static String getPatientTags(String patId) throws IOException, DicomException
	{
		StringBuilder sb = new StringBuilder();
		InputStream stream = getPatientTagsAsStream(patId);

		AttributeList attributeList = new AttributeList();
		attributeList.read(new DicomInputStream(stream));

		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0005));// Specific Character Set -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x0010));// Patient's Name -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x0020));// Patient ID -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x0021));// Issuer of Patient ID -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x0030));// Patient's Birth Date -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x0040));// Patient's Sex -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x1002));// Other Patient IDs Sequence -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0010, 0x4000));// Patient Comments -->

		return sb.toString();
	}

	private static InputStream getInstanceTagsForSeriesAsStream(String seriesIUID) throws SQLException, DicomException,
			IOException
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		Blob blob = queries.getImageBlobDataForSeries(seriesIUID);
		if (blob != null) {
			return blob.getBinaryStream();
		}
		return null;
	}

	private static String getInstanceTagsForSeries(String seriesIUID) throws SQLException, DicomException, IOException
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		Blob blob = queries.getImageBlobDataForSeries(seriesIUID);
		if (blob != null) {
			return getInstanceTags(blob.getBinaryStream());
		}
		return "";
	}

	private static String getInstanceTags(InputStream stream) throws IOException, DicomException
	{
		StringBuilder sb = new StringBuilder();

		AttributeList attributeList = new AttributeList();
		attributeList.read(new DicomInputStream(stream));

		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0018, 0x0050));// Slice Thickness -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x1053));// Rescale Slope -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x1052));// Rescale Intercept -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x1051));// Window Width -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x1050));// Window Center -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0103));// Pixel Representation -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0030));// Pixel Spacing -->

		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0018));// SOP Instance UID -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0022));// Acquisition Date -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0023));// Content Date -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x002A));// Acquisition Datetime -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0032));// Acquisition Time -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0033));// Content Time -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x1115));// Referenced Series Sequence -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0020, 0x0013));// Instance Number -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0020,0x1041));//Slice Location -->//different for each
		// image.
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0028,0x0004));//Photometric Interpretation -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0008));// Number of Frames -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0010));// Rows -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0011));// Columns -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0028, 0x0100));// Bits Allocated -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0040, 0xA032));// Observation DateTime -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0040, 0xA043));// Concept Name Code Sequence -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0040,0xA073));//Verifying Observer Sequence -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0040,0xA370));//Referenced Request Sequence -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0040,0xA375));//Current Requested Procedure Evidence
		// Sequence -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0040,0xA385));//Pertinent Other Evidence Sequence -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0040, 0xA491));// Completion Flag -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0040, 0xA493));// Verification Flag -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0040, 0xA525));// Identical Documents Sequence -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0042, 0x0010));// Document Title-->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0042, 0x0012));// MIME Type of Encapsulated Document -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0070, 0x0080));// Content Label -->
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0070, 0x0081));// Content Description -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0070,0x0082));//Presentation Creation Date -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0070,0x0083));//Presentation Creation Time -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0070,0x0084));//Content Creator s Name -->
		// createDicomTagEntry(sb,attributeList,new AttributeTag(0x0400,0x0561));//Original Attributes Sequence -->

		// logger.info("SeriesTagHandler.getSeriesTags [TEMP]\n"+sb.toString());
		return sb.toString();

	}

	private static InputStream getSeriesTagsAsStream(String seriesIUID)
	{
		MySqlQueries queries = MySqlInstance.getInstance().getMysqlQueries();
		return queries.getSeriesAttrsAsStream(seriesIUID);
	}

	private static String getSeriesTags(String seriesIUID) throws IOException, DicomException
	{
		StringBuilder sb = new StringBuilder();
		InputStream stream = getSeriesTagsAsStream(seriesIUID);

		AttributeList attributeList = new AttributeList();
		attributeList.read(new DicomInputStream(stream));

		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0021));// "Series Date"
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0031));// "Series Time"
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x0060));// "Modality"
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0008, 0x103e));// "Series Description"
		createDicomTagEntry(sb, attributeList, new AttributeTag(0x0020, 0x000e));// "Series Instance UID"

		// logger.info("SeriesTagHandler.getSeriesTags [TEMP]\n"+sb.toString());
		return sb.toString();
	}

	private static void createDicomTagEntry(StringBuilder sb, AttributeList attributeList, AttributeTag tag)
	{
		String key = null;
		try {
			DicomDictionary dictionary = AttributeList.getDictionary();
			key = dictionary.getFullNameFromTag(tag);
			Attribute attribute = attributeList.get(tag);
			if (attribute != null) {
				String value = attribute.getDelimitedStringValuesOrEmptyString();
				sb.append(key).append(": ").append(value).append("\n");
			} else {
				log.info("[Temp] didn't find tag: " + key);
			}

		} catch (Exception e) {
			if (key == null) {
				key = tag.toString();
			}
			log.warning("Failed to create DicomTag: " + key, e);
		}
	}
}
