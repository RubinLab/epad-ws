/*******************************************************************************
 * Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
 * BY CLICKING ON "ACCEPT," DOWNLOADING, OR OTHERWISE USING EPAD, YOU AGREE TO THE FOLLOWING TERMS AND CONDITIONS:
 * STANFORD ACADEMIC SOFTWARE SOURCE CODE LICENSE FOR
 * "ePAD Annotation Platform for Radiology Images"
 *
 * This Agreement covers contributions to and downloads from the ePAD project ("ePAD") maintained by The Board of Trustees 
 * of the Leland Stanford Junior University ("Stanford"). 
 *
 * *	Part A applies to downloads of ePAD source code and/or data from ePAD. 
 *
 * *	Part B applies to contributions of software and/or data to ePAD (including making revisions of or additions to code 
 * and/or data already in ePAD), which may include source or object code. 
 *
 * Your download, copying, modifying, displaying, distributing or use of any ePAD software and/or data from ePAD 
 * (collectively, the "Software") is subject to Part A. Your contribution of software and/or data to ePAD (including any 
 * that occurred prior to the first publication of this Agreement) is a "Contribution" subject to Part B. Both Parts A and 
 * B shall be governed by and construed in accordance with the laws of the State of California without regard to principles 
 * of conflicts of law. Any legal action involving this Agreement or the Research Program will be adjudicated in the State 
 * of California. This Agreement shall supersede and replace any license terms that you may have agreed to previously with 
 * respect to ePAD.
 *
 * PART A. DOWNLOADING AGREEMENT - LICENSE FROM STANFORD WITH RIGHT TO SUBLICENSE ("SOFTWARE LICENSE").
 * 1. As used in this Software License, "you" means the individual downloading and/or using, reproducing, modifying, 
 * displaying and/or distributing Software and the institution or entity which employs or is otherwise affiliated with you. 
 * Stanford  hereby grants you, with right to sublicense, with respect to Stanford's rights in the Software, a 
 * royalty-free, non-exclusive license to use, reproduce, make derivative works of, display and distribute the Software, 
 * provided that: (a) you adhere to all of the terms and conditions of this Software License; (b) in connection with any 
 * copy, distribution of, or sublicense of all or any portion of the Software, the terms and conditions in this Software 
 * License shall appear in and shall apply to such copy and such sublicense, including without limitation all source and 
 * executable forms and on any user documentation, prefaced with the following words: "All or portions of this licensed 
 * product  have been obtained under license from The Board of Trustees of the Leland Stanford Junior University. and are 
 * subject to the following terms and conditions" AND any user interface to the Software or the "About" information display 
 * in the Software will display the following: "Powered by ePAD http://epad.stanford.edu;" (c) you preserve and maintain 
 * all applicable attributions, copyright notices and licenses included in or applicable to the Software; (d) modified 
 * versions of the Software must be clearly identified and marked as such, and must not be misrepresented as being the 
 * original Software; and (e) you consider making, but are under no obligation to make, the source code of any of your 
 * modifications to the Software freely available to others on an open source basis.
 *
 * 2. The license granted in this Software License includes without limitation the right to (i) incorporate the Software 
 * into your proprietary programs (subject to any restrictions applicable to such programs), (ii) add your own copyright 
 * statement to your modifications of the Software, and (iii) provide additional or different license terms and conditions 
 * in your sublicenses of modifications of the Software; provided that in each case your use, reproduction or distribution 
 * of such modifications otherwise complies with the conditions stated in this Software License.
 * 3. This Software License does not grant any rights with respect to third party software, except those rights that 
 * Stanford has been authorized by a third party to grant to you, and accordingly you are solely responsible for (i) 
 * obtaining any permissions from third parties that you need to use, reproduce, make derivative works of, display and 
 * distribute the Software, and (ii) informing your sublicensees, including without limitation your end-users, of their 
 * obligations to secure any such required permissions.
 * 4. You agree that you will use the Software in compliance with all applicable laws, policies and regulations including, 
 * but not limited to, those applicable to Personal Health Information ("PHI") and subject to the Institutional Review 
 * Board requirements of the your institution, if applicable. Licensee acknowledges and agrees that the Software is not 
 * FDA-approved, is intended only for research, and may not be used for clinical treatment purposes. Any commercialization 
 * of the Software is at the sole risk of you and the party or parties engaged in such commercialization. You further agree 
 * to use, reproduce, make derivative works of, display and distribute the Software in compliance with all applicable 
 * governmental laws, regulations and orders, including without limitation those relating to export and import control.
 * 5. You or your institution, as applicable, will indemnify, hold harmless, and defend Stanford against any third party 
 * claim of any kind made against Stanford arising out of or related to the exercise of any rights granted under this 
 * Agreement, the provision of Software, or the breach of this Agreement. Stanford provides the Software AS IS and WITH ALL 
 * FAULTS.  Stanford makes no representations and extends no warranties of any kind, either express or implied.  Among 
 * other things, Stanford disclaims any express or implied warranty in the Software:
 * (a)  of merchantability, of fitness for a particular purpose,
 * (b)  of non-infringement or 
 * (c)  arising out of any course of dealing.
 *
 * Title and copyright to the Program and any associated documentation shall at all times remain with Stanford, and 
 * Licensee agrees to preserve same. Stanford reserves the right to license the Program at any time for a fee.
 * 6. None of the names, logos or trademarks of Stanford or any of Stanford's affiliates or any of the Contributors, or any 
 * funding agency, may be used to endorse or promote products produced in whole or in part by operation of the Software or 
 * derived from or based on the Software without specific prior written permission from the applicable party.
 * 7. Any use, reproduction or distribution of the Software which is not in accordance with this Software License shall 
 * automatically revoke all rights granted to you under this Software License and render Paragraphs 1 and 2 of this 
 * Software License null and void.
 * 8. This Software License does not grant any rights in or to any intellectual property owned by Stanford or any 
 * Contributor except those rights expressly granted hereunder.
 *
 * PART B. CONTRIBUTION AGREEMENT - LICENSE TO STANFORD WITH RIGHT TO SUBLICENSE ("CONTRIBUTION AGREEMENT").
 * 1. As used in this Contribution Agreement, "you" means an individual providing a Contribution to ePAD and the 
 * institution or entity which employs or is otherwise affiliated with you.
 * 2. This Contribution Agreement applies to all Contributions made to ePAD at any time. By making a Contribution you 
 * represent that: (i) you are legally authorized and entitled by ownership or license to make such Contribution and to 
 * grant all licenses granted in this Contribution Agreement with respect to such Contribution; (ii) if your Contribution 
 * includes any patient data, all such data is de-identified in accordance with U.S. confidentiality and security laws and 
 * requirements, including but not limited to the Health Insurance Portability and Accountability Act (HIPAA) and its 
 * regulations, and your disclosure of such data for the purposes contemplated by this Agreement is properly authorized and 
 * in compliance with all applicable laws and regulations; and (iii) you have preserved in the Contribution all applicable 
 * attributions, copyright notices and licenses for any third party software or data included in the Contribution.
 * 3. Except for the licenses you grant in this Agreement, you reserve all right, title and interest in your Contribution.
 * 4. You hereby grant to Stanford, with the right to sublicense, a perpetual, worldwide, non-exclusive, no charge, 
 * royalty-free, irrevocable license to use, reproduce, make derivative works of, display and distribute the Contribution. 
 * If your Contribution is protected by patent, you hereby grant to Stanford, with the right to sublicense, a perpetual, 
 * worldwide, non-exclusive, no-charge, royalty-free, irrevocable license under your interest in patent rights embodied in 
 * the Contribution, to make, have made, use, sell and otherwise transfer your Contribution, alone or in combination with 
 * ePAD or otherwise.
 * 5. You acknowledge and agree that Stanford ham may incorporate your Contribution into ePAD and may make your 
 * Contribution as incorporated available to members of the public on an open source basis under terms substantially in 
 * accordance with the Software License set forth in Part A of this Agreement. You further acknowledge and agree that 
 * Stanford shall have no liability arising in connection with claims resulting from your breach of any of the terms of 
 * this Agreement.
 * 6. YOU WARRANT THAT TO THE BEST OF YOUR KNOWLEDGE YOUR CONTRIBUTION DOES NOT CONTAIN ANY CODE OBTAINED BY YOU UNDER AN 
 * OPEN SOURCE LICENSE THAT REQUIRES OR PRESCRIBES DISTRBUTION OF DERIVATIVE WORKS UNDER SUCH OPEN SOURCE LICENSE. (By way 
 * of non-limiting example, you will not contribute any code obtained by you under the GNU General Public License or other 
 * so-called "reciprocal" license.)
 *******************************************************************************/
package edu.stanford.epad.epadws.queries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.pixelmed.DSOColorHelper;
import edu.stanford.epad.common.pixelmed.SegmentedProperty;
import edu.stanford.epad.common.pixelmed.SegmentedPropertyHelper;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudy;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudySearchType;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.processing.pipeline.task.DicomHeadersTask;

/**
 * @author martin
 */
public class Dcm4CheeQueries
{
	private static EPADLogger log = EPADLogger.getInstance();

	public static DCM4CHEEStudy getStudy(String studyUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		Map<String, String> dcm4CheeStudyData = dcm4CheeDatabaseOperations.studySearch(studyUID);

		if (!dcm4CheeStudyData.isEmpty())
			return extractDCM4CHEEStudyFromData(dcm4CheeStudyData);
		else
			return null;
	}

	public static DCM4CHEEStudyList getStudies(Set<String> studyUIDs)
	{
		DCM4CHEEStudyList dcm4CheeStudyList = new DCM4CHEEStudyList();

		for (String studyUID : studyUIDs) {
			DCM4CHEEStudy dcm4CheeStudy = getStudy(studyUID);
			if (dcm4CheeStudy != null)
				dcm4CheeStudyList.addDCM4CHEEStudy(dcm4CheeStudy);
		}

		return dcm4CheeStudyList;
	}

	public static int getNumberOfStudiesForPatient(String patientID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		return dcm4CheeDatabaseOperations.getNumberOfStudiesForPatient(patientID);
	}

	public static Set<String> getStudyUIDsForPatient(String patientID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		return dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);
	}

	public static int getNumberOfStudiesForPatients(Set<String> patientIDs)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();

		return dcm4CheeDatabaseOperations.getNumberOfStudiesForPatients(patientIDs);
	}

	/**
	 * Query the DCM4CHEE database and return a list of study descriptions.
	 * <p>
	 * The {@link DCM4CHEEStudySearchType} enum specifies the search type, e.g, patientName, patientID.
	 * 
	 */
	public static DCM4CHEEStudyList studySearch(DCM4CHEEStudySearchType searchType, String searchValue)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> dcm4CheeStudySearchResult = dcm4CheeDatabaseOperations.studySearch(searchType,
				searchValue);
		DCM4CHEEStudyList dcm4CheeStudyList = new DCM4CHEEStudyList();

		for (Map<String, String> dcm4CheeStudyData : dcm4CheeStudySearchResult) {
			DCM4CHEEStudy dcm4CheeStudy = extractDCM4CHEEStudyFromData(dcm4CheeStudyData);
			dcm4CheeStudyList.addDCM4CHEEStudy(dcm4CheeStudy);
		}
		return dcm4CheeStudyList;
	}

	/**
	 * Get all the series for a study from DCM4CHEE.
	 * 
	 * @param queryString String - query string which contains the study id. The query line looks like the following:
	 *          http://[ip:port]/search?searchType=series&studyUID=[studyUID].
	 * 
	 */
	public static DCM4CHEESeriesList getSeriesInStudy(String studyUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		List<Map<String, String>> series = dcm4CheeDatabaseOperations.getAllSeriesInStudy(studyUID);
		DCM4CHEESeriesList dcm4cheeSeriesList = new DCM4CHEESeriesList();
		for (Map<String, String> dcm4CheeSeriesData : series) {
			DCM4CHEESeries dcm4cheeSeries = extractDCM4CHEESeriesFromSeriesData(dcm4CheeSeriesData);
			dcm4cheeSeriesList.addDCM4CHEESeries(dcm4cheeSeries);
		}
		return dcm4cheeSeriesList;
	}

	public static DCM4CHEESeries getSeries(String seriesUID)
	{
		Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
				.getDcm4CheeDatabaseOperations();
		Map<String, String> dcm4CheeSeriesData = dcm4CheeDatabaseOperations.getSeriesData(seriesUID);

		if (!dcm4CheeSeriesData.isEmpty())
			return extractDCM4CHEESeriesFromSeriesData(dcm4CheeSeriesData);
		else {
			log.warning("Could not find series " + seriesUID + " in dcm4chee's database");
			return null;
		}
	}

	public static DICOMElementList getDICOMElementsFromWADO(String studyUID, String seriesUID, String imageUID)
	{
		return getDICOMElementsFromWADO(studyUID, seriesUID, imageUID,null);
	}
	public static DICOMElementList getDICOMElementsFromWADO(String studyUID, String seriesUID, String imageUID, SegmentedProperty catTypeProp)
	{
		String catCode="";
		String typeCode="";
		String color=null;
		DICOMElementList dicomElementList = new DICOMElementList();
		DICOMElementList dicomElementListNoSkip = new DICOMElementList();
		boolean skipThumbnail=false;
		try {
			File temporaryDICOMFile = File.createTempFile(imageUID, ".tmp");
			int wadoStatusCode = DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, imageUID, temporaryDICOMFile);
			if (wadoStatusCode == HttpServletResponse.SC_OK) {
				File tempTag = File.createTempFile(imageUID, "_tag.tmp");
				ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
				taskExecutor.execute(new DicomHeadersTask(seriesUID, temporaryDICOMFile, tempTag));
				taskExecutor.shutdown();
				try {
					taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
					BufferedReader tagReader = null;
					try {
						String dicomElementString;
						FileReader tagFileReader = new FileReader(tempTag.getAbsolutePath());
						tagReader = new BufferedReader(tagFileReader);
						skipThumbnail = false;
						String currentSequence = "";
						while ((dicomElementString = tagReader.readLine()) != null) {
							
									
							if (dicomElementString.contains("(0009,1110)"))  // hard code for now TODO:???
								skipThumbnail = true;
							if (dicomElementString.contains("(FFFE,E0DD)"))
								skipThumbnail = false;
							int sequence = dicomElementString.indexOf("SQ #-1");
							if (sequence != -1)
								currentSequence = dicomElementString.substring(sequence + 7);
							if (dicomElementString.contains("Sequence Delimitation Item"))
								currentSequence = "";
							DICOMElement dicomElement = decodeDICOMElementString(dicomElementString);
							DICOMElement dicomElementNoSkip = decodeDICOMElementString(dicomElementString);
							if (dicomElement != null) {
								if (!skipThumbnail) {
									dicomElement.parentSequenceName = currentSequence;
									dicomElementList.addDICOMElement(dicomElement);
									if (dicomElementString.contains("(0008,0100)")) {
										if (dicomElement.parentSequenceName!=null && dicomElement.parentSequenceName.equalsIgnoreCase("Segmented Property Category Code Sequence"))//category code
										{
											catCode=dicomElement.value.trim();
											log.info("cat code is "+catCode);
										}
										else if (dicomElement.parentSequenceName!=null && dicomElement.parentSequenceName.equalsIgnoreCase("Segmented Property Type Code Sequence"))//category code
										{
											typeCode=dicomElement.value.trim();
											log.info("type code is "+typeCode);
										}
									}
									if (dicomElementString.contains("(0062,000D)")) {
										color=dicomElement.value.trim();
										log.info("color is "+color);
										
									}
								} 
							//make a list with all the skip items
							//at the end if the skip is not closed then use this list
								else{
									log.warning("Warning: skip sequence. skipping " + dicomElementString);
									dicomElementNoSkip.parentSequenceName = currentSequence;
									dicomElementListNoSkip.addDICOMElement(dicomElementNoSkip);
								}
							} else {
								//too much log
//								 log.warning("Warning: could not decode DICOM element " + dicomElementString + "");
							}
						}
					} finally {
						IOUtils.closeQuietly(tagReader);
						try {
							temporaryDICOMFile.delete();
							tempTag.delete();
						} catch (Exception x) {};
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.warning("DICOM headers task for series " + seriesUID + " interrupted!");
				}
			} else {
				log.warning("Error invoking dcm4chee to get DICOM headers for series " + seriesUID + "; status code="
						+ wadoStatusCode);
			}
		} catch (IOException e) {
			log.warning("IOException retrieving DICOM headers for image " + imageUID + " in series " + seriesUID, e);
		}
		try{
			if (catTypeProp!=null && !catCode.equals("") && !typeCode.equals("")) {
				SegmentedPropertyHelper helper=new SegmentedPropertyHelper();
				SegmentedProperty prop= helper.getProperty(catCode, typeCode);
				if (prop!=null){
					catTypeProp.copyValuesFrom(prop);
				}
				else {
					log.info("Category-type pair not found");
				}
				if (color!=null){
					String[] colorStrA=color.split("\\\\");
					if (colorStrA.length==3){
						int[] colorIntA=new int[3];
						for (int i=0;i<colorStrA.length; i++)
							colorIntA[i]=Integer.parseInt(colorStrA[i]);
						int[] rgb=DSOColorHelper.scaledLab2Rgb(colorIntA);
						String colorText="rgb("+rgb[0]+";"+rgb[1]+";"+rgb[2]+")";
						catTypeProp.setDefColor(colorText);
					}
				}
			}
		} catch(Exception ex) {
			log.warning("Exception in getting category type ",ex);
		}
		
		if (skipThumbnail) {
			log.warning("End of skip not found returning noskip data. ");
			return dicomElementListNoSkip;
		}
		return dicomElementList;
	}

	private static DCM4CHEEStudy extractDCM4CHEEStudyFromData(Map<String, String> dcm4CheeStudyData)
	{
		String studyUID = getStringValueFromRow(dcm4CheeStudyData, "study_iuid");
		String patientName = getStringValueFromRow(dcm4CheeStudyData, "pat_name");
		String patientID = getStringValueFromRow(dcm4CheeStudyData, "pat_id");
		String examType = getStringValueFromRow(dcm4CheeStudyData, "modality");
		String dateAcquired = getStringValueFromRow(dcm4CheeStudyData, "study_datetime");
		int studyStatus = getIntegerFromRow(dcm4CheeStudyData, "study_status");
		int seriesCount = getIntegerFromRow(dcm4CheeStudyData, "number_series");
		String firstSeriesUID = getStringValueFromRow(dcm4CheeStudyData, "series_iuid");
		String firstSeriesDateAcquired = getStringValueFromRow(dcm4CheeStudyData, "pps_start");
		if (firstSeriesDateAcquired == null || firstSeriesDateAcquired.length() == 0)
			firstSeriesDateAcquired = getStringValueFromRow(dcm4CheeStudyData, "study_datetime");
		String studyAccessionNumber = getStringValueFromRow(dcm4CheeStudyData, "accession_no");
		int imagesCount = getIntegerFromRow(dcm4CheeStudyData, "sum_images");
		String stuidID = getStringValueFromRow(dcm4CheeStudyData, "study_id");
		String studyDescription = getStringValueFromRow(dcm4CheeStudyData, "study_desc");
		String physicianName = getStringValueFromRow(dcm4CheeStudyData, "ref_physician");
		String birthdate = getStringValueFromRow(dcm4CheeStudyData, "pat_birthdate");
		String createdTime = getStringValueFromRow(dcm4CheeStudyData, "created_time");
		String sex = getStringValueFromRow(dcm4CheeStudyData, "pat_sex");
		DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(studyUID, patientName, patientID, examType, dateAcquired,
				studyStatus, seriesCount, firstSeriesUID, firstSeriesDateAcquired, studyAccessionNumber, imagesCount, stuidID,
				studyDescription, physicianName, birthdate, sex, createdTime);
		return dcm4CheeStudy;
	}

	private static DCM4CHEESeries extractDCM4CHEESeriesFromSeriesData(Map<String, String> dcm4CheeSeriesData)
	{
		String studyUID = getStringValueFromRow(dcm4CheeSeriesData, "study_iuid");
		String seriesUID = getStringValueFromRow(dcm4CheeSeriesData, "series_iuid");
		String patientID = getStringValueFromRow(dcm4CheeSeriesData, "pat_id");
		String patientName = getStringValueFromRow(dcm4CheeSeriesData, "pat_name");
		String seriesDate = getStringValueFromRow(dcm4CheeSeriesData, "pps_start");
		if (seriesDate == null || seriesDate.length() == 0){
			//see if there is series date and time inside the blob
			seriesDate = getStringValueFromRow(dcm4CheeSeriesData, "series_datetime");
			if (seriesDate == null || seriesDate.length() == 0){
				//use the study date and time
				seriesDate = getStringValueFromRow(dcm4CheeSeriesData, "study_datetime");
			}
		}
		String examType = getStringValueFromRow(dcm4CheeSeriesData, "modality");
		String thumbnailURL = getStringValueFromRow(dcm4CheeSeriesData, "thumbnail_url");
		String seriesDescription = getStringValueFromRow(dcm4CheeSeriesData, "series_desc");
		int numberOfSeriesRelatedInstances = Integer.parseInt(getStringValueFromRow(dcm4CheeSeriesData, "num_instances"));
		int imagesInSeries = getIntegerFromRow(dcm4CheeSeriesData, "num_instances");
		int seriesStatus = getIntegerFromRow(dcm4CheeSeriesData, "series_status");
		String bodyPart = getStringValueFromRow(dcm4CheeSeriesData, "body_part");
		String institution = getStringValueFromRow(dcm4CheeSeriesData, "institution");
		String stationName = getStringValueFromRow(dcm4CheeSeriesData, "station_name");
		String department = getStringValueFromRow(dcm4CheeSeriesData, "department");
		String accessionNumber = getStringValueFromRow(dcm4CheeSeriesData, "accession_no");
		String createdTime = getTimestampFromRow(dcm4CheeSeriesData, "created_time");
		boolean isDSO = "SEG".equalsIgnoreCase(getStringValueFromRow(dcm4CheeSeriesData, "modality"));
		DCM4CHEESeries dcm4cheeSeries = new DCM4CHEESeries(studyUID, seriesUID, patientID, patientName, seriesDate,
				examType, thumbnailURL, seriesDescription, numberOfSeriesRelatedInstances, imagesInSeries, seriesStatus,
				bodyPart, institution, stationName, department, accessionNumber, createdTime, isDSO);
		return dcm4cheeSeries;

	}

	// TODO This code is very brittle. Rewrite to make more robust. Also ignores DICOM sequences.
	private static DICOMElement decodeDICOMElementString(String dicomElement)
	{
//		log.info("dicom element "+dicomElement);
		String[] fields = dicomElement.split(" ");

		int valueFieldStartIndex = valueFieldStartIndex(fields);
		if (valueFieldStartIndex != -1) {
			int valueFieldEndIndex = valueFieldEndIndex(fields);

			if (valueFieldEndIndex != -1 && ((valueFieldEndIndex - valueFieldStartIndex) < 10)) {
				String tagCode = extractTagCodeFromField(fields[0]);
				String value = stripBraces(assembleValue(fields, valueFieldStartIndex, valueFieldEndIndex));
				String tagName = assembleValue(fields, valueFieldEndIndex + 1, fields.length - 1);

				return new DICOMElement(tagCode, tagName, value);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static String extractTagCodeFromField(String field)
	{
		String subFields[] = field.split(":>*");

		if (subFields.length == 2) {
			return subFields[1];
		} else
			return "";
	}

	private static String assembleValue(String[] fields, int startIndex, int finishIndex)
	{
		String value = "";
		for (int i = startIndex; i <= finishIndex; i++) {
			if (i > startIndex)
				value += " ";
			value += fields[i];
		}
		return value;
	}

	private static int valueFieldStartIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].startsWith("["))
				return i;
		return -1;
	}

	private static int valueFieldEndIndex(String[] fields)
	{
		for (int i = 0; i < fields.length; i++)
			if (fields[i].endsWith("]"))
				return i;
		return -1;
	}

	private static String stripBraces(String valueField)
	{
		if (valueField.startsWith("[") && valueField.endsWith("]")) {
			return valueField.substring(1, valueField.length() - 1);
		} else {
			return "";
		}
	}

	private static String getStringValueFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return "";
		else
			return value;
	}

	private static String getTimestampFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return null;
		else {
			try {
				return value;
			} catch (IllegalArgumentException e) {
				log.warning("Invalid timestamp " + value + " in column " + columnName, e);
				return null;
			}
		}
	}

	// TODO Perhaps throw exception rather than returning -1 for missing or erroneous values.
	private static int getIntegerFromRow(Map<String, String> row, String columnName)
	{
		String value = row.get(columnName);

		if (value == null)
			return -1;
		else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				log.warning("expecting integer value in column " + columnName + " got " + value);
				return -1;
			}
		}
	}

	/**
	 * The DCM4CHEE MySql table returns the series date in the format: YYYY-MM-DD HH:MM:SS.sss
	 * 
	 * We want it in the format: YYYYMMDD
	 * 
	 * @param seriesDate YYYY-MM-DD HH:MM:SS.sss
	 * @return YYYYMMDD
	 */
	private static String reformatSeriesDate(String seriesDate)
	{
		try {
			if (seriesDate != null) {
				String[] parts = seriesDate.split(" ");
				return cleanString(parts[0], "-").replaceAll(" ", "");
			} else {
				return "00000000";
			}
		} catch (Exception e) {
			log.warning("cleanSeriesDate parse error for: " + seriesDate, e);
		}
		return seriesDate;
	}

	/**
	 * Remove a specific character from a from the string.
	 * 
	 * @param input String
	 * @param removeChar String character to remove
	 * @return String cleaned of character and trimmed.
	 */
	private static String cleanString(String input, String removeChar)
	{
		if (input == null)
			return null;
		if (removeChar == null)
			return input;

		return input.replaceAll(removeChar, " ").trim();
	}
}
