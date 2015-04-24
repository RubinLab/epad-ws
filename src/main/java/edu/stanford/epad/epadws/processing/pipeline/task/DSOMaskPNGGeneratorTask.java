package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.aim.aimapi.Aim;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingStatusTracker;
import edu.stanford.epad.epadws.processing.model.SeriesPipelineState;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;

public class DSOMaskPNGGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String seriesUID;
	private final File dsoFile;
	private final boolean generateAIM;
	private final String tagFilePath;

	static public Set seriesBeingProcessed = Collections.synchronizedSet(new HashSet());
	
	public DSOMaskPNGGeneratorTask(String seriesUID, File dsoFile, boolean generateAIM, String tagFilePath)
	{
		this.seriesUID = seriesUID;
		this.dsoFile = dsoFile;
		this.generateAIM = generateAIM;
		this.tagFilePath = tagFilePath;
	}

	@Override
	public void run()
	{
		if (seriesBeingProcessed.contains(seriesUID))
		{
			log.info("DSO series  " + seriesUID + " already being processed");
			return;
		}
		log.info("Processing DSO for series  " + seriesUID + "; file=" + dsoFile.getAbsolutePath());

		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		try {
			seriesBeingProcessed.add(seriesUID);
			try {
				DSOUtil.writeDSOMaskPNGs(dsoFile);
			} catch (Exception x) {
				SeriesPipelineState status = DicomSeriesProcessingStatusTracker.getInstance().getDicomSeriesProcessingStatus(seriesUID);
				if (status != null)
					DicomSeriesProcessingStatusTracker.getInstance().removeSeriesPipelineState(status);
				epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
				throw x;
			}
			// Must be first upload, create AIM file
			List<EPADAIM> aims = EpadDatabase.getInstance().getEPADDatabaseOperations().getAIMsByDSOSeries(seriesUID);
			List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, "admin", 1, 50);
			if (aims.size() == 0 && generateAIM)
			{
				ImageAnnotation ia = AIMUtil.generateAIMFileForDSO(dsoFile);
				ias = new ArrayList<ImageAnnotation>();
				ias.add(ia);
			}
			ImageAnnotation ia = ias.get(0);
			if (ia.getCodingSchemeDesignator().equals("epad-plugin"))
			{
				Aim aim = new Aim(ia);
				epadDatabaseOperations.insertEpadEvent(
						ia.getListUser().get(0).getLoginName(), 
						"Image Generation Complete", 
						ia.getUniqueIdentifier(), ia.getName(),
						aim.getPatientID(), 
						aim.getPatientName(), 
						aim.getCodeMeaning(), 
						aim.getCodeValue(),
						"DSO Plugin");					
				projectOperations.userInfoLog(ia.getListUser().get(0).getLoginName(), "Image Generation Complete:"+aim.getPatientID() + ":" + aim.getCodeValue());
			}
			else if (UserProjectService.pendingUploads.containsKey(seriesUID))
			{
				Aim aim = new Aim(ia);
				String username = UserProjectService.pendingUploads.get(seriesUID);
				if (username != null && username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
				if (username != null)
				{
					epadDatabaseOperations.insertEpadEvent(
							ia.getListUser().get(0).getLoginName(), 
							"Image Generation Complete", 
							ia.getUniqueIdentifier(), ia.getName(),
							aim.getPatientID(), 
							aim.getPatientName(), 
							aim.getCodeMeaning(), 
							aim.getCodeValue(),
							"");					
					UserProjectService.pendingUploads.remove(seriesUID);
					projectOperations.userInfoLog(username, "Image Generation Complete:"+aim.getPatientID() + ":" + aim.getCodeValue());
				}
			}
					 
		} catch (Exception e) {
			log.warning("Error writing AIM file for DSO series " + seriesUID, e);
		} finally {
			log.info("DSO for series " + seriesUID + " completed");
			seriesBeingProcessed.remove(seriesUID);
		}
	}

	@Override
	public File getDICOMFile()
	{
		return dsoFile;
	}

	@Override
	public String getTagFilePath()
	{
		return tagFilePath;
	}

	@Override
	public String getTaskType()
	{
		return "DSOPNGMaskGeneration";
	}

	@Override
	public String getSeriesUID()
	{
		return this.seriesUID;
	}
}
