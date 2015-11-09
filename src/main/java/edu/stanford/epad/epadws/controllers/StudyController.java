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

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
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
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilterBuilder;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.handlers.dicom.DownloadUtil;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;

@RestController
@RequestMapping("/studies")
public class StudyController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	 
	@RequestMapping(value = "/{studyUID:.+}", method = RequestMethod.GET)
	public void getEPADStudy( 
											@PathVariable String studyUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											@RequestParam(value="seriesUIDs", required = false) String seriesUIDs, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		StudyReference studyReference = new StudyReference(null, null, studyUID);
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
				throw new NotFoundException("Study " + studyUID + " not found");
			responseStream.append(study.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{studyUID}/series/", method = RequestMethod.GET)
	public EPADSeriesList getEPADStudySerieses(
												@RequestParam(value="filterDSO", defaultValue="false") boolean filterDSO,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		StudyReference studyReference = new StudyReference(null, null, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
		searchFilter, filterDSO);
		return seriesList;
	}
	 
	@RequestMapping(value = "/{studyUID}/series/{seriesUID:.+}", method = RequestMethod.GET)
	public void getEPADStudySeries( 
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		SeriesReference seriesReference = new SeriesReference(null, null, studyUID, seriesUID);
		if ("file".equalsIgnoreCase(format)) {
			if (seriesReference.seriesUID.contains(","))
				DownloadUtil.downloadSeries(false, response, seriesReference.seriesUID, username, sessionID, includeAims);
			else
				DownloadUtil.downloadSeries(false, response, seriesReference, username, sessionID, includeAims);
		} else if ("stream".equalsIgnoreCase(format)) {
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
				throw new NotFoundException("Series " + seriesUID + " not found in study:" + studyUID);
			responseStream.append(series.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/", method = RequestMethod.GET)
	public EPADImageList getEPADProjectImages(
											@RequestParam(value="filterDSO", defaultValue="false") boolean filterDSO,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SeriesReference seriesReference = new SeriesReference(null, null, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADImageList imageList = epadOperations.getImageDescriptions(seriesReference, sessionID, searchFilter);
		return imageList;
	}
	 
	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID:.+}", method = RequestMethod.GET)
	public void getEPADProjectImage( 
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											@RequestParam(value="format", required = false) String format, 
											@RequestParam(value="includeAims", required = false) boolean includeAims, 
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);
		ImageReference imageReference = new ImageReference(null, null, studyUID, seriesUID, imageUID);
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
				throw new NotFoundException("Image " + imageUID + " for Series " + seriesUID + " not found in study:" + studyUID);
			responseStream.append(image.toJSON());
		}
	}
	 
	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/", method = RequestMethod.GET)
	public EPADFrameList getEPADProjectFrames(
											@RequestParam(value="filterDSO",defaultValue="false") boolean filterDSO,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ImageReference imageReference = new ImageReference(null, null, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFrameList frameList = epadOperations.getFrameDescriptions(imageReference);
		return frameList;
	}
	 
	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}", method = RequestMethod.GET)
	public EPADFrame getEPADProjectFrame( 
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											@PathVariable Integer frameNo,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		FrameReference frameReference = new FrameReference(null, null, studyUID, seriesUID, imageUID, frameNo);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFrame frame = epadOperations.getFrameDescription(frameReference, sessionID);
		if (frame == null)
			throw new NotFoundException("Frame " + frame + " for Image " + imageUID + " for Series " + seriesUID + " not found in study:" + studyUID);
		return frame;
	}

	@RequestMapping(value = "/{studyUID}/aims/", method = RequestMethod.GET)
	public void getEPADStudyAims( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		StudyReference studyReference = new StudyReference(null, null, studyUID);
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

	@RequestMapping(value = "{studyUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADStudyAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		StudyReference studyReference = new StudyReference(null, null, studyUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getStudyAIMDescription(studyReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found");
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

	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/aims/", method = RequestMethod.GET)
	public void getEPADSeriesAims( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SeriesReference seriesReference = new SeriesReference(null, null, studyUID, seriesUID);
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

	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADSeriesAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		SeriesReference seriesReference = new SeriesReference(null, null, studyUID, seriesUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getSeriesAIMDescription(seriesReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found");
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

	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/", method = RequestMethod.GET)
	public void getEPADImageAims( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		ImageReference imageReference = new ImageReference(null, null, studyUID, seriesUID, imageUID);
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

	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADImageAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		ImageReference imageReference = new ImageReference(null, null, studyUID, seriesUID, imageUID);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getImageAIMDescription(imageReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found");
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


	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/", method = RequestMethod.GET)
	public void getEPADFrameAims( 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String imageUID,
									@PathVariable int frameNo,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		String username = SessionService.getUsernameForSession(sessionID);

		long starttime = System.currentTimeMillis();
		FrameReference frameReference = new FrameReference(null, null, studyUID, seriesUID, imageUID, frameNo);
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

	@RequestMapping(value = "/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/{aimID}", method = RequestMethod.GET)
	public void getEPADFrameAim( 
									@RequestParam(value="format", defaultValue="xml") String format,
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
		FrameReference frameReference = new FrameReference(null, null, studyUID, seriesUID, imageUID, frameNo);
		AIMReference aimReference = new AIMReference(aimID);
		EPADAIMList aims = new EPADAIMList();
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADAIM aim = epadOperations.getFrameAIMDescription(frameReference, aimReference.aimID, username, sessionID);
		if (aim == null)
			throw new NotFoundException("Aim " + aimID + " not found");
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
