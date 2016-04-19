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
package edu.stanford.epad.epadws.handlers.core;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.DicomTagList;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADData;
import edu.stanford.epad.dtos.EPADDataList;
import edu.stanford.epad.dtos.EPADError;
import edu.stanford.epad.dtos.EPADEventLogList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADObjectList;
import edu.stanford.epad.dtos.EPADPlugin;
import edu.stanford.epad.dtos.EPADPluginList;
import edu.stanford.epad.dtos.EPADPluginParameterList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplateContainer;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.EPADWorklistSubjectList;
import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.dtos.RemotePACEntity;
import edu.stanford.epad.dtos.RemotePACEntityList;
import edu.stanford.epad.dtos.RemotePACList;
import edu.stanford.epad.dtos.RemotePACQueryConfigList;
import edu.stanford.epad.epadws.EPadWebServerVersion;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.handlers.dicom.DownloadUtil;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.RemotePACQuery;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.processing.pipeline.task.EpadStatisticsTask;
import edu.stanford.epad.epadws.processing.pipeline.task.TCIADownloadTask;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.PluginOperations;
import edu.stanford.epad.epadws.service.RemotePACService;
import edu.stanford.epad.epadws.service.TCIAService;
import edu.stanford.epad.epadws.service.UserProjectService;

/**
 * @author martin
 */
public class EPADGetHandler
{
	private static final String INTERNAL_ERROR_MESSAGE = "Internal error";
	private static final String BAD_GET_MESSAGE = "Invalid GET request - parameters are invalid";

	private static final EPADLogger log = EPADLogger.getInstance();

	/*
	 * Main class for handling GET rest calls using the epad v2 api.
	 * 
	 * Note: These long if/then/else statements looks terrible, they need to be replaced by something like jersey with annotations
	 * But there seems to be some problem using jersey with embedded jetty and multiple handlers - still need to solve that
	 * 
	 * Note: This class will soon become obsolete and be replaced by Spring Controllers
	 */

	static Map<String, String> lastRequest = new HashMap<String, String>();
	protected static int handleGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PrintWriter responseStream, String username, String sessionID)
	{
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		PluginOperations pluginOperations= PluginOperations.getInstance();
		String pathInfo = httpRequest.getPathInfo();
		String host = EPADSessionOperations.getSessionHost(sessionID);
		int statusCode;
		try {
			if (sessionID == null)
				throw new Exception("Invalid sessionID for user:" + username);
			EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(httpRequest);
			int start = getInt(httpRequest.getParameter("start"));
			if (start == 0) start = 1;
			int count = getInt(httpRequest.getParameter("count"));
			if (count == 0) count = 5000;
			long starttime = System.currentTimeMillis();
			String subjectUIDs = httpRequest.getParameter("subjectUIDs");
			String studyUIDs = httpRequest.getParameter("studyUIDs");
			String seriesUIDs = httpRequest.getParameter("seriesUIDs");
			if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_LIST, pathInfo)) {
				boolean annotationCount = false;
				if ("true".equalsIgnoreCase(httpRequest.getParameter("annotationCount")))
					annotationCount = true;
				boolean ignoreSystem = "false".equalsIgnoreCase(httpRequest.getParameter("system"));
				EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter, annotationCount, ignoreSystem);
				responseStream.append(projectList.toJSON());

				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT, pathInfo)) {
				boolean annotationCount = true;
				if ("false".equalsIgnoreCase(httpRequest.getParameter("annotationCount")))
					annotationCount = false;
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT, pathInfo);
				if (projectReference.projectID.equals(EPADConfig.xnatUploadProjectID))
					annotationCount = false;
				//ml added for project download
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnStream(httpRequest)) {
					DownloadUtil.downloadProject(true, httpResponse, projectReference, username, sessionID, searchFilter, subjectUIDs, includeAims);
				}else if (returnConnected(httpRequest)) { //ml connected data for deletion
					//need to get all subjects within and return their connected projects
					if (searchFilter ==null) {
						searchFilter= new EPADSearchFilter();
					}
					log.info("get subjects for project " + projectReference.projectID );
					EPADSubjectList subjects= epadOperations.getSubjectDescriptions(projectReference.projectID, username, sessionID, searchFilter, start, count, "", annotationCount);
					EPADProjectList allProjectList = new EPADProjectList();
					for (EPADSubject subj: subjects.ResultSet.Result) {
						EPADProjectList projectList= epadOperations.getProjectsForSubject( username,  sessionID,  searchFilter,  false, subj.subjectID);
						log.info("get projects for subject " + subj.subjectID + " size " + projectList.ResultSet.Result.size() );
						allProjectList.ResultSet.addAll(projectList.ResultSet);
					}
					
					log.info("removing project " + projectReference.projectID  );
					allProjectList.removeEPADProject(projectReference.projectID);
					
					responseStream.append(allProjectList.toJSON());
				} else {
					EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID, annotationCount);
				
					if (project != null) {
						log.info("Project aim count:" + project.numberOfAnnotations);
						responseStream.append(project.toJSON());
					} else
						throw new Exception("Project " + projectReference.projectID + " not found");
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.SUBJECT_LIST, pathInfo);
				if (false && host != null && host.contains("epad-dev")) {
					// For multiple duplicate requests
					long currSec = System.currentTimeMillis()/1000;
					String requestStr = projectReference.projectID + httpRequest.getQueryString() + currSec;
					if (requestStr.equals(lastRequest.get(host))) {
						throw new Exception("Duplicate requests from client, please stop multiple, duplicate requests:" + httpRequest.getPathInfo());
					}
					lastRequest.put(host, requestStr);
				}
				
				String sortField = httpRequest.getParameter("sortField");
				boolean unassignedOnly = "true".equalsIgnoreCase(httpRequest.getParameter("unassignedOnly"));
				boolean annotationCount = true;
				if ("false".equalsIgnoreCase(httpRequest.getParameter("annotationCount")))
					annotationCount = false;
				boolean annotationCountOnly = false;
				if ("true".equalsIgnoreCase(httpRequest.getParameter("annotationCountOnly")))
				{
					annotationCountOnly = true;
					annotationCount = true;
				}
				String worklist = httpRequest.getParameter("worklist");
				EPADSubjectList subjectList = null;
				if (worklist != null && worklist.trim().length() > 0)
				{
					epadOperations.getWorklistSubjectDescriptions(projectReference.projectID, username, worklist, searchFilter, sessionID, sortField);
				}
				else if (projectReference.projectID.equalsIgnoreCase(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) || (projectReference.projectID.equals(EPADConfig.xnatUploadProjectID) && unassignedOnly))
				{
					subjectList = epadOperations.getUnassignedSubjectDescriptions(username, sessionID, searchFilter);
				}
				else
				{
					subjectList = epadOperations.getSubjectDescriptions(projectReference.projectID, username,
						sessionID, searchFilter, start, count, sortField, annotationCount);
				}
				if (annotationCountOnly) // What a stupid request!
				{
					for (EPADSubject subject: subjectList.ResultSet.Result)
					{
						subject.examTypes = null;
						subject.insertDate = null;
						subject.insertUser = null;
						subject.subjectName = null;
						subject.projectID = null;
						subject.uri = null;
						subject.xnatID = null;
						subject.numberOfStudies = null;
					}
				}
				long endtime = System.currentTimeMillis();
				log.info("Returning " + subjectList.ResultSet.totalRecords + " subjects to client, took " + (endtime-starttime) + " msecs");
				responseStream.append(subjectList.toJSON());
				long resptime = System.currentTimeMillis();
				log.info("Time taken for write http response:" + (resptime-endtime) + " msecs");
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				//ml multiple subjects
				if (subjectReference.subjectID.contains(",") && returnStream(httpRequest) ) {
					subjectUIDs=subjectReference.subjectID;
					DownloadUtil.downloadSubjects(true, httpResponse, subjectReference.subjectID, username, sessionID, searchFilter, includeAims);

				}else if (returnConnected(httpRequest)) { //ml connected data for deletion
					log.info("get projects for subject " + subjectReference.subjectID );
					if (searchFilter ==null) {
						searchFilter= new EPADSearchFilter();
					}
					EPADProjectList projectList = epadOperations.getProjectsForSubject( username,  sessionID,  searchFilter,  false, subjectReference.subjectID);
					projectList.removeEPADProject(subjectReference.projectID);
					responseStream.append(projectList.toJSON());
	
				}else if (returnFile(httpRequest)) {
					DownloadUtil.downloadSubject(false, httpResponse, subjectReference, username, sessionID, searchFilter, studyUIDs, includeAims);
				} else if (returnStream(httpRequest)) {
					DownloadUtil.downloadSubject(true, httpResponse, subjectReference, username, sessionID, searchFilter, studyUIDs, includeAims);
				} else {
					EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
					if (subject != null) {
						log.info("subject aim count:" + subject.numberOfAnnotations);
						responseStream.append(subject.toJSON());
					} else {
						log.info("Subject " + subjectReference.subjectID + " not found");
						throw new Exception("Subject " + subjectReference.subjectID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.STUDY_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
						searchFilter);
				log.info("Returning " + studyList.ResultSet.totalRecords + " studies");
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnConnected(httpRequest)) { //ml connected data for deletion
					log.info("get projects for study " + studyReference.studyUID );
					if (searchFilter ==null) {
						searchFilter= new EPADSearchFilter();
					}
					EPADProjectList projectList = epadOperations.getProjectsForStudy( username,  sessionID,  searchFilter,  false, studyReference.studyUID);
					projectList.removeEPADProject(studyReference.projectID);
					responseStream.append(projectList.toJSON());
	
				}else if (returnFile(httpRequest)) {
					if (studyReference.studyUID.contains(","))
						DownloadUtil.downloadStudies(false, httpResponse, studyReference.studyUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadStudy(false, httpResponse, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
				} else if (returnStream(httpRequest)) {
					if (studyReference.studyUID.contains(","))
						DownloadUtil.downloadStudies(true, httpResponse, studyReference.studyUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadStudy(true, httpResponse, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
				} else {
					try {
					EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
					if (study != null) {
						responseStream.append(study.toJSON());
					} else {
						log.info("Study " + studyReference.studyUID + " not found");
//						throw new Exception("Study " + studyReference.studyUID + " not found");
						//ml error payload
						EPADError err=new EPADError(1000, username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, "" , "", -1, "Study not found", "Study " + studyReference.studyUID + " not found. Please check studyId");
						responseStream.append(err.toJSON());
						statusCode = HttpServletResponse.SC_NOT_FOUND;
					}
					}catch (Exception e) {
						EPADError err=new EPADError(1000, username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, "" , "", -1, "Study not found", "Study " + studyReference.studyUID + " not found. Please check studyId");
						responseStream.append(err.toJSON());
						statusCode = HttpServletResponse.SC_NOT_FOUND;
					}
				}
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_LIST, pathInfo)) {
				boolean filterDSO = "true".equalsIgnoreCase(httpRequest.getParameter("filterDSO"));
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.SERIES_LIST, pathInfo);
				if (studyReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
						searchFilter, filterDSO);
				responseStream.append(seriesList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnConnected(httpRequest)) { //ml connected data for deletion
					log.info("get projects for study " + seriesReference.studyUID );
					if (searchFilter ==null) {
						searchFilter= new EPADSearchFilter();
					}
					EPADProjectList projectList = epadOperations.getProjectsForStudy( username,  sessionID,  searchFilter,  false, seriesReference.studyUID);
					projectList.removeEPADProject(seriesReference.projectID);
					responseStream.append(projectList.toJSON());
	
				}else if (returnFile(httpRequest)) {
					if (seriesReference.seriesUID.contains(","))
						DownloadUtil.downloadSeries(false, httpResponse, seriesReference.seriesUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadSeries(false, httpResponse, seriesReference, username, sessionID, includeAims);
				} else if (returnStream(httpRequest)) {
					if (seriesReference.seriesUID.contains(","))
						DownloadUtil.downloadSeries(true, httpResponse, seriesReference.seriesUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadSeries(true, httpResponse, seriesReference, username, sessionID, includeAims);
				} else {
					EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
					if (series != null) {
						responseStream.append(series.toJSON());
					} else {
						log.info("Series " + seriesReference.seriesUID + " not found");
						throw new Exception("Series " + seriesReference.seriesUID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIESFILE_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.SERIESFILE_LIST, pathInfo);
				EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
						searchFilter, true);
				EPADFileList fileList = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter, true);
				List objects = new ArrayList();
				objects.add(seriesList);
				objects.add(fileList);
				responseStream.append(new Gson().toJson(objects));
//				List<EPADFile> files = epadOperations.getEPADFiles(studyReference, username, sessionID, searchFilter);
//				for (EPADSeries series: seriesList.ResultSet.Result)
//				{
//					EPADFile efile = new EPADFile(studyReference.projectID, studyReference.subjectID, series.patientID, studyReference.studyUID, series.seriesUID,
//							series.seriesUID, 0, FileType.SERIES.getName(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(series.seriesDate), "");
//					files.add(0, efile);
//				}
//				responseStream.append(new EPADFileList(files).toJSON());
				statusCode = HttpServletResponse.SC_OK;


			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.IMAGE_LIST, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				responseStream.append(imageList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE, pathInfo);
				if (returnFile(httpRequest)) {
					DownloadUtil.downloadImage(false, httpResponse, imageReference, username, sessionID, true);
				} else if (returnStream(httpRequest)) {
					DownloadUtil.downloadImage(true, httpResponse, imageReference, username, sessionID, true);
				} else if (returnPNG(httpRequest)) {
					DownloadUtil.downloadPNG(httpResponse, imageReference, username, sessionID);
				} else if (returnJPEG(httpRequest)) {
					DownloadUtil.downloadImage(true, httpResponse, imageReference, username, sessionID, false);
				} else {
					EPADImage image = null;
					String followupStudy = httpRequest.getParameter("followupStudy");
					if (followupStudy != null && followupStudy.length() > 0)
						image = epadOperations.getSameSliceFromNewStudy(username, imageReference, followupStudy, sessionID);
					else
						image = epadOperations.getImageDescription(imageReference, sessionID);
					if (image != null) {
						responseStream.append(image.toJSON());
					} else {
						log.info("Image " + imageReference.imageUID + " not found");
						throw new Exception("Image " + imageReference.imageUID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGEFILE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.IMAGEFILE_LIST, pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				EPADFileList fileList = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
				List objects = new ArrayList();
				objects.add(imageList);
				objects.add(fileList);
				responseStream.append(new Gson().toJson(objects));
//				List<EPADFile> files = epadOperations.getEPADFiles(seriesReference, username, sessionID, searchFilter);
//				for (EPADImage image: imageList.ResultSet.Result)
//				{
//					EPADFile efile = new EPADFile(seriesReference.projectID, seriesReference.subjectID, image.patientID, seriesReference.studyUID, seriesReference.seriesUID,
//							image.imageUID, 0, FileType.DICOM.getName(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(image.imageDate), image.lossyImage);
//					files.add(0, efile);
//				}
//				responseStream.append(new EPADFileList(files).toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.FRAME_LIST, pathInfo);
				if (imageReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
				responseStream.append(frameList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME, pathInfo);
				EPADFrame frame = epadOperations.getFrameDescription(frameReference, sessionID);
				if (frame != null) {
					responseStream.append(frame.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else {
					log.info("Image " + frameReference.imageUID + " frame " + frameReference.frameNumber + " not found");
					throw new Exception("Image " + frameReference.imageUID + " frame " + frameReference.frameNumber + " not found");
				}
				
			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnFile(httpRequest)) {
					DownloadUtil.downloadSubject(false, httpResponse, subjectReference, username, sessionID, searchFilter, studyUIDs, includeAims);
				} else if (returnStream(httpRequest)) {
					DownloadUtil.downloadSubject(true, httpResponse, subjectReference, username, sessionID, searchFilter, studyUIDs, includeAims);
				} else {
					EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
					if (subject != null) {
						responseStream.append(subject.toJSON());
					} else {
						log.info("Subject " + subjectReference.subjectID + " not found");
						throw new Exception("Subject " + subjectReference.subjectID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
						searchFilter);
				responseStream.append(studyList.toJSON());
				statusCode = HttpServletResponse.SC_OK;


				/**
				 * Studies routes. These short cuts are used when the invoker does not have a project or subject ID.
				 */
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnFile(httpRequest)) {
					if (studyReference.studyUID.contains(","))
						DownloadUtil.downloadStudies(false, httpResponse, studyReference.studyUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadStudy(false, httpResponse, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
				} else if (returnStream(httpRequest)) {
					if (studyReference.studyUID.contains(","))
						DownloadUtil.downloadStudies(true, httpResponse, studyReference.studyUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadStudy(true, httpResponse, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
				} else {
					EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
					if (study != null) {
						responseStream.append(study.toJSON());
					} else {
						log.info("Study " + studyReference.studyUID + " not found");
						throw new Exception("Study " + studyReference.studyUID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES, pathInfo);
				boolean includeAims = "true".equalsIgnoreCase(httpRequest.getParameter("includeAims"));
				if (returnFile(httpRequest)) {
					if (seriesReference.seriesUID.contains(","))
						DownloadUtil.downloadSeries(false, httpResponse, seriesReference.seriesUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadSeries(false, httpResponse, seriesReference, username, sessionID, includeAims);
				} else if (returnStream(httpRequest)) {
					if (seriesReference.seriesUID.contains(","))
						DownloadUtil.downloadSeries(true, httpResponse, seriesReference.seriesUID, username, sessionID, includeAims);
					else
						DownloadUtil.downloadSeries(true, httpResponse, seriesReference, username, sessionID, includeAims);
				} else {
					EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
					responseStream.append(series.toJSON());
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.IMAGE_LIST, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
				responseStream.append(imageList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE, pathInfo);
				if (returnFile(httpRequest)) {
					DownloadUtil.downloadImage(false, httpResponse, imageReference, username, sessionID, true);
				} else if (returnStream(httpRequest)) {
					DownloadUtil.downloadImage(true, httpResponse, imageReference, username, sessionID, true);
				} else if (returnPNG(httpRequest)) {
					DownloadUtil.downloadPNG(httpResponse, imageReference, username, sessionID);
				} else if (returnJPEG(httpRequest)) {
					DownloadUtil.downloadImage(true, httpResponse, imageReference, username, sessionID, false);
				} else {
					EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
					if (image != null) {
						responseStream.append(image.toJSON());
					} else {
						log.info("Image " + imageReference.imageUID + " not found");
						throw new Exception("Image " + imageReference.imageUID + " not found");
					}
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.FRAME_LIST, pathInfo);
				EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
				responseStream.append(frameList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

				/**
				 * New AIM-related routes.
				 */

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM_LIST, pathInfo);
				AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String projectID = projectReference.projectID;
				log.info("GET request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectID);
				EPADAIMList aims = null;
				if (aimSearchType != null)
					aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
				else
					aims = epadOperations.getProjectAIMDescriptions(projectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType) || AIMSearchType.AIM_QUERY.equals(aimSearchType))
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, aimSearchType, searchValue, username, sessionID);
					else
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					long starttime2 = System.currentTimeMillis();
					responseStream.append(aims.toJSON());
					long resptime = System.currentTimeMillis();
					log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
				}
				else if (returnJson(httpRequest))
				{
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType) || AIMSearchType.AIM_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, true);					
					else
						AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType) || AIMSearchType.JSON_QUERY.equals(aimSearchType))
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, false);					
					else
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_AIM, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.PROJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getProjectAIMDescription(projectReference, aimReference.aimID, username, sessionID);
				if (returnConnected(httpRequest)) { //ml
					EPADProjectList projectList = new EPADProjectList();
					log.info("project "+ aim.projectID + " username " +username);
					projectList.addEPADProject(epadOperations.getProjectDescription(new ProjectReference(aim.projectID), username, sessionID, false));
					String[] sharedProjs= aim.sharedProjects.split(",");
					for (String sharedProj: sharedProjs) {
						projectList.addEPADProject(epadOperations.getProjectDescription(new ProjectReference(sharedProj), username, sessionID, false));
						
					}
					for (EPADProject prj: projectList.ResultSet.Result) {
						log.info("project " + prj.id  );
					}
					log.info("removing project " + projectReference.projectID  );
					projectList.removeEPADProject(projectReference.projectID);
					responseStream.append(projectList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
					return statusCode;
					
				}
				
				if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
					username = null;
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, projectReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations
						.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, subjectReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM_LIST, pathInfo);
				if (studyReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.STUDY_AIM, pathInfo);
				EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, studyReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM_LIST, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = null;
				if ("true".equalsIgnoreCase(httpRequest.getParameter("includeStudyAims")))
					aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID, true);
				else
					aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs for count:" + aims.ResultSet.totalRecords);
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}

				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, seriesReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(ProjectsRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, imageReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM_LIST, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(ProjectsRouteTemplates.FRAME_AIM, pathInfo);
				EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, frameReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_LIST, pathInfo);
				EPADUserList users = epadOperations.getUserDescriptions(username, projectReference, sessionID);
				responseStream.append(users.toJSON());
				statusCode = HttpServletResponse.SC_OK;

//			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.USER_WORKLIST_SUBJECTS, pathInfo)) {
//				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.USER_WORKLIST_SUBJECTS, pathInfo);
//				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.USER_WORKLIST_SUBJECTS, pathInfo);
//				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
//				String subjectID = HandlerUtil.getTemplateParameter(templateMap, "subjectID");
//				String workListID = HandlerUtil.getTemplateParameter(templateMap, "workListID");
//				log.info("Project:" + projectReference.projectID + " reader:" + reader + " workListID:" + workListID);
//				EPADWorklistSubjectList wlsl = epadOperations.getWorkListSubjects(projectReference, reader, workListID);
//				responseStream.append(wlsl.toJSON());
//				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.STUDY_AIM, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(StudiesRouteTemplates.STUDY_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.STUDY_AIM, pathInfo);
				EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, studyReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES_AIM_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES_AIM_LIST, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}

				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.SERIES_AIM, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, seriesReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_AIM_LIST, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.IMAGE_AIM, pathInfo)) {
				ImageReference imageReference = ImageReference.extract(StudiesRouteTemplates.IMAGE_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.SERIES_AIM, pathInfo);
				EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, imageReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_AIM_LIST, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(StudiesRouteTemplates.FRAME_AIM_LIST, pathInfo);
				EPADAIMList aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(StudiesRouteTemplates.FRAME_AIM, pathInfo)) {
				FrameReference frameReference = FrameReference.extract(StudiesRouteTemplates.FRAME_AIM, pathInfo);
				AIMReference aimReference = AIMReference.extract(StudiesRouteTemplates.FRAME_AIM, pathInfo);
				EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, frameReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADAIMList aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(SubjectsRouteTemplates.SUBJECT_AIM, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(SubjectsRouteTemplates.SUBJECT_AIM_LIST, pathInfo);
				AIMReference aimReference = AIMReference.extract(SubjectsRouteTemplates.SUBJECT_AIM, pathInfo);
				EPADAIM aim = epadOperations.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
				if (returnSummary(httpRequest))
				{	
					responseStream.append(aim.toJSON());
				}
				else
				{
					AIMUtil.queryAIMImageAnnotations(responseStream, subjectReference.projectID, AIMSearchType.ANNOTATION_UID,
							aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIMS_LIST, pathInfo)) {
				AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(httpRequest);
				String searchValue = aimSearchType != null ? httpRequest.getParameter(aimSearchType.getName()) : null;
				String projectID = httpRequest.getParameter("projectID");
				String database = httpRequest.getParameter("database");
				String user = httpRequest.getParameter("user");
				boolean deletedAims = "true".equalsIgnoreCase(httpRequest.getParameter("deletedAIMs"));
				log.info("GET request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
						+ searchValue + ", project " + projectID + " deletedAIMs:" + deletedAims);
				EPADAIMList aims = null;
				if (!deletedAims)
					aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
				
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (database!=null && database.equalsIgnoreCase("AIME")){ //ml
					if (user!=null)
						username=user;
					if (returnSummary(httpRequest))
					{
						aims = AIMUtil.queryAIMImageAnnotationSummariesV4AIME(aimSearchType, searchValue, username, sessionID);
						responseStream.append(aims.toJSON());
					}else if (returnJson(httpRequest))
					{
						AIMUtil.queryAIMImageAnnotationsV4AIME(responseStream, aimSearchType, searchValue, username, sessionID, true);					
					}else {
						AIMUtil.queryAIMImageAnnotationsV4AIME(responseStream, aimSearchType, searchValue, username, sessionID, false);					
					}
				}
				else
				if (returnSummary(httpRequest))
				{
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType) || AIMSearchType.JSON_QUERY.equals(aimSearchType))
					{
						if (!deletedAims)
							aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, aimSearchType, searchValue, username, sessionID);
						else
							aims = AIMUtil.queryDeletedAIMImageAnnotationSummaries(aimSearchType, searchValue, username);
					}
					else
					{
						if (!deletedAims)
							aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
						else
							aims = AIMUtil.queryDeletedAIMImageAnnotationSummaries(aimSearchType, searchValue, username);
					}
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					if (AIMSearchType.JSON_QUERY.equals(aimSearchType))
					{
						AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, true);					
					}
					else
					{
						if (!deletedAims)
							AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
						else
							AIMUtil.queryDeletedAIMImageJsonAnnotation(responseStream, aimSearchType, searchValue, username, sessionID);
					}
				}
				else
				{
					if (AIMSearchType.AIM_QUERY.equals(aimSearchType))
					{
						if (!deletedAims)
							AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, aimSearchType, searchValue, username, sessionID, false);					
						else
							AIMUtil.queryDeletedAIMImageAnnotations(responseStream, aimSearchType, searchValue, username, sessionID);
					}
					else
					{
						if (!deletedAims)
							AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
						else
							AIMUtil.queryDeletedAIMImageAnnotations(responseStream, aimSearchType, searchValue, username, sessionID);
					}
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(AimsRouteTemplates.AIM, pathInfo)) {
				String version = httpRequest.getParameter("version");
				AIMReference aimReference = AIMReference.extract(AimsRouteTemplates.AIM, pathInfo);
				EPADAIM aim = epadOperations.getAIMDescription(aimReference.aimID, username, sessionID);
				//ml return not found status if not found
				if (aim == null) {
					return HttpServletResponse.SC_NOT_FOUND;
				}
					
				if (returnSummary(httpRequest))
				{	
					if ("all".equalsIgnoreCase(version))
					{
						EPADAIMList aims = AIMUtil.getAllVersionSummaries(aim);
						responseStream.append(aims.toJSON());
					}
					else if ("previous".equalsIgnoreCase(version))
					{
						EPADAIMList aims = AIMUtil.getPreviousVersionSummaries(aim);
						responseStream.append(aims.toJSON());
					}
					else if ("next".equalsIgnoreCase(version))
					{
						EPADAIMList aims = AIMUtil.getNextVersionSummaries(aim);
						responseStream.append(aims.toJSON());
					}
					else
						responseStream.append(aim.toJSON());
				}
				else if ("data".equals(httpRequest.getParameter("format")))
				{
					String templateName = httpRequest.getParameter("templateName");
					if (templateName == null || templateName.trim().length() == 0)
						throw new Exception("Invalid template name");
					String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
					responseStream.append(json);
				}
				else
				{
					if ("all".equalsIgnoreCase(version))
					{
						AIMUtil.returnAllVersions(responseStream, aim);
					}
					else if ("previous".equalsIgnoreCase(version))
					{
						AIMUtil.returnPreviousVersions(responseStream, aim);
					}
					else
						AIMUtil.queryAIMImageAnnotations(responseStream, null, AIMSearchType.ANNOTATION_UID,
								aim.aimID, username);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_LIST, pathInfo)) {
				boolean usage = "true".equalsIgnoreCase(httpRequest.getParameter("includeSystemUsage"));
				EPADUserList userlist = epadOperations.getUserDescriptions(username, sessionID, usage);
				responseStream.append(userlist.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String return_username = HandlerUtil.getTemplateParameter(templateMap, "username");
				boolean usage = "true".equalsIgnoreCase(httpRequest.getParameter("includeSystemUsage"));
				EPADUser user = epadOperations.getUserDescription(username, return_username, sessionID, usage);
				responseStream.append(user.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_SESSIONS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER, pathInfo);
				String user = HandlerUtil.getTemplateParameter(templateMap, "username");
				Collection<EPADSession> sessions = epadOperations.getCurrentSessions(user);
				responseStream.append(new Gson().toJson(sessions));
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_EVENTLOGS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_EVENTLOGS, pathInfo);
				String user = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADEventLogList logs = epadOperations.getEventLogs(username, user, start, count);
				responseStream.append(new Gson().toJson(logs));
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_TASKSTATUS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_TASKSTATUS, pathInfo);
				String user = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADObjectList tasks = epadOperations.getTaskStatuses(username, user);
				responseStream.append(new Gson().toJson(tasks));
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLISTS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLISTS, pathInfo);
				String user = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADWorklistList wll = epadOperations.getWorkListsForUser(username, user);
				responseStream.append(wll.toJSON());
				statusCode = HttpServletResponse.SC_OK;
				
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST, pathInfo);
				String user = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				EPADWorklist wl = epadOperations.getWorkListByID(username, user, workListID);
				responseStream.append(wl.toJSON());
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST_SUBJECTS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST_SUBJECTS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				log.info(" reader:" + reader + " workListID:" + workListID);
				EPADWorklistSubjectList wlsl = epadOperations.getWorkListSubjects(username, reader, workListID);
				responseStream.append(wlsl.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_WORKLIST_STUDIES, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_WORKLIST_STUDIES, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				String workListID = HandlerUtil.getTemplateParameter(templateMap, "worklistID");
				log.info(" reader:" + reader + " workListID:" + workListID);
				EPADWorklistStudyList wlsl = epadOperations.getWorkListStudies(username, reader, workListID);
				responseStream.append(wlsl.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWERS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWERS, pathInfo);
				String reader = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADUserList users = epadOperations.getReviewers(username, reader, sessionID);
				responseStream.append(users.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEES, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEES, pathInfo);
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
				EPADUserList users = epadOperations.getReviewees(username, reviewer, sessionID);
				responseStream.append(users.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE, pathInfo);
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "reviewee");
				EPADUserList users = epadOperations.getReviewees(username, reviewer, sessionID);
				boolean found = false;
				for (EPADUser user: users.ResultSet.Result)
				{
					if (user.username.equals(reviewee))
					{
						responseStream.append(user.toJSON());
						found = true;
					}
				}
				if (!found)
					throw new Exception("User " + reviewee + " is not in the list for this user");
				statusCode = HttpServletResponse.SC_OK;
			} else if (HandlerUtil.matchesTemplate(UsersRouteTemplates.USER_REVIEWEE_AIMS, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(UsersRouteTemplates.USER_REVIEWEE_AIMS, pathInfo);
				String reviewer = HandlerUtil.getTemplateParameter(templateMap, "username");
				String reviewee = HandlerUtil.getTemplateParameter(templateMap, "reviewee");
				EPADUserList users = epadOperations.getReviewees(username, reviewer, sessionID);
				boolean found = false;
				for (EPADUser user: users.ResultSet.Result)
				{
					if (user.username.equals(reviewee))
					{
						found = true;
					}
				}
				if (!found)
					throw new Exception("User " + reviewee + " is not in the list for this user");
				EPADAIMList aims = epadOperations.getAIMDescriptionsForUser(reviewee, sessionID);
				long dbtime = System.currentTimeMillis();
				log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
				if (returnSummary(httpRequest))
				{	
					aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, "admin", sessionID);	// He needs to see all				
					responseStream.append(aims.toJSON());
				}
				else if (returnJson(httpRequest))
				{
					AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, "admin", sessionID);					
				}
				else
				{
					AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, "admin", sessionID);					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE_LIST, pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(projectReference, username, sessionID, searchFilter, true);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE_LIST, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(subjectReference, username, sessionID, searchFilter, true);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE_LIST, pathInfo);
				if (studyReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter, true);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE_LIST, pathInfo);
				if (seriesReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				EPADFileList files = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
				responseStream.append(files.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PROJECT_FILE, pathInfo)) {
				ProjectReference projectReference = ProjectReference.extract(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.PROJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(projectReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}					
				statusCode = HttpServletResponse.SC_OK;
						
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo)) {
				SubjectReference subjectReference = SubjectReference.extract(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				if (subjectReference.subjectID.equals("null"))
					throw new Exception("Patient ID in rest call is null:" + pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SUBJECT_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(subjectReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.STUDY_FILE, pathInfo)) {
				StudyReference studyReference = StudyReference.extract(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.STUDY_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(studyReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;
	
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SERIES_FILE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				EPADFile file = epadOperations.getFileDescription(seriesReference, filename, username, sessionID);
				if (returnSummary(httpRequest)){
					responseStream.append(file.toJSON());
				} else {
					EPADFileUtils.downloadFile(httpRequest, httpResponse, new File(EPADConfig.getEPADWebServerResourcesDir()+file.path), file.fileName); 					
				}
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.SERIES_FILE_COMPARE, pathInfo)) {
				SeriesReference seriesReference = SeriesReference.extract(ProjectsRouteTemplates.SERIES_FILE_COMPARE, pathInfo);
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(ProjectsRouteTemplates.SERIES_FILE_COMPARE, pathInfo);
				String filename = HandlerUtil.getTemplateParameter(templateMap, "filename");
				if (filename == null || filename.trim().length() == 0)
					throw new Exception("Invalid filename");
				String filename2 = HandlerUtil.getTemplateParameter(templateMap, "filename2");
				if (filename2 == null || filename2.trim().length() == 0)
					throw new Exception("Missing second filename");
				EPADFile file1 = epadOperations.getFileDescription(seriesReference, filename, username, sessionID);
				EPADFile file2 = epadOperations.getFileDescription(seriesReference, filename2, username, sessionID);
				String results = DSOUtil.getNiftiDSOComparison(new File(EPADConfig.getEPADWebServerResourcesDir() + file1.path), new File(EPADConfig.getEPADWebServerResourcesDir() + file2.path));
				responseStream.append(results);
				statusCode = HttpServletResponse.SC_OK;
			
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PACS_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PACS_LIST, pathInfo);
				List<RemotePAC> pacs = RemotePACService.getInstance().getRemotePACs();
				RemotePACList pacList = new RemotePACList();
				for (RemotePAC pac: pacs)
					pacList.addRemotePAC(pac);
				responseStream.append(pacList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					responseStream.append(new Gson().toJson(pac));
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					statusCode = HttpServletResponse.SC_NOT_FOUND;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_ENTITY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_ENTITY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String patientNameFilter = httpRequest.getParameter("patientNameFilter");
				if (patientNameFilter == null) httpRequest.getParameter("patientName");
				if (patientNameFilter == null) patientNameFilter = "";
				String patientIDFilter = httpRequest.getParameter("patientIDFilter");
				if (patientIDFilter == null) patientIDFilter = httpRequest.getParameter("patientID");
				if (patientIDFilter == null) patientIDFilter = "";
				String studyIDFilter = httpRequest.getParameter("studyIDFilter");
				if (studyIDFilter == null) studyIDFilter = "";
				String studyDateFilter = httpRequest.getParameter("studyDateFilter");
				if (studyDateFilter == null) studyDateFilter = "";
				String[] tagGroup = httpRequest.getParameterValues("tagGroup");
				String[] tagElement = httpRequest.getParameterValues("tagElement");
				String[] tagValue = httpRequest.getParameterValues("tagValue");
				String[] tagType = httpRequest.getParameterValues("tagType");
				String modality = httpRequest.getParameter("modality");
				boolean studiesOnly = !"true".equalsIgnoreCase(httpRequest.getParameter("series"));
				if (tagGroup != null && httpRequest.getParameter("series") == null)
					studiesOnly = false;
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, 
							studyIDFilter, studyDateFilter, modality,
							tagGroup, tagElement, tagValue, tagType, false, studiesOnly);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					throw new Exception("Remote PAC " + pacid + " not found");

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_SUBJECT_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_SUBJECT_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String patientNameFilter = httpRequest.getParameter("patientNameFilter");
				if (patientNameFilter == null) patientNameFilter = httpRequest.getParameter("patientName");
				if (patientNameFilter == null) 
					patientNameFilter = "";
				else
					patientNameFilter = patientNameFilter + "*";
				String patientIDFilter = httpRequest.getParameter("patientIDFilter");
				if (patientIDFilter == null) patientIDFilter = httpRequest.getParameter("patientID");
				if (patientIDFilter == null) 
					patientIDFilter = "";
				else
					patientIDFilter = patientIDFilter + "*";
				String modality = httpRequest.getParameter("modality");
				String[] tagGroup = httpRequest.getParameterValues("tagGroup");
				String[] tagElement = httpRequest.getParameterValues("tagElement");
				String[] tagValue = httpRequest.getParameterValues("tagValue");
				String[] tagType = httpRequest.getParameterValues("tagType");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					if (!isTest(host) && patientNameFilter.length() == 0 && patientIDFilter.length() == 0 && (tagGroup == null || tagGroup.length == 0) && modality == null)
						patientIDFilter = "1*";  // Use as Default filter, otherwise a full query takes a too long and overloads the PAC
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, patientNameFilter, patientIDFilter, "", "", modality, tagGroup, tagElement, tagValue, tagType, true, true);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else if (pacid.startsWith(TCIAService.TCIA_PREFIX))
				{
					List<RemotePACEntity> entities = TCIAService.getInstance().getPatientsForCollection(pacid.substring(5));
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
				{
					throw new Exception("Remote PAC " + pacid + " not found");
				}

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_STUDY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_STUDY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String subjectid = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				String studyDateFilter = httpRequest.getParameter("studyDateFilter");
				if (studyDateFilter == null) studyDateFilter = "";
				String modality = httpRequest.getParameter("modality");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectid, "", studyDateFilter, modality, false, true);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else if (pacid.startsWith(TCIAService.TCIA_PREFIX))
				{
					List<RemotePACEntity> entities = TCIAService.getInstance().getStudiesForPatient(pacid.substring(5), subjectid);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					throw new Exception("Remote PAC " + pacid + " not found");
				
			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_SERIES_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_SERIES_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String subjectid = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				String studyid = HandlerUtil.getTemplateParameter(templateMap, "studyid");
				String modality = httpRequest.getParameter("modality");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacid);
				if (pac != null)
				{
					List<RemotePACEntity> entities = RemotePACService.getInstance().queryRemoteData(pac, "", subjectid, studyid, "", modality, false, false);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else if (pacid.startsWith(TCIAService.TCIA_PREFIX))
				{
					if (studyid.indexOf(":") != -1)
						studyid = studyid.substring(studyid.lastIndexOf(":")+1);
					List<RemotePACEntity> entities = TCIAService.getInstance().getSeriesForStudy(pacid.substring(5), subjectid, studyid);
					RemotePACEntityList entityList = new RemotePACEntityList();
					for (RemotePACEntity entity: entities)
						entityList.addRemotePACEntity(entity);
					responseStream.append(entityList.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					throw new Exception("Remote PAC " + pacid + " not found");

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_ENTITY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_ENTITY, pathInfo);
				String pacID = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacID.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String entityID = HandlerUtil.getTemplateParameter(templateMap, "entityid");
				String projectID = httpRequest.getParameter("projectID");
				RemotePAC pac = RemotePACService.getInstance().getRemotePAC(pacID);
				if (pac != null)
				{
					RemotePACService.getInstance().retrieveRemoteData(pac, entityID, projectID, username, sessionID);
					statusCode = HttpServletResponse.SC_OK;
				}
				else if (pacID.startsWith(TCIAService.TCIA_PREFIX))
				{
					String id = entityID;
					if (id.indexOf(":") != -1)
						id = entityID.substring(entityID.lastIndexOf(":")+1);
					if (entityID.indexOf("SUBJECT:") != -1)
					{
						(new Thread(new TCIADownloadTask(projectID, pacID.substring(TCIAService.TCIA_PREFIX.length()), id, null, username))).start();
						statusCode = HttpServletResponse.SC_OK;
					}
					else if (entityID.indexOf("STUDY:") != -1)
					{
						String[] ids = entityID.split(":");
						(new Thread(new TCIADownloadTask(projectID, pacID.substring(TCIAService.TCIA_PREFIX.length()), ids[ids.length-3], id, username))).start();
						statusCode = HttpServletResponse.SC_OK;
					}
					else
						statusCode = TCIAService.downloadSeriesFromTCIA(username, id, projectID);
				}
				else
					throw new Exception("Remote PAC " + pacID + " not found");

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY_LIST, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY_LIST, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				RemotePACService rps = RemotePACService.getInstance();
				List<RemotePACQuery> remoteQueries = null;
				if (pacid.equals("*"))
					remoteQueries =	rps.getAllQueries();
				else
					remoteQueries =	rps.getRemotePACQueries(pacid);
				RemotePACQueryConfigList queryList = new RemotePACQueryConfigList();
				for (RemotePACQuery query: remoteQueries)
				{
					queryList.addRemotePACQueryConfig(rps.getConfig(query));
				}
				responseStream.append(queryList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.PAC_QUERY, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(PACSRouteTemplates.PAC_QUERY, pathInfo);
				String pacid = HandlerUtil.getTemplateParameter(templateMap, "pacid");
				if (pacid.equalsIgnoreCase("null"))
					throw new Exception("PAC ID in rest call is null:" + pathInfo);
				String subject = HandlerUtil.getTemplateParameter(templateMap, "subjectid");
				RemotePACQuery remoteQuery = RemotePACService.getInstance().getRemotePACQuery(pacid, subject);
				if (remoteQuery != null)
				{
					responseStream.append(new Gson().toJson( RemotePACService.getInstance().getConfig(remoteQuery)));
					statusCode = HttpServletResponse.SC_OK;
				}
				else
					throw new Exception("Remote PAC " + pacid + " not found");

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.TAG_LIST, pathInfo)) {
				DicomTagList tagList = RemotePACService.getDicomTags();
				responseStream.append(tagList.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PACSRouteTemplates.TCIA_TRANSFER, pathInfo)) {
				String seriesUID = httpRequest.getParameter("seriesUID");
				String projectID = httpRequest.getParameter("projectID");
				if (seriesUID == null || seriesUID.trim().length() == 0)
					throw new Exception("Missing seriesUID in TCIA data transfer request");
				if (projectID == null || projectID.trim().length() == 0)
					throw new Exception("Missing projectID in TCIA data transfer request");
				//ml get status code from tcia service to see if it was successful
				statusCode = TCIAService.downloadSeriesFromTCIA(username, seriesUID, projectID);

			} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN_LIST, pathInfo)) { //ML
				
				EPADPluginList plugins = null;
				if (returnSummary(httpRequest)) 
					plugins = pluginOperations.getPluginSummaries(username, sessionID);
				else
					plugins = pluginOperations.getPluginDescriptions(username, sessionID);
				
				responseStream.append(plugins.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(PluginRouteTemplates.PLUGIN, pathInfo)) { //ML
								
				PluginReference pluginReference = PluginReference.extract(PluginRouteTemplates.PLUGIN, pathInfo);

				EPADPlugin plugin = pluginOperations.getPluginDescription(pluginReference.pluginID,username, sessionID);
				
				if (plugin != null) {
					responseStream.append(plugin.toJSON());
					statusCode = HttpServletResponse.SC_OK;
				} else
					throw new Exception("Plugin " + pluginReference.pluginID + " not found");


			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PLUGIN_LIST, pathInfo)) { //ML
				ProjectReference reference = ProjectReference.extract(ProjectsRouteTemplates.PLUGIN_LIST, pathInfo);
				EPADPluginList plugins = null;
				if (returnSummary(httpRequest)) 
					plugins = pluginOperations.getPluginSummariesForProject(reference.projectID, username, sessionID);
					
				else
					plugins = pluginOperations.getPluginDescriptionsForProject(reference.projectID, username, sessionID);

				responseStream.append(plugins.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.PARAMETER_LIST, pathInfo)) { //ML
				ProjectPluginReference reference = ProjectPluginReference.extract(ProjectsRouteTemplates.PARAMETER_LIST, pathInfo);
								
				EPADPluginParameterList parameters = pluginOperations.getParameterForPluginOfProject(reference.projectId, reference.pluginId);
				responseStream.append(parameters.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(ProjectsRouteTemplates.TEMPLATE_LIST, pathInfo)) {
				ProjectReference reference = ProjectReference.extract(ProjectsRouteTemplates.TEMPLATE_LIST, pathInfo);
				EPADTemplateContainerList templates = epadOperations.getTemplateDescriptions(reference.projectID, username, sessionID);
				if (templates.ResultSet.totalRecords == 0 && "true".equals(httpRequest.getParameter("includeSystemTemplates"))) {
					EPADTemplateContainerList systemplates = epadOperations.getSystemTemplateDescriptions(username, sessionID);
					for (EPADTemplateContainer template: systemplates.ResultSet.Result) {
						templates.addTemplate(template);
					}
				}
				responseStream.append(templates.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(TemplatesRouteTemplates.TEMPLATE_LIST, pathInfo)) {
				EPADTemplateContainerList templates = epadOperations.getTemplateDescriptions(username, sessionID);
				responseStream.append(templates.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(EPADsRouteTemplates.EPAD_LIST, pathInfo)) {
				EPADDataList epads = EpadDatabase.getInstance().getEPADDatabaseOperations().getEpadHostNames();
				responseStream.append(epads.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(EPADsRouteTemplates.EPAD_VERSION, pathInfo)) {
				EPADData data = new EPADData(EPADConfig.xnatServer, "Current Version", new EPadWebServerVersion().getVersion(),"Current version is the latest");
				if (EpadStatisticsTask.newEPADVersionAvailable)
				{
					data = new EPADData(EPADConfig.xnatServer, "Current Version", new EPadWebServerVersion().getVersion(),
							"New version " + EpadStatisticsTask.newEPADVersion + " is available");
					data.alert = true;
					boolean download = "true".equalsIgnoreCase(httpRequest.getParameter("downloadLatestEPAD"));
					if (download)
					{
						User user = DefaultEpadProjectOperations.getInstance().getUser(username);
						if (!user.isAdmin())
							throw new Exception("No permissions to download ePAD software");
						new EPadWebServerVersion().downloadEpadLatestVersion(username);
						data.status = EpadStatisticsTask.newEPADVersion + " has been downloaded, please restart the server";
					}
				}
				responseStream.append(data.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else if (HandlerUtil.matchesTemplate(EPADsRouteTemplates.EPAD_USAGE, pathInfo)) {
				Map<String, String> templateMap = HandlerUtil.getTemplateMap(EPADsRouteTemplates.EPAD_USAGE, pathInfo);
				String hostname = HandlerUtil.getTemplateParameter(templateMap, "hostname");
				boolean all = "true".equalsIgnoreCase(httpRequest.getParameter("all"));
				boolean byMonth = "true".equalsIgnoreCase(httpRequest.getParameter("byMonth"));
				boolean byYear = "true".equalsIgnoreCase(httpRequest.getParameter("byYear"));
				EPADUsageList eul = epadOperations.getUsage(username, hostname, byMonth, byYear, all);
				responseStream.append(eul.toJSON());
				statusCode = HttpServletResponse.SC_OK;

			} else
				statusCode = HandlerUtil.badRequestJSONResponse(BAD_GET_MESSAGE + ":" + pathInfo, responseStream, log);
		} catch (Throwable t) {
			t.printStackTrace();
			log.warning("Error handleget:", t);
			statusCode = HandlerUtil.internalErrorJSONResponse(INTERNAL_ERROR_MESSAGE, t, responseStream, log);
			if (t.getMessage() != null && t.getMessage().contains("not found"))
				statusCode = HttpServletResponse.SC_NOT_FOUND;
			log.info("ID:" + Thread.currentThread().getId() + " Error message to client:" + t.getMessage());
		}
		return statusCode;
	}

	private static boolean returnSummary(HttpServletRequest httpRequest)
	{
		String summary = httpRequest.getParameter("format");
		if (summary != null && summary.trim().equalsIgnoreCase("summary"))
			return true;
		else
			return false;
	}

	private static boolean returnConnected(HttpServletRequest httpRequest)
	{
		String format = httpRequest.getParameter("format");
		if (format != null && format.trim().equalsIgnoreCase("connectedSummary"))
			return true;
		else
			return false;
	}
	private static boolean returnFile(HttpServletRequest httpRequest)
	{
		String format = httpRequest.getParameter("format");
		if (format != null && format.trim().equalsIgnoreCase("file"))
			return true;
		else
			return false;
	}

	private static boolean returnStream(HttpServletRequest httpRequest)
	{
		String format = httpRequest.getParameter("format");
		if (format != null && format.trim().equalsIgnoreCase("stream"))
			return true;
		else
			return false;
	}

	private static boolean returnPNG(HttpServletRequest httpRequest)
	{
		String format = httpRequest.getParameter("format");
		if (format != null && format.trim().equalsIgnoreCase("png"))
			return true;
		else
			return false;
	}

	private static boolean returnJPEG(HttpServletRequest httpRequest)
	{
		String format = httpRequest.getParameter("format");
		if (format != null && format.trim().equalsIgnoreCase("jpeg"))
			return true;
		else
			return false;
	}

	private static boolean returnJson(HttpServletRequest httpRequest)
	{
		String summary = httpRequest.getParameter("format");
		if (summary != null && summary.trim().equalsIgnoreCase("json"))
			return true;
		else
			return false;
	}
	
	private static int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}
	
	static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private static Date getDate(String dateStr)
	{
		try
		{
			return dateformat.parse(dateStr);
		}
		catch (Exception x)
		{
			return null;
		}
	}

	static boolean isTest(String host)
	{
		if (host == null) return false;
		if (host.startsWith("epad-dev") || host.startsWith("epad-build"))
			return true;
		else
			return false;
	}
}
