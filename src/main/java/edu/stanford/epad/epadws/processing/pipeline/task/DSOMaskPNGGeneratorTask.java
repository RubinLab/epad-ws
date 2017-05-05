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
package edu.stanford.epad.epadws.processing.pipeline.task;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EventMessageCodes;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.processing.model.DicomSeriesProcessingStatusTracker;
import edu.stanford.epad.epadws.processing.model.SeriesPipelineState;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;

public class DSOMaskPNGGeneratorTask implements GeneratorTask
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private final String studyUID;
	private final String seriesUID;
	private final File dsoFile;
	private final boolean generateAIM;
	private final String tagFilePath;

	static public Set seriesBeingProcessed = Collections.synchronizedSet(new HashSet());
	
	public DSOMaskPNGGeneratorTask(String studyUID, String seriesUID, File dsoFile, boolean generateAIM, String tagFilePath)
	{
		this.studyUID = studyUID;
		this.seriesUID = seriesUID;
		this.dsoFile = dsoFile;
		this.generateAIM = generateAIM;
		this.tagFilePath = tagFilePath;
	}

	@Override
	public void run()
	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // Let interactive thread run sooner
		if (seriesBeingProcessed.contains(seriesUID))
		{
			log.info("DSO series  " + seriesUID + " already being processed");
			return;
		}
		log.info("Processing DSO for series  " + seriesUID + "; file=" + dsoFile.getAbsolutePath());
		EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
		EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
		if (UserProjectService.pendingUploads.containsKey(studyUID))
		{
			String username = UserProjectService.pendingUploads.get(studyUID);
			String projectID = EPADConfig.xnatUploadProjectID;
			if (username != null && username.indexOf(":") != -1)
			{
				projectID = username.substring(username.indexOf(":")+1);
				username = username.substring(0, username.indexOf(":"));
			}
			if (username != null)
			{
				epadDatabaseOperations.insertEpadEvent(
						username, 
						EventMessageCodes.STUDY_PROCESSED, 
						"", "", "", "", "", "", 
						"Study:" + studyUID,
						projectID,"",studyUID,"",false);					
				UserProjectService.pendingUploads.remove(studyUID);
			}
		}

		try {
			seriesBeingProcessed.add(seriesUID);
			try {
				DSOUtil.writeDSOMaskPNGs(dsoFile);
			} catch (Exception x) {
				log.warning("Error generating PNGs DSO series " + seriesUID, x);
				SeriesPipelineState status = DicomSeriesProcessingStatusTracker.getInstance().getDicomSeriesProcessingStatus(seriesUID);
				if (status != null)
					DicomSeriesProcessingStatusTracker.getInstance().removeSeriesPipelineState(status);
				epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
				throw x;
			} catch (Error x) {
				log.warning("Error generating PNGs DSO series " + seriesUID, x);
				SeriesPipelineState status = DicomSeriesProcessingStatusTracker.getInstance().getDicomSeriesProcessingStatus(seriesUID);
				if (status != null)
					DicomSeriesProcessingStatusTracker.getInstance().removeSeriesPipelineState(status);
				epadDatabaseOperations.updateOrInsertSeries(seriesUID, SeriesProcessingStatus.ERROR);
				throw x;
			}
			// Must be first upload, create AIM file
			//check if there were any dso aims in the upload first
			for (int i=0; i<AIMUtil.dsoAims.size(); i++) {
				String[] a=AIMUtil.dsoAims.get(i);
				final Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
						.getDcm4CheeDatabaseOperations();
				String dsoSeriesUID=dcm4CheeDatabaseOperations.getSeriesUIDForImage(a[1]);
				if (dsoSeriesUID.equals(seriesUID) ) { //referencing to this dso series
					EpadDatabase.getInstance().getEPADDatabaseOperations().updateAIMDSOSeries(a[2], dsoSeriesUID, "admin");
					SeriesReference seriesReference = new SeriesReference(a[3], null, null, a[0]);
					List<EPADAIM> aims = epadDatabaseOperations.getAIMs(seriesReference);
					for (EPADAIM e: aims)
					{
						log.info("Checking, aimID:" + e.aimID + " dsoSeries:" + e.dsoSeriesUID + " this:" + dsoSeriesUID);
						if (!e.aimID.equals(a[2]) && e.dsoSeriesUID != null && e.dsoSeriesUID.equals(dsoSeriesUID))
						{
							ImageReference imageReference = new ImageReference(a[3], e.subjectID, e.studyUID, e.seriesUID, e.imageUID);												
							//delete the wrong one
							if (e.isDicomSR==false && a[4].equalsIgnoreCase("false"))// if none of them are dicomsr
								epadDatabaseOperations.deleteAIM("admin", e.aimID);
							log.info("Updating dsoSeriesUID in aim database:" + e.dsoSeriesUID + " aimID:" + a[2]);
							//update the existing aim with the dso series uid
							epadDatabaseOperations.addDSOAIM("admin", imageReference, e.dsoSeriesUID, a[2]);												
							epadDatabaseOperations.updateAIMDSOFrameNo(a[2], e.dsoFrameNo);	
							//update template code
							//done below after getting the aims
							break;
						}
					}
					
					AIMUtil.dsoAims.remove(a);
					i--;
				}
			}
			//check db
			List<EPADAIM> aims = EpadDatabase.getInstance().getEPADDatabaseOperations().getAIMsByDSOSeries(seriesUID);
			log.info("aims count:"+aims.size());
			//update the templatecode columns
			AIMUtil.updateTableTemplateColumn(aims);
			//requery
			aims= EpadDatabase.getInstance().getEPADDatabaseOperations().getAIMsByDSOSeries(seriesUID);
			//check exist (series uid is dso's series id. this won't work with the new aims)
//			List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, seriesUID, "admin", 1, 50);
			String projectID = null;
			if (aims.size() == 0 && generateAIM)
			{
				List<Project> projects = projectOperations.getProjectsForStudy(studyUID);
				Study study = projectOperations.getStudy(studyUID);
				String username = study.getCreator();
				for (Project project: projects)
				{
					if (project.getProjectId().equals(EPADConfig.xnatUploadProjectID)) continue;
					projectID = project.getProjectId();
					if ("admin".equals(username)){ 
						username = project.getCreator();
						log.info("dso png generator sets username admin to "+username);
					}
				}
				ImageAnnotation ia = AIMUtil.generateAIMFileForDSO(dsoFile, username, projectID);
				//requery
				aims=EpadDatabase.getInstance().getEPADDatabaseOperations().getAIMsByDSOSeries(seriesUID);
			}
			if (aims.size() != 0 && aims.get(0).templateType != null && aims.get(0).templateType.equals("epad-plugin"))
			{
				EPADAIMList aimList= AIMUtil.getAllVersionSummaries(aims.get(0));
				if (aimList.ResultSet.totalRecords<0)
					return;
				EPADAIM aim = aimList.ResultSet.Result.get(0);
				EpadProjectOperations projOp = DefaultEpadProjectOperations.getInstance();
				Template t=projOp.getTemplate(aim.template);
				
				epadDatabaseOperations.insertEpadEvent(
						aim.userName, 
						EventMessageCodes.IMAGE_PROCESSED, 
						aim.aimID, aim.name,
						aim.subjectID, 
						aim.patientName, 
						aim.template,
						t.getTemplateName(), 
						"DSO Plugin", aim.projectID,"","",seriesUID, false);					
			}
			else if (aims.size() != 0 && UserProjectService.pendingPNGs.containsKey(seriesUID))
			{
				EPADAIMList aimList= AIMUtil.getAllVersionSummaries(aims.get(0));
				if (aimList.ResultSet.totalRecords<0)
					return;
				EPADAIM aim = aimList.ResultSet.Result.get(0);
				EpadProjectOperations projOp = DefaultEpadProjectOperations.getInstance();
				Template t=projOp.getTemplate(aim.template);
				String username = UserProjectService.pendingPNGs.get(seriesUID);
				if (username != null && username.indexOf(":") != -1)
					username = username.substring(0, username.indexOf(":"));
				if (username != null)
				{
					epadDatabaseOperations.insertEpadEvent(
							aim.userName, 
							EventMessageCodes.IMAGE_PROCESSED, 
							aim.aimID, aim.name,
							aim.subjectID, 
							aim.patientName, 
							aim.template,
							t.getTemplateName(), 
							"", aim.projectID,"","",seriesUID, false);					
					UserProjectService.pendingPNGs.remove(seriesUID);
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
