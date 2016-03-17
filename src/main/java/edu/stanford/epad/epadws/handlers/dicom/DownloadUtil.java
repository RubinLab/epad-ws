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
package edu.stanford.epad.epadws.handlers.dicom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpException;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;

/**
 * @author Dev Gude
 *
 * Utility class with functions to create a zip file for subject/study/series/image dicoms and 
 * 	either stream them to client 
 *  or just return a json with a pointer to the zip file (client needs to delete after use) 
 */
public class DownloadUtil {
	
	private static final EPADLogger log = EPADLogger.getInstance();
	private static final String INTERNAL_EXCEPTION_MESSAGE = "Internal error from WADO";

	/**
	 * Method to download Subject dicoms
	 * 
	 * @author Emel Alkim  
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param subjectUIDs - download only these selected subjects
	 * @throws Exception
	 */
	public static void downloadProject(boolean stream, HttpServletResponse httpResponse, ProjectReference projectReference, String username, String sessionID, 
									EPADSearchFilter searchFilter, String subjectUIDs, boolean includeAIMs) throws Exception
	{
		log.info("Downloading project:" + projectReference.projectID + " stream:" + stream);
		Set<String> subjects = new HashSet<String>();
		if (subjectUIDs != null) {
			String[] ids = subjectUIDs.split(",");
			for (String id: ids)
				subjects.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		//TODO ml change the static numbers
		//subjects
		EPADSubjectList subjectList = epadOperations.getSubjectDescriptions(projectReference.projectID, username, sessionID, searchFilter, 1, 5000, "", false);
		for (EPADSubject subject: subjectList.ResultSet.Result)
		{
			if (!subjects.isEmpty() && !subjects.contains(subject.subjectID)) continue;
			File subjectDir = new File(downloadDir, "Subject-" + subject.subjectID);
			subjectDir.mkdirs();
			SubjectReference subjectReference = new SubjectReference(projectReference.projectID, subject.subjectID);
			//studies
			EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID, searchFilter);
			for (EPADStudy study: studyList.ResultSet.Result)
			{
				File studyDir = new File(subjectDir, "Study-" + study.studyUID);
				studyDir.mkdirs();
				
				StudyReference studyReference = new StudyReference(subjectReference.projectID, subjectReference.subjectID, study.studyUID);
				//series
				EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, searchFilter, false);
				for (EPADSeries series: seriesList.ResultSet.Result)
				{
					File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
					seriesDir.mkdirs();
					SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
					EPADImageList imageList = new EPADImageList();
					try {
						imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
					} catch (Exception x) {}
					for (EPADImage image: imageList.ResultSet.Result)
					{
						String name = image.imageUID + ".dcm";
						File imageFile = new File(seriesDir, name);
						fileNames.add("Subject-" + subjectReference.subjectID +"/Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
						FileOutputStream fos = null;
						try 
						{
							fos = new FileOutputStream(imageFile);
							String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
									+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
							performWADOQuery(queryString, fos, username, sessionID);
						}
						catch (Exception x)
						{
							log.warning("Error downloading image using wado");
						}
						finally 
						{
							if (fos != null) fos.close();
						}
					}
					
					//ml include aims copied from series
					if (includeAIMs)
					{
						EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
						for (EPADAIM aim: aimList.ResultSet.Result)
						{
							String name = "Aim_" + aim.aimID + ".xml";
							File aimFile = new File(seriesDir, name);
							fileNames.add("Subject-" + subjectReference.subjectID +"/Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
							FileWriter fw = null;
							try 
							{
								fw = new FileWriter(aimFile);
								fw.write(aim.xml);
							}
							catch (Exception x)
							{
								log.warning("Error writing aim file");
							}
							finally 
							{
								if (fw != null) fw.close();
							}
						}
					}
				}
			}
		}
		String zipName = "Project-" + projectReference.projectID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Project", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, projectReference.projectID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	
	/**
	 * Method to download Subject dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param studyUIDs - download only these selected studies
	 * @throws Exception
	 */
	public static void downloadSubject(boolean stream, HttpServletResponse httpResponse, SubjectReference subjectReference, String username, String sessionID, 
									EPADSearchFilter searchFilter, String studyUIDs, boolean includeAIMs) throws Exception
	{
		log.info("Downloading subject:" + subjectReference.subjectID + " stream:" + stream);
		Set<String> studies = new HashSet<String>();
		if (studyUIDs != null) {
			String[] ids = studyUIDs.split(",");
			for (String id: ids)
				studies.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID, searchFilter);
		for (EPADStudy study: studyList.ResultSet.Result)
		{
			if (!studies.isEmpty() && !studies.contains(study.studyUID)) continue;
			File studyDir = new File(downloadDir, "Study-" + study.studyUID);
			studyDir.mkdirs();
			StudyReference studyReference = new StudyReference(subjectReference.projectID, subjectReference.subjectID, study.studyUID);
			EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, searchFilter, false);
			for (EPADSeries series: seriesList.ResultSet.Result)
			{
				File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
				seriesDir.mkdirs();
				SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
				EPADImageList imageList = new EPADImageList();
				try {
					imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
				} catch (Exception x) {}
				for (EPADImage image: imageList.ResultSet.Result)
				{
					String name = image.imageUID + ".dcm";
					File imageFile = new File(seriesDir, name);
					fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
					FileOutputStream fos = null;
					try 
					{
						fos = new FileOutputStream(imageFile);
						String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
								+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
						performWADOQuery(queryString, fos, username, sessionID);
					}
					catch (Exception x)
					{
						log.warning("Error downloading image using wado");
					}
					finally 
					{
						if (fos != null) fos.close();
					}
				}
				
				//ml include aims copied from series
				if (includeAIMs)
				{
					EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
					for (EPADAIM aim: aimList.ResultSet.Result)
					{
						String name = "Aim_" + aim.aimID + ".xml";
						File aimFile = new File(seriesDir, name);
						fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
						FileWriter fw = null;
						try 
						{
							fw = new FileWriter(aimFile);
							fw.write(aim.xml);
						}
						catch (Exception x)
						{
							log.warning("Error writing aim file");
						}
						finally 
						{
							if (fw != null) fw.close();
						}
					}
				}
			}
		}
		String zipName = "Patient-" + subjectReference.subjectID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Patient", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, subjectReference.subjectID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download list of Study dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param studyUIDs - download only these selected studies
	 * @throws Exception
	 */
	public static void downloadStudies(boolean stream, HttpServletResponse httpResponse, String studyUIDs, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading studies:" + studyUIDs + " stream:" + stream);
		Set<String> studies = new HashSet<String>();
		if (studyUIDs != null) {
			String[] ids = studyUIDs.split(",");
			for (String id: ids)
				studies.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<String> fileNames = new ArrayList<String>();
		for (String studyUID: studies)
		{
			if (studyUID.length() == 0) continue;
			File studyDir = new File(downloadDir, "Study-" + studyUID);
			studyDir.mkdirs();
			
			StudyReference studyReference = new StudyReference(null, null, studyUID);
			EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
			if (study == null)
				throw new Exception("Study not found:" + studyReference.studyUID);
			studyReference = new StudyReference(null, study.patientID, studyUID);
			EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, new EPADSearchFilter(), false);
			for (EPADSeries series: seriesList.ResultSet.Result)
			{
				if (series.isNonDicomSeries) {
					File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
					seriesDir.mkdirs();
					List<EpadFile> files = projectOperations.getSeriesFiles(null, null, studyUID, series.seriesUID);
					for (EpadFile file: files) {
						String name = file.getName();
						File epadFile = new File(seriesDir, name);
						EPADFileUtils.copyFile(new File(EPADConfig.getEPADWebServerResourcesDir() +  getEpadFilePath(file)), epadFile);
					}
					continue;
				}
				if (series.isDSO) {
					try {
						List<EPADAIM> aims = databaseOperations.getAIMsByDSOSeries(series.seriesUID);
						boolean skip = true;
						for (EPADAIM aim: aims) {
							if (aim.userName.equals(username))
								skip = false;
						}
						if (skip)
							continue;
					} catch (Exception x) {};
				}
				File seriesDir = new File(studyDir, "Series-" + series.seriesUID);
				seriesDir.mkdirs();
				SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
				EPADImageList imageList = new EPADImageList();
				try {
					imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
				} catch (Exception x) {}
				for (EPADImage image: imageList.ResultSet.Result)
				{
					String name = image.imageUID + ".dcm";
					File imageFile = new File(seriesDir, name);
					fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
					FileOutputStream fos = null;
					try 
					{
						fos = new FileOutputStream(imageFile);
						String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
								+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
						performWADOQuery(queryString, fos, username, sessionID);
					}
					catch (Exception x)
					{
						log.warning("Error downloading image using wado");
					}
					finally 
					{
						if (fos != null) fos.close();
					}
				}
				if (includeAIMs)
				{
					EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
					for (EPADAIM aim: aimList.ResultSet.Result)
					{
						String name = "Aim_" + aim.aimID + ".xml";
						File aimFile = new File(seriesDir, name);
						fileNames.add("Study-" + studyReference.studyUID + "/Series-" + series.seriesUID + "/" + name);
						FileWriter fw = null;
						try 
						{
							fw = new FileWriter(aimFile);
							fw.write(aim.xml);
						}
						catch (Exception x)
						{
							log.warning("Error writing aim file");
						}
						finally 
						{
							if (fw != null) fw.close();
						}
					}
				}
			}
		}
		String ids = studyUIDs.replace(",","-");
		if (ids.length() > 128) ids = ids.substring(0, 128);
		String zipName = "Studies-" + ids + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Studies", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, studyUIDs);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download list of Files
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param studyUIDs - download only these selected studies
	 * @throws Exception
	 */
	public static void downloadFiles(HttpServletResponse httpResponse, String[] filePaths, String username) throws Exception
	{
		log.info("Downloading files:" + filePaths.length);
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		EpadDatabaseOperations databaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		List<String> fileNames = new ArrayList<String>();
		for (String filePath: filePaths)
		{
			if (filePath.startsWith("/")) filePath = filePath.substring(1);
			String[] parts = filePath.split("/");
			String projectID = null;
			String subjectID = null;
			String studyUID = null;
			String seriesUID = null;
			String fileName = null;
			if (parts.length < 4)
			{
				log.warning("Invalid filePath:" + filePath);
				continue;
			}
			projectID = parts[1];
			if (parts.length == 4)
			{
				fileName = parts[3];
			}
			else if (parts.length == 6)
			{
				subjectID = parts[3];
				fileName = parts[5];
			}
			else if (parts.length == 8)
			{
				subjectID = parts[3];
				studyUID = parts[5];
				fileName = parts[7];
			}
			else if (parts.length == 10)
			{
				subjectID = parts[3];
				studyUID = parts[5];
				seriesUID = parts[7];
				fileName = parts[9];
			}
			else
			{
				log.warning("Invalid filePath:" + filePath);
			}
			File projectDir = new File(downloadDir, "Project-" + projectID);
			projectDir.mkdirs();
			EpadFile file = projectOperations.getEpadFile(projectID, subjectID, studyUID, seriesUID, fileName);
			String name = file.getName();
			File epadFile = new File(projectDir, name);
			EPADFileUtils.copyFile(new File(EPADConfig.getEPADWebServerResourcesDir() +  getEpadFilePath(file)), epadFile);
			fileNames.add("Project-" + projectID + "/" + name);
		}
		String zipName = "EpadFiles-" + timestamp.format(new Date()) + ".zip";
		httpResponse.setContentType("application/zip");
		httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			out = httpResponse.getOutputStream();
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	private static String getEpadFilePath(EpadFile file)
	{
		String path = "files/"+ file.getRelativePath();
		String fileName = file.getId() + file.getExtension();
		if (path.endsWith("/"))
			return path + fileName;
		else
			return path + "/" + fileName;
	}

	/**
	 * Method to download Study dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param seriesUIDs - download only these selected series
	 * @throws Exception
	 */
	public static void downloadStudy(boolean stream, HttpServletResponse httpResponse, StudyReference studyReference, String username, String sessionID, EPADSearchFilter searchFilter, String seriesUIDs, boolean includeAIMs) throws Exception
	{
		log.info("Downloading study:" + studyReference.studyUID + " stream:" + stream);
		Set<String> seriesSet = new HashSet<String>();
		if (seriesUIDs != null) {
			String[] ids = seriesUIDs.split(",");
			for (String id: ids)
				seriesSet.add(id.trim());
		}
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID, searchFilter, false);
		int imageCount = 0;
		log.debug("Number series in study:" + seriesList.ResultSet.totalRecords);
		for (EPADSeries series: seriesList.ResultSet.Result)
		{
			if (!seriesSet.isEmpty() && !seriesSet.contains(series.seriesUID)) continue;
			File seriesDir = new File(downloadDir, "Series-"+ series.seriesUID);
			seriesDir.mkdirs();
			SeriesReference seriesReference = new SeriesReference(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
			if (series.isNonDicomSeries) {
				log.debug("Downloading files:" + series.seriesUID);
				EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
				List<EpadFile> files = projectOperations.getSeriesFiles(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, series.seriesUID);
				for (EpadFile file: files) {
					String name = file.getName();
					File epadFile = new File(seriesDir, name);
					EPADFileUtils.copyFile(new File(EPADConfig.getEPADWebServerResourcesDir() +  getEpadFilePath(file)), epadFile);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
				}
				continue;
			}
			EPADImageList imageList = new EPADImageList();
			try {
				imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
			} catch (Exception x) {}
			int i = 0;
			for (EPADImage image: imageList.ResultSet.Result)
			{
				imageCount++;
				String name = image.imageUID + ".dcm";
				File imageFile = new File(seriesDir, name);
				fileNames.add("Series-" + series.seriesUID + "/" + name);
				FileOutputStream fos = null;
				try 
				{
					fos = new FileOutputStream(imageFile);
					String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
							+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
					performWADOQuery(queryString, fos, username, sessionID);
				}
				catch (Exception x)
				{
					log.warning("Error downloading image using wado");
				}
				finally 
				{
					if (fos != null) fos.close();
				}
			}
			if (includeAIMs)
			{
				EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				for (EPADAIM aim: aimList.ResultSet.Result)
				{
					String name = "Aim_" + aim.aimID + ".xml";
					File aimFile = new File(seriesDir, name);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
					FileWriter fw = null;
					try 
					{
						fw = new FileWriter(aimFile);
						fw.write(aim.xml);
					}
					catch (Exception x)
					{
						log.warning("Error writing aim file");
					}
					finally 
					{
						if (fw != null) fw.close();
					}
				}
			}
		}
		String zipName = "Patient-" + studyReference.subjectID + "-Study-" + studyReference.studyUID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Study", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, studyReference.studyUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download dicoms from a list of series
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param seriesUIDs
	 * @throws Exception
	 */
	public static void downloadSeries(boolean stream, HttpServletResponse httpResponse, String seriesUIDs, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading seriesUIDs:" + seriesUIDs + " stream:" + stream);
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		List<String> fileNames = new ArrayList<String>();
		String[] seriesIDs = seriesUIDs.split(",");
		for (String seriesUID: seriesIDs)
		{
			if (seriesUID.trim().length() == 0) continue;
			File seriesDir = new File(downloadDir, "Series-"+ seriesUID);
			seriesDir.mkdirs();
			SeriesReference seriesReference = new SeriesReference(null, null, null, seriesUID);
			EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
			if (series.isNonDicomSeries) {
				EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
				List<EpadFile> files = projectOperations.getSeriesFiles(null, null, null, series.seriesUID);
				for (EpadFile file: files) {
					String name = file.getName();
					File epadFile = new File(seriesDir, name);
					EPADFileUtils.copyFile(new File(EPADConfig.getEPADWebServerResourcesDir() +  getEpadFilePath(file)), epadFile);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
				}
				continue;
			}
			seriesReference = new SeriesReference(null, series.patientID, series.studyUID, seriesUID);
			EPADImageList imageList = new EPADImageList();
			try {
				imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
			} catch (Exception x) {}
			int i = 0;
			for (EPADImage image: imageList.ResultSet.Result)
			{
				String name = image.imageUID + ".dcm";
				File imageFile = new File(seriesDir, name);
				fileNames.add("Series-" + series.seriesUID + "/" + name);
				FileOutputStream fos = null;
				try 
				{
					fos = new FileOutputStream(imageFile);
					String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
							+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
					performWADOQuery(queryString, fos, username, sessionID);
				}
				catch (Exception x)
				{
					log.warning("Error downloading image using wado");
				}
				finally 
				{
					if (fos != null) fos.close();
				}
			}
			if (includeAIMs)
			{
				EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				for (EPADAIM aim: aimList.ResultSet.Result)
				{
					String name = "Aim_" + aim.aimID + ".xml";
					File aimFile = new File(seriesDir, name);
					fileNames.add("Series-" + series.seriesUID + "/" + name);
					FileWriter fw = null;
					try 
					{
						fw = new FileWriter(aimFile);
						fw.write(aim.xml);
					}
					catch (Exception x)
					{
						log.warning("Error writing aim file");
					}
					finally 
					{
						if (fw != null) fw.close();
					}
				}
			}
		}
		String ids = seriesUIDs.replace(",","-");
		if (ids.length() > 128) ids = ids.substring(0, 128);
		String zipName = "Series-" + ids + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Series", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, seriesUIDs);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
		
	}

	/**
	 * Method to download Series dicoms
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadSeries(boolean stream, HttpServletResponse httpResponse, SeriesReference seriesReference, String username, String sessionID, boolean includeAIMs) throws Exception
	{
		log.info("Downloading series:" + seriesReference.seriesUID + " stream:" + stream);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
		String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/" + "temp" + Long.toString(System.currentTimeMillis());
		File downloadDir = new File(downloadDirPath);
		downloadDir.mkdirs();
		List<String> fileNames = new ArrayList<String>();
		if (series.isNonDicomSeries) {
			EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
			List<EpadFile> files = projectOperations.getSeriesFiles(null, null, null, series.seriesUID);
			for (EpadFile file: files) {
				String name = file.getName();
				File epadFile = new File(downloadDir, name);
				EPADFileUtils.copyFile(new File(EPADConfig.getEPADWebServerResourcesDir() +  getEpadFilePath(file)), epadFile);
				fileNames.add(name);
			}
		}
		else
		{
			EPADImageList imageList = new EPADImageList();
			try {
				imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, null);
			} catch (Exception x) {}
			for (EPADImage image: imageList.ResultSet.Result)
			{
				String name = image.imageUID + ".dcm";
				File imageFile = new File(downloadDir, name);
				fileNames.add(name);
				FileOutputStream fos = null;
				try 
				{
					fos = new FileOutputStream(imageFile);
					String queryString = "requestType=WADO&studyUID=" + seriesReference.studyUID 
							+ "&seriesUID=" + seriesReference.seriesUID + "&objectUID=" + image.imageUID + "&contentType=application/dicom";
					performWADOQuery(queryString, fos, username, sessionID);
				}
				catch (Exception x)
				{
					log.warning("Error downloading image using wado");
				}
				finally 
				{
					if (fos != null) fos.close();
				}
			}
		}
		if (includeAIMs)
		{
			EPADAIMList aimList = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
			for (EPADAIM aim: aimList.ResultSet.Result)
			{
				String name = "Aim_" + aim.aimID + ".xml";
				File aimFile = new File(downloadDir, name);
				fileNames.add(name);
				FileWriter fw = null;
				try 
				{
					fw = new FileWriter(aimFile);
					fw.write(aim.xml);
				}
				catch (Exception x)
				{
					log.warning("Error writing aim file");
				}
				finally 
				{
					if (fw != null) fw.close();
				}
			}
		}
		String zipName = "Patient-" + seriesReference.subjectID + "-Study-" + seriesReference.studyUID + "-Serie-" + seriesReference.seriesUID + ".zip";
		if (stream)
		{
			httpResponse.setContentType("application/zip");
			httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + zipName + "\"");
		}
		
		File zipFile = null;
		OutputStream out = null;
		try
		{
			if (stream)
			{
				out = httpResponse.getOutputStream();
			}
			else
			{
				zipFile = new File(downloadDir, zipName);
				out = new FileOutputStream(zipFile);
			}
		}
		catch (Exception e)
		{
			log.warning("Error getting output stream", e);
			throw e;
		}
		ZipAndStreamFiles(out, fileNames, downloadDirPath + "/");
		if (!stream)
		{
			File newZip = new File(EPADConfig.getEPADWebServerResourcesDir() + "download/", zipName);
			zipFile.renameTo(newZip);
			EPADFile epadFile = new EPADFile("", "", "", "", "", zipName, zipFile.length(), "Series", 
					formatDate(new Date()), "download/" + zipFile.getName(), true, seriesReference.seriesUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
		EPADFileUtils.deleteDirectoryAndContents(downloadDir);
	}

	/**
	 * Method to download file in resources folder
	 * 
	 * @param httpResponse
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadResource(String relativePath, HttpServletResponse httpResponse, String username, String sessionID) throws Exception
	{
		String resourcesPath = EPADConfig.getEPADWebServerResourcesDir();
		File file = new File(resourcesPath + "/" + relativePath);
		if (!file.exists())
			throw new Exception("Requested resource " + relativePath + " does not exist");
			
		if (file.isDirectory())
			throw new Exception("Requested resource " + relativePath + " is a folder");
				
		httpResponse.setContentType("application/zip");
		httpResponse.setHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
		
		OutputStream out = null;
		BufferedInputStream fr = null;
		try
		{
			out = httpResponse.getOutputStream();
			fr = new BufferedInputStream(new FileInputStream(file));

			byte buffer[] = new byte[0xffff];
			int b;
			while ((b = fr.read(buffer)) != -1)
				out.write(buffer, 0, b);
		}
		catch (Exception e)
		{
			log.warning("Error streaming file", e);
			throw e;
		}
		finally
		{
			if (fr != null)
				fr.close();
		}
	}
	
	/**
	 * Method to download a dicom
	 * 
	 * @param stream - true if file should stream, otherwise placed on disk to be picked (should be deleted after use)
	 * @param httpResponse
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadImage(boolean stream, HttpServletResponse httpResponse, ImageReference imageReference, String username, String sessionID, boolean dicom) throws Exception
	{
		String queryString = "requestType=WADO&studyUID=" + imageReference.studyUID 
				+ "&seriesUID=" + imageReference.seriesUID + "&objectUID=" + imageReference.imageUID;
		if (dicom)
			queryString = queryString + "&contentType=application/dicom";
		if (stream)
		{
			httpResponse.setContentType("application/octet-stream");
			ServletOutputStream responseStream = httpResponse.getOutputStream();
			performWADOQuery(queryString, responseStream, username, sessionID);
		}
		else
		{
			String downloadDirPath = EPADConfig.getEPADWebServerResourcesDir() + "download/";
			File downloadDir = new File(downloadDirPath);
			downloadDir.mkdirs();
			String imageName = imageReference.imageUID + ".dcm";
			File imageFile = new File(downloadDir, imageName);
			FileOutputStream fos = null;
			try 
			{
				fos = new FileOutputStream(imageFile);
				performWADOQuery(queryString, fos, username, sessionID);
			}
			catch (Exception x)
			{
				log.warning("Error downloading image using wado");
			}
			finally 
			{
				if (fos != null) fos.close();
			}
			EPADFile epadFile = new EPADFile("", "", "", "", "", imageName, imageFile.length(), "Image", 
					formatDate(new Date()), "download/" + imageFile.getName(), true, imageReference.imageUID);
			PrintWriter responseStream = httpResponse.getWriter();
			responseStream.append(epadFile.toJSON());
		}
	}
	
	/**
	 * Method to download a png
	 * 
	 * @param httpResponse
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	public static void downloadPNG(HttpServletResponse httpResponse, ImageReference imageReference, String username, String sessionID) throws Exception
	{
		String pngPath = EpadDatabase.getInstance().getEPADDatabaseOperations().getPNGLocation(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
		File file = new File(EPADConfig.getEPADWebServerResourcesDir() +  pngPath);
		if (!file.exists()) {
			throw new Exception("Image " + file.getAbsolutePath() + " does not exist");
		}
	    EPADFileUtils.downloadFile(null, httpResponse, file, imageReference.imageUID + ".png");
	}

	public static int performWADOQuery(String queryString, OutputStream outputStream, String username, String sessionID)
	{
		String wadoHost = EPADConfig.dcm4CheeServer;
		int wadoPort = EPADConfig.dcm4cheeServerWadoPort;
		String wadoBase = EPADConfig.wadoURLExtension;
		if (queryString.toLowerCase().indexOf("dicom") != -1)
		{
			log.info("User:" + username  + " host:" + EPADSessionOperations.getSessionHost(sessionID) 
					+ " Wado Request to download dicom:" + queryString);
		}
		String wadoURL = buildWADOURL(wadoHost, wadoPort, wadoBase, queryString);
		int statusCode;
		try {
			statusCode = HandlerUtil.streamGetResponse(wadoURL, outputStream, log);
			if (statusCode != HttpServletResponse.SC_OK)
				log.warning("Unexpected response " + statusCode + " to WADO request " + wadoURL);
		} catch (HttpException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		} catch (IOException e) {
			statusCode = HandlerUtil.internalErrorResponse(INTERNAL_EXCEPTION_MESSAGE, log);
		}
		return statusCode;
	}

	private static String buildWADOURL(String host, int port, String base, String queryString)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("http://").append(host);
		sb.append(":").append(port);
		sb.append(base);
		sb.append(queryString);
		return sb.toString();
	}

	public static boolean ZipAndStreamFiles(OutputStream out, List<String> fileNames, String dirPath)
	{

		File dir_file = new File(dirPath);
		int dir_l = dir_file.getAbsolutePath().length();
		ZipOutputStream zipout = new ZipOutputStream(out);
		zipout.setLevel(1);
		for (int i = 0; i < fileNames.size(); i++)
		{
			File f = (File) new File(dirPath + fileNames.get(i));
			if (f.canRead())
			{
				log.debug("Adding file: " + f.getAbsolutePath());
				try
				{
					zipout.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(dir_l + 1)));
				}
				catch (Exception e)
				{
					log.warning("Error adding to zip file", e);
					return false;
				}
				BufferedInputStream fr;
				try
				{
					fr = new BufferedInputStream(new FileInputStream(f));

					byte buffer[] = new byte[0xffff];
					int b;
					while ((b = fr.read(buffer)) != -1)
						zipout.write(buffer, 0, b);

					fr.close();
					zipout.closeEntry();

				}
				catch (Exception e)
				{
					log.warning("Error closing zip file", e);
					return false;
				}
			}
		}

		try
		{
			zipout.finish();
			out.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;

	}

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private static String formatDate(Date date)
	{
		if (date == null)
			return "";
		else
			return dateFormat.format(date);
	}

	static SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMddHHmm");

}
