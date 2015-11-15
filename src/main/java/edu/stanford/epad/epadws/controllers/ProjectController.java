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
package edu.stanford.epad.epadws.controllers;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADMessage;
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
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.controllers.exceptions.NotFoundException;
import edu.stanford.epad.epadws.handlers.HandlerUtil;
import edu.stanford.epad.epadws.handlers.core.AIMReference;
import edu.stanford.epad.epadws.handlers.core.AimsRouteTemplates;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilterBuilder;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.ProjectsRouteTemplates;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.handlers.dicom.DownloadUtil;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADProjectList getEPADProjects(
											@RequestParam(value="annotationCount", required = false, defaultValue = "false") boolean annotationCount,
											@RequestParam(value="system", required = false, defaultValue = "true") boolean system,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Getting project descriptions");
		EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter, annotationCount, !system); 
		log.info("Number of projects:" + projectList.ResultSet.totalRecords);
		return projectList;
	}
	 
	@RequestMapping(value = "/{projectID:.+}", method = RequestMethod.GET)
	public EPADProject getEPADProject( 
										@RequestParam(value="annotationCount", required = false, defaultValue = "true") boolean annotationCount,
										@PathVariable String projectID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID, annotationCount);
		if (project == null)
			throw new NotFoundException("Project " + projectID + " not found");
		return project;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/", method = RequestMethod.GET)
	public EPADSubjectList getEPADProjectSubjects( 
											@PathVariable String projectID,
											@RequestParam(value="start", defaultValue = "0") int start,
											@RequestParam(value="count", defaultValue = "0") int count,
											@RequestParam(value="sortField", defaultValue = "name") String sortField,
											@RequestParam(value="annotationCount", defaultValue = "true") boolean annotationCount,
											@RequestParam(value="annotationCountOnly", defaultValue = "true") boolean annotationCountOnly,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubjectList subjectList = epadOperations.getSubjectDescriptions(projectID, username, sessionID, searchFilter, start, count, sortField, annotationCount);
		return subjectList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID:.+}", method = RequestMethod.GET)
	public EPADSubject getEPADProjectSubject( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		if (subject == null)
			throw new NotFoundException("Subject " + subjectID + " not found in project " + projectID);
		return subject;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/", method = RequestMethod.GET)
	public EPADStudyList getEPADProjectStudies( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
		searchFilter);
		return studyList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID:.+}", method = RequestMethod.GET)
	public void getEPADProjectStudy( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											@RequestParam(value="seriesUIDs", required = false) String seriesUIDs, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		if ("file".equals(format)) {
			if (studyReference.studyUID.contains(","))
				DownloadUtil.downloadStudies(false, response, studyReference.studyUID, username, sessionID, includeAims);
			else
				DownloadUtil.downloadStudy(false, response, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
		} else if ("stream".equals(format)) {
			if (studyReference.studyUID.contains(","))
				DownloadUtil.downloadStudies(true, response, studyReference.studyUID, username, sessionID, includeAims);
			else
				DownloadUtil.downloadStudy(true, response, studyReference, username, sessionID, searchFilter, seriesUIDs, includeAims);
		} else {
			PrintWriter responseStream = response.getWriter();
			response.setContentType("application/json");
			EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
			if (study == null)
				throw new NotFoundException("Study " + studyUID + " not found in project " + projectID + " for subject:" + subjectID);
			responseStream.append(study.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/", method = RequestMethod.GET)
	public EPADSeriesList getEPADProjectSerieses(
												@RequestParam(value="filterDSO", defaultValue="false") boolean filterDSO,
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
		searchFilter, filterDSO);
		return seriesList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID:.+}", method = RequestMethod.GET)
	public void getEPADProjectSeries( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		if ("file".equals(format)) {
			if (seriesReference.seriesUID.contains(","))
				DownloadUtil.downloadSeries(false, response, seriesReference.seriesUID, username, sessionID, includeAims);
			else
				DownloadUtil.downloadSeries(false, response, seriesReference, username, sessionID, includeAims);
		} else if ("stream".equals(format)) {
			if (seriesReference.seriesUID.contains(","))
				DownloadUtil.downloadSeries(true, response, seriesReference.seriesUID, username, sessionID, includeAims);
			else
				DownloadUtil.downloadSeries(true, response, seriesReference, username, sessionID, includeAims);
		} else {
			PrintWriter responseStream = response.getWriter();
			response.setContentType("application/json");
			EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
			if (series == null)
				throw new NotFoundException("Series " + seriesUID + " not found in project " + projectID + " for subject:" + subjectID + " and study:" + studyUID);
			responseStream.append(series.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/", method = RequestMethod.GET)
	public EPADImageList getEPADProjectImages(
											@RequestParam(value="filterDSO", defaultValue="false") boolean filterDSO,
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
		return imageList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID:.+}", method = RequestMethod.GET)
	public void getEPADProjectImage( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		if ("file".equalsIgnoreCase(format)) {
			DownloadUtil.downloadImage(false, response, imageReference, username, sessionID, true);
		} else if ("stream".equalsIgnoreCase(format)) {
			DownloadUtil.downloadImage(true, response, imageReference, username, sessionID, true);
		} else if ("png".equalsIgnoreCase(format)) {
			DownloadUtil.downloadPNG(response, imageReference, username, sessionID);
		} else if ("jpeg".equalsIgnoreCase(format)) {
			DownloadUtil.downloadImage(true, response, imageReference, username, sessionID, false);
		} else {
			PrintWriter responseStream = response.getWriter();
			response.setContentType("application/json");
			EpadOperations epadOperations = DefaultEpadOperations.getInstance();
			EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
			if (image == null)
				throw new NotFoundException("Image " + imageUID + " for Series " + seriesUID + " not found in project " + projectID + " for subject:" + subjectID + " and study:" + studyUID);
			responseStream.append(image.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/", method = RequestMethod.GET)
	public EPADFrameList getEPADProjectFrames(
											@RequestParam(value="filterDSO",defaultValue="false") boolean filterDSO,
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
		return frameList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}", method = RequestMethod.GET)
	public EPADFrame getEPADProjectFrame( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											@PathVariable Integer frameNo,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNo);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFrame frame = epadOperations.getFrameDescription(frameReference, sessionID);
		if (frame == null)
			throw new NotFoundException("Frame " + frame + " for Image " + imageUID + " for Series " + seriesUID + " not found in project " + projectID + " for subject:" + subjectID + " and study:" + studyUID);
		return frame;
	}

	@RequestMapping(value = "/{projectID}/aims/", method = RequestMethod.GET)
	public void getEPADProjectAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
										@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		AIMSearchType aimSearchType = AIMUtil.getAIMSearchType(request);
		String searchValue = aimSearchType != null ? request.getParameter(aimSearchType.getName()) : null;
		log.info("GET request for AIMs from user " + username + "; query type is " + aimSearchType + ", value "
				+ searchValue + ", project " + projectID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		if (aimSearchType != null)
			aims = epadOperations.getAIMDescriptions(projectID, aimSearchType, searchValue, username, sessionID, start, count);
		else
			aims = epadOperations.getProjectAIMDescriptions(projectReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
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
		else if ("json".equalsIgnoreCase(format))
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
	}

	@RequestMapping(value = "/{projectID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADProjectAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		ProjectReference projectReference = new ProjectReference(projectID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getProjectAIMDescription(projectReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/aims/", method = RequestMethod.GET)
	public void getEPADSubjectAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		aims = epadOperations.getSubjectAIMDescriptions(subjectReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADSubjectAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getSubjectAIMDescription(subjectReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/aims/", method = RequestMethod.GET)
	public void getEPADStudyAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		aims = epadOperations.getStudyAIMDescriptions(studyReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADStudyAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/aims/", method = RequestMethod.GET)
	public void getEPADSeriesAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		aims = epadOperations.getSeriesAIMDescriptions(seriesReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADSeriesAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/", method = RequestMethod.GET)
	public void getEPADImageAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		aims = epadOperations.getImageAIMDescriptions(imageReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADImageAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}	


	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/", method = RequestMethod.GET)
	public void getEPADFrameAims( 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
									@PathVariable int frameNo,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNo);
		EPADAIMList aims = null;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		aims = epadOperations.getFrameAIMDescriptions(frameReference, username, sessionID);
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADFrameAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
									@PathVariable String aimID,
									@PathVariable int frameNo,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNo);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found in project " + projectID);
		if (!UserProjectService.isCollaborator(sessionID, username, aim.projectID))
			username = null;
		long dbtime = System.currentTimeMillis();
		log.info("Time taken for AIM database query:" + (dbtime-starttime) + " msecs");
		PrintWriter responseStream = response.getWriter();
		response.setContentType("application/json");
		if ("summary".equalsIgnoreCase(format))
		{
			aims = AIMUtil.queryAIMImageAnnotationSummariesV4(aims, username, sessionID);					
			long starttime2 = System.currentTimeMillis();
			responseStream.append(aims.toJSON());
			long resptime = System.currentTimeMillis();
			log.info("Time taken for write http response:" + (resptime-starttime2) + " msecs");
		}
		else if ("json".equalsIgnoreCase(format))
		{
			AIMUtil.queryAIMImageJsonAnnotations(responseStream, aims, username, sessionID);					
		}
		else if ("data".equals(request.getParameter("format")))
		{
			String templateName = request.getParameter("templateName");
			if (templateName == null || templateName.trim().length() == 0)
				throw new Exception("Invalid template name");
			String json = AIMUtil.readPlugInData(aim, templateName, sessionID);
			responseStream.append(json);
		}
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}	
	 
	@RequestMapping(value = "/{projectID}/users/", method = RequestMethod.GET)
	public EPADUserList getEPADProjectUsers( 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUserList users = epadOperations.getUserDescriptions(username, projectReference, sessionID);
		return users;
	}
	 
	@RequestMapping(value = "/{projectID}/templates/", method = RequestMethod.GET)
	public EPADTemplateContainerList getEPADProjectTemplates( 
											@PathVariable String projectID,
											@RequestParam(value="includeSystemTemplates", required = false) boolean includeSystemTemplates, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADTemplateContainerList templates = epadOperations.getTemplateDescriptions(projectReference.projectID, username, sessionID);
		if (includeSystemTemplates) {
			EPADTemplateContainerList systemplates = epadOperations.getSystemTemplateDescriptions(username, sessionID);
			for (EPADTemplateContainer template: systemplates.ResultSet.Result) {
				templates.addTemplate(template);
			}
		}
		return templates;
	}
	 
	@RequestMapping(value = "/{projectID}/templates/{templatename:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void updateProjectTemplate( 
			@PathVariable String projectID,
			@PathVariable String templatename,
			@RequestParam(value="enable", required = false) String enable, 
			@RequestParam(value="addToProject", required = false) String addToProject, 
			@RequestParam(value="removeFromProject", required = false) String removeFromProject, 
			HttpServletRequest request, 
	        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		if (enable != null)
		{
			EpadFile efile = projectOperations.getEpadFile(projectReference.projectID, null, null, null, templatename);
			if (efile != null && "true".equalsIgnoreCase(enable))
			{
				projectOperations.enableFile(username, projectReference.projectID, null, null, null, templatename);
			}
			else if (efile != null && "false".equalsIgnoreCase(enable))
			{	
				projectOperations.disableFile(username, projectReference.projectID, null, null, null, templatename);
			}
			efile = projectOperations.getEpadFile(EPADConfig.xnatUploadProjectID, null, null, null, templatename);
			if (efile != null && "true".equalsIgnoreCase(enable))
			{
				projectOperations.enableTemplate(username, projectReference.projectID, null, null, null, templatename);
			}
			else if (efile != null && "false".equalsIgnoreCase(enable))
			{	
				projectOperations.disableTemplate(username, projectReference.projectID, null, null, null, templatename);
			}
		}
		else if (addToProject != null)
		{
			Project project = projectOperations.getProject(addToProject);
			if (project == null)
				throw new Exception("Project " + addToProject + " not found");
			EpadFile efile = projectOperations.getEpadFile(projectReference.projectID, null, null, null, templatename);
			if (efile != null)
			{
				projectOperations.linkFileToProject(username, project, efile);
			}
		}
		else if (removeFromProject != null)
		{
			Project project = projectOperations.getProject(removeFromProject);
			if (project == null)
				throw new Exception("Project " + removeFromProject + " not found");
			EpadFile efile = projectOperations.getEpadFile(projectReference.projectID, null, null, null, templatename);
			if (efile != null)
			{
				projectOperations.unlinkFileFromProject(username, project, efile);
			}
		}
		else
		{
			File uploadedFile = HandlerUtil.getUploadedFile(request);
			if (uploadedFile != null)
			{
				EpadOperations epadOperations = DefaultEpadOperations.getInstance();
				epadOperations.createFile(username, projectReference, uploadedFile, "", FileType.TEMPLATE.getName(), sessionID);
			}
		}
	}
	 
	@RequestMapping(value = "/{projectID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADProjectFiles( 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(projectReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADSubjectFiles( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(subjectReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADStudyFiles( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADSeriesFiles( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADProjectFile( 
											@PathVariable String projectID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile file = epadOperations.getFileDescription(projectReference, filename, username, sessionID);
		return file;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADSubjectFile( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(subjectReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADStudyFile( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(studyReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADSeriesFile( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(seriesReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADProject( 
											@PathVariable String projectID,
											@RequestParam(value="projectName", required=true) String projectName,
											@RequestParam(value="projectDescription", required=true) String projectDescription,
											@RequestParam(value="defaultTemplate", required=false) String defaultTemplate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID, false);
		if (project != null) {
			statusCode = epadOperations.updateProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID);
		} else {
			statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID);
		}
		log.warning("Create/Modify project, status:" + statusCode);
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSubject( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@RequestParam(value="subjectName", required=true) String subjectName,
										@RequestParam(value="gender") String gender,
										@RequestParam(value="dob") String dob,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		int statusCode = 0;
		if (subject != null) {
			statusCode = epadOperations.updateSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		} else {
			statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		}
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSubject( 
										@PathVariable String projectID,
										@RequestParam(value="subjectName", required=true) String subjectName,
										@RequestParam(value="gender") String gender,
										@RequestParam(value="dob") String dob,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int	statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/status/{status:.+}", method = RequestMethod.PUT)
	public void setEPADSubjectStatus( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String status,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		subjectReference.status = status;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		String errstatus = epadOperations.setSubjectStatus(subjectReference, sessionID, username);
		if (!"".equals(status))
			throw new Exception("Error setting patient status");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADStudy( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="description", required=true) String description,
											@RequestParam(value="studyDate", required=true) String studyDate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
		log.warning("Create/Modify Study, status:" + statusCode);
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating a study");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADStudy( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@RequestParam(value="description", required=true) String description,
											@RequestParam(value="studyDate", required=true) String studyDate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
		log.warning("Create/Modify Study, status:" + statusCode);
		if (statusCode != HttpServletResponse.SC_OK)
			throw new Exception("Error creating a study");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSeries( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@RequestParam(value="description", required=true) String description,
										@RequestParam(value="seriesDate", required=true) String seriesDate,
										@RequestParam(value="modality", required=false) String modality,
										@RequestParam(value="referencedSeries", required=false) String referencedSeries,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), modality, referencedSeries, sessionID);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSeries( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@RequestParam(value="description", required=true) String description,
										@RequestParam(value="seriesDate", required=true) String seriesDate,
										@RequestParam(value="modality", required=false) String modality,
										@RequestParam(value="referencedSeries", required=false) String referencedSeries,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), modality, referencedSeries, sessionID);
	}

	@RequestMapping(value = "/{projectID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADProjectAIM( 
											@PathVariable String projectID,
											@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		AIMReference aimReference = new AIMReference(aimID);
		log.info("Projects AIM PUT");
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createProjectAIM(username, projectReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
			throw new Exception("Error creating Project AIM:" + status);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADSubjectAIM( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		AIMReference aimReference = new AIMReference(aimID);
		log.info("Subject AIM PUT");
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createSubjectAIM(username, subjectReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
			throw new Exception("Error creating Subject AIM:" + status);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADStudyAIM( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Study AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createStudyAIM(username, studyReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
			throw new Exception("Error creating Study AIM:" + status);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADSeriesAIM( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Series AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createSeriesAIM(username, seriesReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
			throw new Exception("Error creating Series AIM:" + status);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADImageAIM( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String imageUID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Image AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createImageAIM(username, imageReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
		{
			throw new Exception("Error creating Image AIM:" + status);
		}
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADImageAIM( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String imageUID,
										@PathVariable int frameNo,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNo);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Frame AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createFrameAIM(username, frameReference, aimReference.aimID, uploadedFile, sessionID);
		if (!"".equals(status))
			throw new Exception("Error creating Frame AIM:" + status);
	}	

	@RequestMapping(value = "/{projectID}/aims/", method = RequestMethod.PUT)
	public void runEPADProjectAIMPlugin( 
											@PathVariable String projectID,
											@RequestParam(value="annotationUID", required=true) String annotationUID,
											@RequestParam(value="templateName", required=true) String templateName,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		String[] aimIDs = annotationUID.split(",");
		AIMUtil.runPlugIn(aimIDs, templateName, projectReference.projectID, sessionID);
	}

	@RequestMapping(value = "/{projectID}/users/{projectuser:.+}", method = RequestMethod.PUT)
	public void addUserToProject( 
										@PathVariable String projectID,
										@PathVariable String projectuser,
										@RequestParam(value="role", required=false) String role,
										@RequestParam(value="defaultTemplate", required=false) String defaultTemplate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.addUserToProject(username, projectReference, projectuser, role, defaultTemplate, sessionID);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/", method = RequestMethod.POST)
	public void editDSO( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String imageUID,
										@RequestParam(value="type", required=false) String type,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		PrintWriter responseStream = response.getWriter();
		if (!"new".equals(type))
		{
			boolean errstatus = DSOUtil.handleDSOFramesEdit(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
					imageReference.seriesUID, imageReference.imageUID, request, responseStream);
			if (errstatus)
				throw new Exception("Error editing DSO");
		}
		else
		{
			boolean errstatus = DSOUtil.handleCreateDSO(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
					imageReference.seriesUID, request, responseStream, username);
			if (errstatus)
				throw new Exception("Error creating DSO");
		}
	}	

	@RequestMapping(value = "/{projectID}/files/", method = RequestMethod.POST)
	public void uploadProjectFiles( 
										@PathVariable String projectID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		createFile(username, projectID, null, null, null, fileType, description, request, response);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/", method = RequestMethod.POST)
	public void uploadSubjectFiles( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		createFile(username, projectID, subjectID, null, null, fileType, description, request, response);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/", method = RequestMethod.POST)
	public void uploadStudyFiles( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="description", required=false) String description,
											@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		createFile(username, projectID, subjectID, studyUID, null, fileType, description, request, response);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/", method = RequestMethod.POST)
	public void uploadSeriesFiles( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		createFile(username, projectID, subjectID, studyUID, seriesUID, fileType, description, request, response);
	}
	
	private void createFile(String username, String projectID, String subjectID, String studyUID, String seriesUID,
			String fileType, String description, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String sessionID = SessionService.getJSessionIDFromRequest(request);
	    String requestContentType = request.getContentType();
		Map<String, Object> paramData = null;
		int numberOfFiles = 0;
		File uploadedFile = null;
		String uploadDir = null;
		try
		{
			if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
			{
				PrintWriter responseStream = response.getWriter();
				uploadDir = EPADConfig.getEPADWebServerFileUploadDir() + "temp" + Long.toString(System.currentTimeMillis());
				paramData = HandlerUtil.parsePostedData(uploadDir, request, responseStream);
				for (String param: paramData.keySet())
				{
					if (paramData.get(param) instanceof File)
					{
						if (uploadedFile == null)
							uploadedFile = (File) paramData.get(param);
						numberOfFiles++;
					}
				}
			}
			List<String> descriptions = (List<String>) paramData.get("description_List");
			List<String> fileTypes = (List<String>) paramData.get("fileType_List");
			if (numberOfFiles == 1) {
				if (description == null) {
					if (descriptions != null && descriptions.size() > 0)
						description = descriptions.get(0);
				}
				if (fileType == null) {
					if (fileTypes != null && fileTypes.size() > 0)
						fileType = fileTypes.get(0);
				}
				if (FileType.ANNOTATION.getName().equalsIgnoreCase(fileType)) {
					if (AIMUtil.saveAIMAnnotation(uploadedFile, projectID, sessionID, username))
						throw new Exception("Error saving AIM file");
				}
				else {
					EpadOperations epadOperations = DefaultEpadOperations.getInstance();
					epadOperations.createFile(username, projectID, subjectID, studyUID, seriesUID,
							uploadedFile, description, fileType, sessionID);
				}
			} else {
				int i = 0;
				for (String param: paramData.keySet())
				{
					if (paramData.get(param) instanceof File)
					{
						description = request.getParameter("description");
						if (descriptions != null && descriptions.size() > i)
							description = descriptions.get(i);
						fileType = request.getParameter("fileType");
						if (fileTypes != null && fileTypes.size() > i)
								fileType = fileTypes.get(i);
						if (FileType.ANNOTATION.getName().equalsIgnoreCase(fileType)) {
							if (AIMUtil.saveAIMAnnotation(uploadedFile, projectID, sessionID, username))
								throw new Exception("Error saving AIM file");
						}
						else {
							EpadOperations epadOperations = DefaultEpadOperations.getInstance();
							epadOperations.createFile(username, projectID, subjectID, studyUID, seriesUID,
									uploadedFile, description, fileType, sessionID);
						}
						i++;
					}
				}
			}		
		}
		finally {
			if (uploadedFile != null)
			{
				if (uploadedFile.getParentFile().exists())
				{
					log.info("Deleting upload directory " + uploadedFile.getParentFile().getAbsolutePath());
					EPADFileUtils.deleteDirectoryAndContents(uploadedFile.getParentFile());
				}
			}
		}
	}
	
	@RequestMapping(value = "/{projectID:.+}", method = RequestMethod.DELETE)
	public void deleteEPADProject( 
										@PathVariable String projectID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.projectDelete(projectID, sessionID, username);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID:.+}", method = RequestMethod.DELETE)
	public void deleteEPADSubject( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.subjectDelete(subjectReference, sessionID, username);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID:.+}", method = RequestMethod.DELETE)
	public void deleteEPADStudy( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="deleteAims", defaultValue="true") boolean deleteAims,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.studyDelete(studyReference, sessionID, deleteAims, username);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID:.+}", method = RequestMethod.DELETE)
	public void deleteEPADSeries( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@RequestParam(value="deleteAims", defaultValue="true") boolean deleteAims,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.seriesDelete(seriesReference, sessionID, deleteAims, username);
	}


	@RequestMapping(value = "/{projectID}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteProjectAim( 
									@PathVariable String projectID,
									@PathVariable String aimID,
									@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		ProjectReference projectReference = new ProjectReference(projectID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.projectAIMDelete(projectReference, aimID, sessionID, deleteDSO, username);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteEPADSubjectAim( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String aimID,
										@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.subjectAIMDelete(subjectReference, aimID, sessionID, deleteDSO, username);
	}	
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteEPADStudyAim( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String aimID,
											@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.studyAIMDelete(studyReference, aimID, sessionID, deleteDSO, username);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteEPADSeriesAim( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String aimID,
											@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.seriesAIMDelete(seriesReference, aimID, sessionID, deleteDSO, username);
	}
	
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteEPADImageAim( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											@PathVariable String aimID,
											@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.imageAIMDelete(imageReference, aimID, sessionID, deleteDSO, username);
	}
		
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameno}/aims/{aimID}", method = RequestMethod.DELETE)
	public void deleteEPADSeriesAim( 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String aimID,
											@PathVariable String imageUID,
											@PathVariable int frameno,
											@RequestParam(value="deleteDSO", defaultValue="true") boolean deleteDSO,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameno);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.frameAIMDelete(frameReference, aimID, sessionID, deleteDSO, username);
	}
		
	
	@RequestMapping(value = "/{projectID}/users/{username:.+}", method = RequestMethod.DELETE)
	public void removeUserFromProject( 
											@PathVariable String projectID,
											@PathVariable String username,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		ProjectReference projectReference = new ProjectReference(projectID);
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String loggedname = SessionService.getUsernameForSession(sessionID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.removeUserFromProject(loggedname, projectReference, username, sessionID);
	}
		
	
	@RequestMapping(value = "/{projectID}/users/{reader}/worklists/{workListID:.+}", method = {RequestMethod.DELETE})
	public void deleteUserWorkList( 
										@PathVariable String projectID,
										@PathVariable String reader,
										@PathVariable String workListID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		WorkList wl = worklistOperations.getWorkList(workListID);
		if (wl == null)
			throw new NotFoundException("Worklist not found for id " + workListID + " and project " + projectID);
		worklistOperations.deleteWorkList(username, wl.getWorkListID());;		
	}
	
	@RequestMapping(value = "/{projectID}/worklists/{workListID:.+}", method = {RequestMethod.DELETE})
	public void deleteUserWorkList( 
										@PathVariable String projectID,
										@PathVariable String workListID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		WorkList wl = worklistOperations.getWorkList(workListID);
		if (wl == null)
			throw new NotFoundException("Worklist not found for id " + workListID + " and project " + projectID);
		worklistOperations.deleteWorkList(username, wl.getWorkListID());;		
	}

	@RequestMapping(value = "/{projectID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteProjectFile( 
										@PathVariable String projectID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		try {
		epadOperations.deleteFile(username, projectReference, filename);
		} catch (Exception x) {
			log.warning("Error deleting " + filename, x);
			throw x;
		}
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteSubjectFile( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteFile(username, subjectReference, filename);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteStudyFile( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteFile(username, studyReference, filename);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteSeriesFile( 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteFile(username, seriesReference, filename);
	}			
	
	private int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}
	
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	private Date getDate(String dateStr)
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

}
