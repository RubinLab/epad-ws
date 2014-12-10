package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.aim.aimapi.Aim;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
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

		try {
			seriesBeingProcessed.add(seriesUID);
			DSOUtil.writeDSOMaskPNGs(dsoFile);
			if (generateAIM)
			{
				// Must be first upload, create AIM file
				List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, "admin", 1, 50);
				if (ias == null || ias.size() == 0)
				{
					AIMUtil.generateAIMFileForDSO(dsoFile);
				}
				else
				{
					ImageAnnotation ia = ias.get(0);
					if (ia.getCodingSchemeDesignator().equals("epad-plugin"))
					{
						Aim aim = new Aim(ia);
						EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
						epadDatabaseOperations.insertEpadEvent(
								ia.getListUser().get(0).getLoginName(), 
								"Image Generation Complete", 
								ia.getUniqueIdentifier(), ia.getName(),
								aim.getPatientID(), 
								aim.getPatientName(), 
								aim.getCodeMeaning(), 
								aim.getCodeValue(),
								"DSO Plugin");					
					}
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
