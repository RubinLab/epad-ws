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
