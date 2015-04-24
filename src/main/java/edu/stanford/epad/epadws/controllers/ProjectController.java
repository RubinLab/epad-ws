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
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.queries.DefaultEpadOperations;
import edu.stanford.epad.epadws.queries.EpadOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.SessionService;
import edu.stanford.epad.epadws.service.UserProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	private static final EPADLogger log = EPADLogger.getInstance();
 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public EPADProjectList getEPADProjects(@RequestParam(value="username") String username, 
											@RequestParam(value="annotationCount", required = false) boolean annotationCount,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Getting project descriptions");
		EPADProjectList projectList = epadOperations.getProjectDescriptions(username, sessionID, searchFilter, annotationCount); 
		log.info("Number of projects:" + projectList.ResultSet.totalRecords);
		return projectList;
	}
	 
	@RequestMapping(value = "/{projectID:.+}", method = RequestMethod.GET)
	public EPADProject getEPADProject(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
		if (project == null)
			throw new NotFoundException("Project " + projectID + " not found");
		return project;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/", method = RequestMethod.GET)
	public EPADSubjectList getEPADProjectSubjects(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubjectList subjectList = epadOperations.getSubjectDescriptions(projectID, username, sessionID, searchFilter);
		return subjectList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID:.+}", method = RequestMethod.GET)
	public EPADSubject getEPADProjectSubject(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		if (subject == null)
			throw new NotFoundException("Subject " + subjectID + " not found in project " + projectID);
		return subject;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/", method = RequestMethod.GET)
	public EPADStudyList getEPADProjectStudies(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADStudyList studyList = epadOperations.getStudyDescriptions(subjectReference, username, sessionID,
		searchFilter);
		return studyList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID:.+}", method = RequestMethod.GET)
	public EPADStudy getEPADProjectStudy(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADStudy study = epadOperations.getStudyDescription(studyReference, username, sessionID);
		if (study == null)
			throw new NotFoundException("Study " + studyUID + " not found in project " + projectID + " for subject:" + subjectID);
		return study;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/", method = RequestMethod.GET)
	public EPADSeriesList getEPADProjectSerieses(@RequestParam(value="username") String username,
												@RequestParam(value="filterDSO", defaultValue="false") boolean filterDSO,
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSeriesList seriesList = epadOperations.getSeriesDescriptions(studyReference, username, sessionID,
		searchFilter, filterDSO);
		return seriesList;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID:.+}", method = RequestMethod.GET)
	public EPADSeries getEPADProjectSeries(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSeries series = epadOperations.getSeriesDescription(seriesReference, username, sessionID);
		if (series == null)
			throw new NotFoundException("Series " + seriesUID + " not found in project " + projectID + " for subject:" + subjectID + " and study:" + studyUID);
		return series;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/", method = RequestMethod.GET)
	public EPADImageList getEPADProjectImages(@RequestParam(value="username") String username,
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
	public EPADImage getEPADProjectImage(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String imageUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADImage image = epadOperations.getImageDescription(imageReference, sessionID);
		if (image == null)
			throw new NotFoundException("Image " + imageUID + " for Series " + seriesUID + " not found in project " + projectID + " for subject:" + subjectID + " and study:" + studyUID);
		return image;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/", method = RequestMethod.GET)
	public EPADFrameList getEPADProjectFrames(@RequestParam(value="username") String username,
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
	public EPADFrame getEPADProjectFrame(@RequestParam(value="username") String username, 
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
	public void getEPADProjectAims(@RequestParam(value="username") String username, 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
										@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADProjectAim(@RequestParam(value="username") String username, 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADSubjectAims(@RequestParam(value="username") String username, 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADSubjectAim(@RequestParam(value="username") String username, 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADStudyAims(@RequestParam(value="username") String username, 
									@RequestParam(value="start", defaultValue="0") int start,
									@RequestParam(value="count", defaultValue="5000") int count,
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADStudyAim(@RequestParam(value="username") String username, 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADSeriesAims(@RequestParam(value="username") String username, 
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
	public void getEPADSeriesAim(@RequestParam(value="username") String username, 
									@RequestParam(value="format", defaultValue="xml") String format,
									@PathVariable String projectID,
									@PathVariable String subjectID,
									@PathVariable String studyUID,
									@PathVariable String seriesUID,
									@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);

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
	public void getEPADImageAims(@RequestParam(value="username") String username, 
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
	public void getEPADImageAim(@RequestParam(value="username") String username, 
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
	public void getEPADFrameAims(@RequestParam(value="username") String username, 
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
	public void getEPADFrameAim(@RequestParam(value="username") String username, 
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
		else
		{
			AIMUtil.queryAIMImageAnnotationsV4(responseStream, aims, username, sessionID);					
		}
	}	
	 
	@RequestMapping(value = "/{projectID}/users/", method = RequestMethod.GET)
	public EPADUserList getEPADProjectUsers(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADUserList users = epadOperations.getUserDescriptions(username, projectReference, sessionID);
		return users;
	}
	 
	@RequestMapping(value = "/{projectID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADProjectFiles(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(projectReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADSubjectFiles(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(subjectReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADStudyFiles(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EPADSearchFilter searchFilter = EPADSearchFilterBuilder.build(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFileList files = epadOperations.getFileDescriptions(studyReference, username, sessionID, searchFilter, true);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/", method = RequestMethod.GET)
	public EPADFileList getEPADSeriesFiles(@RequestParam(value="username") String username, 
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
		EPADFileList files = epadOperations.getFileDescriptions(seriesReference, username, sessionID, searchFilter);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADProjectFile(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile file = epadOperations.getFileDescription(projectReference, filename, username, sessionID);
		return file;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADSubjectFile(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(subjectReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADStudyFile(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(studyReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/{filename:.+}", method = RequestMethod.GET)
	public EPADFile getEPADSeriesFile(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String seriesUID,
											@PathVariable String filename,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADFile files = epadOperations.getFileDescription(seriesReference, filename, username, sessionID);
		return files;
	}
	 
	@RequestMapping(value = "/{projectID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADProject(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@RequestParam(value="projectName", required=true) String projectName,
											@RequestParam(value="projectDescription", required=true) String projectDescription,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADProject project = epadOperations.getProjectDescription(projectReference, username, sessionID);
		if (project != null) {
			statusCode = epadOperations.updateProject(username, projectReference, projectName, projectDescription, sessionID);
		} else {
			statusCode = epadOperations.createProject(username, projectReference, projectName, projectDescription, sessionID);
		}
		if (statusCode != HttpServletResponse.SC_OK);
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSubject(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@RequestParam(value="subjectName", required=true) String subjectName,
										@RequestParam(value="gender") String gender,
										@RequestParam(value="dob") String dob,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		int statusCode = 0;
		if (subject != null) {
			statusCode = epadOperations.updateSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		} else {
			statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		}
		if (statusCode != HttpServletResponse.SC_OK);
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSubject(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@RequestParam(value="subjectName", required=true) String subjectName,
										@RequestParam(value="gender") String gender,
										@RequestParam(value="dob") String dob,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int	statusCode = epadOperations.createSubject(username, subjectReference, subjectName, getDate(dob), gender, sessionID);
		if (statusCode != HttpServletResponse.SC_OK);
			throw new Exception("Error creating or modifying project");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/status/{status:.+}", method = RequestMethod.PUT)
	public void setEPADSubjectStatus(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String status,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		subjectReference.status = status;
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		EPADSubject subject = epadOperations.getSubjectDescription(subjectReference, username, sessionID);
		String errstatus = epadOperations.setSubjectStatus(subjectReference, sessionID, username);
		if (status != "");
			throw new Exception("Error setting patient status");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADStudy(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="description", required=true) String description,
											@RequestParam(value="studyDate", required=true) String studyDate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
		if (statusCode != HttpServletResponse.SC_OK);
			throw new Exception("Error creating a study");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADStudy(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@RequestParam(value="description", required=true) String description,
											@RequestParam(value="studyDate", required=true) String studyDate,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		statusCode = epadOperations.createStudy(username, studyReference, description, getDate(studyDate), sessionID);
		if (statusCode != HttpServletResponse.SC_OK);
			throw new Exception("Error creating a study");
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID:.+}", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSeries(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@RequestParam(value="description", required=true) String description,
										@RequestParam(value="seriesDate", required=true) String seriesDate,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), sessionID);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/", method = {RequestMethod.PUT,RequestMethod.POST})
	public void createEPADSeries(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@RequestParam(value="description", required=true) String description,
										@RequestParam(value="seriesDate", required=true) String seriesDate,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, "new");
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		int statusCode = 0;
		EPADSeries series  = epadOperations.createSeries(username, seriesReference, description, getDate(seriesDate), sessionID);
	}

	@RequestMapping(value = "/{projectID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADProjectAIM(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		AIMReference aimReference = new AIMReference(aimID);
		log.info("Projects AIM PUT");
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createProjectAIM(username, projectReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Project AIM:" + status);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADSubjectAIM(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		AIMReference aimReference = new AIMReference(aimID);
		log.info("Subject AIM PUT");
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createSubjectAIM(username, subjectReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Subject AIM:" + status);
	}
	
	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADStudyAIM(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@PathVariable String aimID,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Study AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createStudyAIM(username, studyReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Study AIM:" + status);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADSeriesAIM(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		SeriesReference seriesReference = new SeriesReference(projectID, subjectID, studyUID, seriesUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Series AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createSeriesAIM(username, seriesReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Series AIM:" + status);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADImageAIM(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String imageUID,
										@PathVariable String aimID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Image AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createImageAIM(username, imageReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Image AIM:" + status);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/{frameNo}/aims/{aimID}", method = RequestMethod.PUT)
	public void createEPADImageAIM(@RequestParam(value="username") String username, 
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
		FrameReference frameReference = new FrameReference(projectID, subjectID, studyUID, seriesUID, imageUID, frameNo);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		log.info("Frame AIM PUT");
		AIMReference aimReference = new AIMReference(aimID);
		File uploadedFile = HandlerUtil.getUploadedFile(request);
		String status = epadOperations.createFrameAIM(username, frameReference, aimReference.aimID, uploadedFile, sessionID);
		if (status != "");
			throw new Exception("Error creating Frame AIM:" + status);
	}	

	@RequestMapping(value = "/{projectID}/aims/", method = RequestMethod.PUT)
	public void runEPADProjectAIMPlugin(@RequestParam(value="username") String username, 
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

	@RequestMapping(value = "/{projectID}/user/{projectuser:.+}", method = RequestMethod.PUT)
	public void addUserToProject(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String projectuser,
										@RequestParam(value="role", required=true) String role,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ProjectReference projectReference = new ProjectReference(projectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.addUserToProject(username, projectReference, projectuser, role, sessionID);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/images/{imageUID}/frames/", method = RequestMethod.POST)
	public void editDSO(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String imageUID,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		ImageReference imageReference = new ImageReference(projectID, subjectID, studyUID, seriesUID, imageUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		PrintWriter responseStream = response.getWriter();
		boolean errstatus = DSOUtil.handleDSOFramesEdit(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
				imageReference.seriesUID, imageReference.imageUID, request, responseStream);
		if (errstatus);
			throw new Exception("Error editing DSO");
	}	

	@RequestMapping(value = "/{projectID}/files/", method = RequestMethod.POST)
	public void uploadProjectFiles(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		createFile(username, projectID, null, null, null, fileType, description, request, response);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/files/", method = RequestMethod.POST)
	public void uploadSubjectFiles(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		createFile(username, projectID, subjectID, null, null, fileType, description, request, response);
	}	

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/", method = RequestMethod.POST)
	public void uploadStudyFiles(@RequestParam(value="username") String username, 
											@PathVariable String projectID,
											@PathVariable String subjectID,
											@PathVariable String studyUID,
											@RequestParam(value="description", required=false) String description,
											@RequestParam(value="fileType", required=false) String fileType,
											HttpServletRequest request, 
									        HttpServletResponse response) throws Exception {
		createFile(username, projectID, subjectID, studyUID, null, fileType, description, request, response);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/", method = RequestMethod.POST)
	public void uploadSeriesFiles(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="fileType", required=false) String fileType,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
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
		if (requestContentType != null && requestContentType.startsWith("multipart/form-data"))
		{
			PrintWriter responseStream = response.getWriter();
			paramData = HandlerUtil.parsePostedData(request, responseStream);
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
	
	@RequestMapping(value = "/{projectID}/users/{reader}/worklists/{workListID:.+}", method = RequestMethod.PUT)
	public void createUserWorkList(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String reader,
										@PathVariable String workListID,
										@RequestParam(value="description", required=false) String description,
										@RequestParam(value="dueDate", required=false) String dueDate,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		String sessionID = SessionService.getJSessionIDFromRequest(request);
		EpadWorkListOperations worklistOperations = DefaultWorkListOperations.getInstance();
		worklistOperations.createWorkList(username, reader, projectID, workListID, description, null, getDate(dueDate));
	}

	@RequestMapping(value = "/{projectID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteProjectFile(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
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
	public void deleteSubjectFile(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		SubjectReference subjectReference = new SubjectReference(projectID, subjectID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteFile(username, subjectReference, filename);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteStudyFile(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
		StudyReference studyReference = new StudyReference(projectID, subjectID, studyUID);
		EpadOperations epadOperations = DefaultEpadOperations.getInstance();
		epadOperations.deleteFile(username, studyReference, filename);
	}

	@RequestMapping(value = "/{projectID}/subjects/{subjectID}/studies/{studyUID}/series/{seriesUID}/files/{filename:.+}", method = RequestMethod.DELETE)
	public void deleteSeriesFile(@RequestParam(value="username") String username, 
										@PathVariable String projectID,
										@PathVariable String subjectID,
										@PathVariable String studyUID,
										@PathVariable String seriesUID,
										@PathVariable String filename,
										HttpServletRequest request, 
								        HttpServletResponse response) throws Exception {
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
