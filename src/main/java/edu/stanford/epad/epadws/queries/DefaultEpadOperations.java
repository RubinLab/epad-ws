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

import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.Calibration;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pixelmed.dicom.ImageToDicom;
import com.pixelmed.dicom.SOPClass;
import com.pixelmed.dicom.UIDGenerator;

import edu.stanford.epad.common.dicom.DCM4CHEEImageDescription;
import edu.stanford.epad.common.dicom.DCM4CHEEUtil;
import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.common.pixelmed.PixelMedUtils;
import edu.stanford.epad.common.pixelmed.SegmentedProperty;
import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADFileUtils;
import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.dtos.AnnotationStatus;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
import edu.stanford.epad.dtos.EPADDSOFrame;
import edu.stanford.epad.dtos.EPADEventLog;
import edu.stanford.epad.dtos.EPADEventLogList;
import edu.stanford.epad.dtos.EPADFile;
import edu.stanford.epad.dtos.EPADFileList;
import edu.stanford.epad.dtos.EPADFrame;
import edu.stanford.epad.dtos.EPADFrameList;
import edu.stanford.epad.dtos.EPADImage;
import edu.stanford.epad.dtos.EPADImageList;
import edu.stanford.epad.dtos.EPADObjectList;
import edu.stanford.epad.dtos.EPADProject;
import edu.stanford.epad.dtos.EPADProjectList;
import edu.stanford.epad.dtos.EPADProjectTemplate;
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplate;
import edu.stanford.epad.dtos.EPADTemplateContainer;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADTemplateUsage;
import edu.stanford.epad.dtos.EPADTemplateUsageList;
import edu.stanford.epad.dtos.EPADUsage;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudy;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.EPADWorklistSubject;
import edu.stanford.epad.dtos.EPADWorklistSubjectList;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.dtos.StudyProcessingStatus;
import edu.stanford.epad.dtos.TaskStatus;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DCM4CHEESeriesList;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudy;
import edu.stanford.epad.dtos.internal.DCM4CHEEStudyList;
import edu.stanford.epad.dtos.internal.DICOMElement;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.dtos.internal.XNATProject;
import edu.stanford.epad.dtos.internal.XNATSubject;
import edu.stanford.epad.dtos.internal.XNATUserList;
import edu.stanford.epad.epadws.aim.AIMQueries;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.aim.AIMUtil;
import edu.stanford.epad.epadws.aim.dicomsr.Aim2DicomSRConverter;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabase;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeDatabaseOperations;
import edu.stanford.epad.epadws.dcm4chee.Dcm4CheeOperations;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperations;
import edu.stanford.epad.epadws.epaddb.PNGFilesOperations;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.handlers.dicom.DSOUtil;
import edu.stanford.epad.epadws.models.DisabledTemplate;
import edu.stanford.epad.epadws.models.EpadFile;
import edu.stanford.epad.epadws.models.EpadStatistics;
import edu.stanford.epad.epadws.models.EpadStatisticsMonthly;
import edu.stanford.epad.epadws.models.EpadStatisticsTemplate;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.models.EventLog;
import edu.stanford.epad.epadws.models.FileType;
import edu.stanford.epad.epadws.models.NonDicomSeries;
import edu.stanford.epad.epadws.models.Project;
import edu.stanford.epad.epadws.models.ProjectToSubjectToStudy;
import edu.stanford.epad.epadws.models.ProjectToTemplate;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Study;
import edu.stanford.epad.epadws.models.Subject;
import edu.stanford.epad.epadws.models.User;
import edu.stanford.epad.epadws.models.UserRole;
import edu.stanford.epad.epadws.models.WorkList;
import edu.stanford.epad.epadws.models.WorkListToStudy;
import edu.stanford.epad.epadws.models.WorkListToSubject;
import edu.stanford.epad.epadws.processing.pipeline.task.DSOEvaluationTask;
import edu.stanford.epad.epadws.processing.pipeline.task.StudyDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.task.SubjectDataDeleteTask;
import edu.stanford.epad.epadws.processing.pipeline.watcher.Dcm4CheeDatabaseWatcher;
import edu.stanford.epad.epadws.security.EPADSession;
import edu.stanford.epad.epadws.security.EPADSessionOperations;
import edu.stanford.epad.epadws.security.IdGenerator;
import edu.stanford.epad.epadws.service.DefaultEpadProjectOperations;
import edu.stanford.epad.epadws.service.DefaultWorkListOperations;
import edu.stanford.epad.epadws.service.EpadProjectOperations;
import edu.stanford.epad.epadws.service.EpadWorkListOperations;
import edu.stanford.epad.epadws.service.UserProjectService;
import edu.stanford.epad.epadws.xnat.XNATDeletionOperations;
import edu.stanford.epad.epadws.xnat.XNATUtil;
import edu.stanford.hakan.aim4api.base.AimException;
import edu.stanford.hakan.aim4api.base.CD;
import edu.stanford.hakan.aim4api.base.ST;
import edu.stanford.hakan.aim4api.compability.aimv3.ImageAnnotation;
import edu.stanford.hakan.aim4api.compability.aimv3.Lexicon;
import edu.stanford.hakan.aim4api.usage.AnnotationBuilder;
import edu.stanford.hakan.aim4api.usage.AnnotationValidator;

// TODO Too long - separate in to multiple classes

public class DefaultEpadOperations implements EpadOperations
{
	private static final EPADLogger log = EPADLogger.getInstance();

	private static final DefaultEpadOperations ourInstance = new DefaultEpadOperations();

	private final EpadDatabaseOperations epadDatabaseOperations = EpadDatabase.getInstance().getEPADDatabaseOperations();
	private final Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations = Dcm4CheeDatabase.getInstance()
			.getDcm4CheeDatabaseOperations();
	private final EpadProjectOperations projectOperations = DefaultEpadProjectOperations.getInstance();
	private final EpadWorkListOperations workListOperations = DefaultWorkListOperations.getInstance();

	private DefaultEpadOperations()
	{
	}

	public static DefaultEpadOperations getInstance()
	{
		return ourInstance;
	}
	
	
	/**
	 * calculates a combined uid w/ uid_name and limiting it to 128 chars
	 * first checks if the combined key exists in db
	 * if not checks if the uid exists with different name, 
	 * if so returns the calculated combined key otherwise returns the id after replacing / with %2F
	 * this was implemented for handling animal data which has multiple patient names with a single patient id
	 * 
	 */
	@Override
	public String getUniquePatientID(String dicomPatientID,String dicomPatientName){
		try {
			
			String validDicomPatientID=dicomPatientID.replace("/", "-");
			String uniquePatientID=validDicomPatientID;
			//construct the combined id uid_name. db field is 128 chars
			String combinedDicomPatientID= validDicomPatientID+"_"+dicomPatientName.replace("^", "").replaceAll("\\s", "").trim();
			if (combinedDicomPatientID.length()>128)
				combinedDicomPatientID=combinedDicomPatientID.substring(0,127);
			
			Subject subject = projectOperations.getSubject(combinedDicomPatientID);
			
			if (subject!=null) {
				if (dicomPatientName.replace("^", "").trim().equalsIgnoreCase(subject.getName().replace("^", "").trim()))
						uniquePatientID=combinedDicomPatientID;
			}else{
				
				subject = projectOperations.getSubject(validDicomPatientID);
			
				if (subject!= null && !dicomPatientName.replace("^", "").trim().equalsIgnoreCase(subject.getName().replace("^", "").trim())){ //if db record exists but the name is different
					uniquePatientID=combinedDicomPatientID;
				}
					
				
			}
			log.info("Unique patient id is "+uniquePatientID);
			return uniquePatientID;
		} catch (Exception e) {
			log.warning("Couldn't check the uniqueness of dicom patient id returning the original", e);
		}
		return dicomPatientID.replace("/", "-");
	}

	/**
	 * Get operations
	 */

	@Override
	public EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount, boolean ignoreSystem) throws Exception
	{
		return getProjectDescriptions(username, sessionID, searchFilter, annotationCount, ignoreSystem, false);
	}
	@Override
	public EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount, boolean ignoreSystem, boolean includeAnnotationStatus) throws Exception
	{
		if (searchFilter.hasAnnotationMatch()) annotationCount = true;
		EPADProjectList epadProjectList = new EPADProjectList();
		long starttime = System.currentTimeMillis();
		List<Project> projects = new ArrayList<Project>();
		List projectList =	projectOperations.getProjectsForUser(username);
		projectList = projectOperations.sort(projectList, "name", true);
		projects.addAll(projectList);
		long gettime = System.currentTimeMillis();
		for (Project project : projects) {
			if (ignoreSystem && (project.getProjectId().equals(EPADConfig.xnatUploadProjectID) || project.getProjectId().equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned"))))
				continue;
			EPADProject epadProject = project2EPADProject(sessionID, username, project, searchFilter, annotationCount, includeAnnotationStatus);

			if (epadProject != null)
			{
				//log.info("project " + epadProject.id + " aim count:" + epadProject.numberOfAnnotations);
				epadProjectList.addEPADProject(epadProject);
			}
		}
		long convtime = System.currentTimeMillis();
		log.info("Time to get " + epadProjectList.ResultSet.totalRecords + " projects:" + (gettime-starttime) + " msecs, to convert:" + (convtime-gettime) + " msecs");
		return epadProjectList;
	}

	@Override
	public EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID, boolean annotationCount) throws Exception
	{
		return getProjectDescription(projectReference, username, sessionID, annotationCount,false);
	}
	@Override
	public EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID, boolean annotationCount, boolean includeAnnotationStatus) throws Exception
	{
		Project project = projectOperations.getProjectForUser(username, projectReference.projectID);
		if (project != null)
			return project2EPADProject(sessionID, username, project, new EPADSearchFilter(), annotationCount, includeAnnotationStatus);
		return null;
	}

	@Override
	public EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter, int start, int count, String sortField, boolean annotationCount) throws Exception
	{
		return getSubjectDescriptions(projectID, username, sessionID, searchFilter, start, count, sortField, annotationCount, false);
	}

	@Override
	public EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter, int start, int count, String sortField, boolean annotationCount, boolean includeAnnotationStatus) throws Exception
	{
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		List<Subject> subjects = projectOperations.getSubjectsForProject(projectID);
		if (count > 0 && !searchFilter.hasSomeMatchCriteria() && subjects.size() > (start+count))
		{
			subjects = subjects.subList(start, start+count);
		}
		if (EPADConfig.xnatUploadProjectID.equals(projectID))
		{
			annotationCount = false;
		}
		if (subjects.size() > 300 && !searchFilter.hasAnnotationMatch())
			annotationCount = false;
		for (Subject subject : subjects) {
			EPADSubject epadSubject = subject2EPADSubject(sessionID, username, subject, projectID, searchFilter, annotationCount, includeAnnotationStatus);
			if (epadSubject != null)
			{
				boolean matchAccessionNumber = true;
				if (searchFilter.hasAccessionNumberMatch())
				{
					matchAccessionNumber = false;
					Set<String> studyUIDsInXNAT = UserProjectService.getStudyUIDsForSubject(projectID,
							epadSubject.subjectID);
					//TODO what is this??
					for (String studyUID: studyUIDsInXNAT)
					{
						SubjectReference subjectReference = new SubjectReference(projectID, epadSubject.subjectID);
						EPADStudyList studyList = getStudyDescriptions(subjectReference, username, sessionID,
								searchFilter);
						if (studyList.ResultSet.totalRecords > 0)
						{
							matchAccessionNumber = true;
							break;
						}
					}					
				}
				if (matchAccessionNumber)
				{
					//log.info("subject " + epadSubject.subjectID + " aim count:" + epadSubject.numberOfAnnotations);
					epadSubjectList.addEPADSubject(epadSubject);
				}
			}
		}
		//		if (count > 0 && searchFilter.hasSomeMatchCriteria() && epadSubjectList.ResultSet.Result.size() > (start+count))
		//		{
		//			epadSubjectList.ResultSet.Result = epadSubjectList.ResultSet.Result.subList(start, start+count);
		//		}
		return epadSubjectList;
	}	

	@Override
	public EPADSubjectList getWorklistSubjectDescriptions(String projectID,
			String username, String workListID, EPADSearchFilter searchFilter, String sessionID, String sortField)
					throws Exception {
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		List<Subject> subjects = workListOperations.getSubjectsForWorkList(workListID);
		for (Subject subject : subjects) {
			if (!subject.getProjectID().equals(projectID)) continue;
			EPADSubject epadSubject = subject2EPADSubject(sessionID, username, subject, projectID, searchFilter, true);
			if (epadSubject != null)
			{
				boolean matchAccessionNumber = true;
				if (searchFilter.hasAccessionNumberMatch())
				{
					matchAccessionNumber = false;
					Set<String> studyUIDsInXNAT = UserProjectService.getStudyUIDsForSubject(projectID,
							epadSubject.subjectID);
					for (String studyUID: studyUIDsInXNAT)
					{
						SubjectReference subjectReference = new SubjectReference(projectID, epadSubject.subjectID);
						EPADStudyList studyList = getStudyDescriptions(subjectReference, username, sessionID,
								searchFilter);
						if (studyList.ResultSet.totalRecords > 0)
						{
							matchAccessionNumber = true;
							break;
						}
					}					
				}
				if (matchAccessionNumber)
				{
					//log.info("subject " + epadSubject.subjectID + " aim count:" + epadSubject.numberOfAnnotations);
					epadSubjectList.addEPADSubject(epadSubject);
				}
			}
		}
		return epadSubjectList;
	}

	@Override
	public EPADSubjectList getUnassignedSubjectDescriptions(String username,
			String sessionID, EPADSearchFilter searchFilter) throws Exception {
		EPADSubjectList epadSubjectList = new EPADSubjectList();
		List<Subject> subjects = projectOperations.getUnassignSubjects();
		for (Subject subject : subjects) {
			EPADSubject epadSubject = subject2EPADSubject(sessionID, username, subject, EPADConfig.xnatUploadProjectID, searchFilter, false);
			if (epadSubject != null)
				epadSubjectList.addEPADSubject(epadSubject);
		}

		return epadSubjectList;
	}

	public EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID) throws Exception
	{
		return getSubjectDescription(subjectReference, username, sessionID,false);
	}
	@Override
	public EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID, boolean includeAnnotationStatus) throws Exception
	{

		Subject subject =null;
		if (subjectReference.projectID == null ){
			subject = projectOperations.getSubject(subjectReference.subjectID);
			if (subject != null) {
				//ml for download. no project id. cannot take status
				EPADSubject esubject = subject2EPADSubject(sessionID, username, subject, subjectReference.projectID, new EPADSearchFilter(), true, includeAnnotationStatus);
				return esubject;
			}
		}else {
			subject = projectOperations.getSubjectForProject(subjectReference.projectID, subjectReference.subjectID);

		}
		if (subject != null) {
			EPADSubject esubject = subject2EPADSubject(sessionID, username, subject, subjectReference.projectID, new EPADSearchFilter(), true, includeAnnotationStatus);
			String status = projectOperations.getUserStatusForProjectAndSubject(username, subjectReference.projectID, subjectReference.subjectID);
			esubject.setUserProjectStatus(status);
			return esubject;
		}
		return null;
	}

	@Override
	public EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception
	{
		return getStudyDescriptions(subjectReference, username, sessionID, searchFilter, false);

	}

	@Override
	public EPADStudyList getStudyDescriptions(String username, String sessionID,
			Integer days) throws Exception
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		List<Study> studies = new ArrayList<Study>();
		Subject subject = null;
		Set<String> studyUIDsInEpad = new HashSet<String>();

		studies = projectOperations.getStudiesOlderThanDays(days);
		for (Study study: studies)
		{
			studyUIDsInEpad.add(study.getStudyUID());
		}
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.getStudies(studyUIDsInEpad);

		for (DCM4CHEEStudy dcm4CheeStudy : dcm4CheeStudyList.ResultSet.Result) {
			subject =projectOperations.getSubjectForStudy(dcm4CheeStudy.studyUID);

			List<NonDicomSeries> series = projectOperations.getNonDicomSeriesForStudy(dcm4CheeStudy.studyUID);
			dcm4CheeStudy.seriesCount = dcm4CheeStudy.seriesCount + series.size();
			EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(sessionID, subject.getProjectID(), subject.getSubjectUID(),
					dcm4CheeStudy, username, false);

			studyUIDsInEpad.remove(epadStudy.studyUID);
			epadStudyList.addEPADStudy(epadStudy);

		}
		for (Study study: studies)
		{
			if (studyUIDsInEpad.contains(study.getStudyUID()))
			{
				List<NonDicomSeries> series = projectOperations.getNonDicomSeriesForStudy(study.getStudyUID());
				String firstSeries = "";
				if (series.size() > 0) firstSeries = series.get(0).getSeriesUID();
				String firstSeriesDate = "";
				if (series.size() > 0) firstSeriesDate = dateformat.format(series.get(0).getCreatedTime());
				String desc = "";
				subject =projectOperations.getSubjectForStudy(study.getStudyUID());

				DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(study.getStudyUID(), subject.getName(),subject.getSubjectUID(), 
						"", formatDateTime(study.getStudyDate()), 
						0, series.size(), firstSeries, firstSeriesDate,
						"", 0, study.getStudyUID(), study.getDescription(), "",
						"", "");
				EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(sessionID, study.getProjectID(), subject.getSubjectUID(),
						dcm4CheeStudy, username);
				epadStudyList.addEPADStudy(epadStudy);
			}
		}
		//		for (Study study: studies)
		//		{
		//			
		//				List<NonDicomSeries> series = projectOperations.getNonDicomSeriesForStudy(study.getStudyUID());
		//				String firstSeries = "";
		//				if (series.size() > 0) firstSeries = series.get(0).getSeriesUID();
		//				String firstSeriesDate = "";
		//				if (series.size() > 0) firstSeriesDate = dateformat.format(series.get(0).getCreatedTime());
		//				
		//				subject =projectOperations.getSubjectForStudy(study.getStudyUID());
		//				
		//				DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(study.getStudyUID(), subject.getName(),subject.getSubjectUID(), 
		//						"", formatDateTime(study.getStudyDate()), 
		//						0, series.size(), firstSeries, firstSeriesDate,
		//						"", 0, study.getStudyUID(), study.getDescription(), "",
		//						"", "");
		//				EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(sessionID, study.getProjectID(), subject.getSubjectUID(),
		//						dcm4CheeStudy, username);
		//				epadStudyList.addEPADStudy(epadStudy);
		//			
		//		}
		return epadStudyList;
	}

	@Override
	public EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean includeAnnotationStatus) throws Exception
	{
		EPADStudyList epadStudyList = new EPADStudyList();
		List<Study> studies = new ArrayList<Study>();
		Subject subject = null;
		Set<String> studyUIDsInEpad = new HashSet<String>();
		subject = projectOperations.getSubject(subjectReference.subjectID);
		boolean unassignedProject = false;
		boolean noProject=false;
		//ml fix for subjects download (null check)
		if (subjectReference.projectID==null) {
			noProject=true;
		} else {
			unassignedProject= subjectReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned"));
		}
		if (unassignedProject || noProject)
		{
			studies = projectOperations.getStudiesForSubject(subjectReference.subjectID);
		}
		else
		{
			studies = projectOperations.getStudiesForProjectAndSubject(subjectReference.projectID, 
					subjectReference.subjectID);

		}
		for (Study study: studies)
		{
			if (noProject || !unassignedProject || new ProjectToSubjectToStudy().getCount("study_id = " + study.getId()) <= 1)
				studyUIDsInEpad.add(study.getStudyUID());
		}
		DCM4CHEEStudyList dcm4CheeStudyList = Dcm4CheeQueries.getStudies(studyUIDsInEpad);

		for (DCM4CHEEStudy dcm4CheeStudy : dcm4CheeStudyList.ResultSet.Result) {
			//ml+Dev for debugging patient mismatch with dcm4chee
			if (!dcm4CheeStudy.patientID.equals(subjectReference.subjectID))
			{
				log.warning("Patient mismatch, Study:" + dcm4CheeStudy.studyUID + " epad patientID:" + subjectReference.subjectID + " dcm4chee patientID:" + dcm4CheeStudy.patientID);
			}
			List<NonDicomSeries> series = projectOperations.getNonDicomSeriesForStudy(dcm4CheeStudy.studyUID);
			dcm4CheeStudy.seriesCount = dcm4CheeStudy.seriesCount + series.size();
			EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(sessionID, subjectReference.projectID, subjectReference.subjectID,
					dcm4CheeStudy, username, includeAnnotationStatus);
			if (epadStudy.studyDescription!=null && !epadStudy.studyDescription.equals("")) {//fill study's description in our db if it exists in dcm4che
				Study dbStudy=projectOperations.getStudy(epadStudy.studyUID);
				if (dbStudy.getDescription()==null || dbStudy.getDescription().equals("")) {
					dbStudy.setDescription(epadStudy.studyDescription);
					dbStudy.save();
				}

			}else {// see if our db has it
				Study dbStudy=projectOperations.getStudy(epadStudy.studyUID);
				if (dbStudy.getDescription()!=null ) {
					epadStudy.studyDescription=dbStudy.getDescription();
					log.info("Setting desc to "+dbStudy.getDescription());
				}
			}
			
			studyUIDsInEpad.remove(epadStudy.studyUID);
			boolean filter = searchFilter.shouldFilterStudy(subjectReference.subjectID, epadStudy.studyAccessionNumber,
					epadStudy.examTypes, epadStudy.numberOfAnnotations);
			if (!filter)
				epadStudyList.addEPADStudy(epadStudy);

		}
		for (Study study: studies)
		{
			if (studyUIDsInEpad.contains(study.getStudyUID()))
			{
				List<NonDicomSeries> series = projectOperations.getNonDicomSeriesForStudy(study.getStudyUID());
				String firstSeries = "";
				if (series.size() > 0) firstSeries = series.get(0).getSeriesUID();
				String firstSeriesDate = "";
				if (series.size() > 0) firstSeriesDate = dateformat.format(series.get(0).getCreatedTime());
				String desc = "";
				DCM4CHEEStudy dcm4CheeStudy = new DCM4CHEEStudy(study.getStudyUID(), subject.getName(), subjectReference.subjectID, 
						"", formatDateTime(study.getStudyDate()), 
						0, series.size(), firstSeries, firstSeriesDate,
						"", 0, study.getStudyUID(), study.getDescription(), "",
						"", "");
				EPADStudy epadStudy = dcm4cheeStudy2EpadStudy(sessionID, subjectReference.projectID, subjectReference.subjectID,
						dcm4CheeStudy, username, includeAnnotationStatus);
				boolean filter = searchFilter.shouldFilterStudy(subjectReference.subjectID, epadStudy.studyAccessionNumber,
						epadStudy.examTypes, epadStudy.numberOfAnnotations);
				if (!filter)
					epadStudyList.addEPADStudy(epadStudy);
			}
		}
		return epadStudyList;
	}

	@Override
	public EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID) throws Exception
	{
		return getStudyDescription(studyReference, username, sessionID, false);
	}

	@Override
	public EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID, boolean includeAnnotationStatus) throws Exception
	{
		boolean found = true;
		String patientID = studyReference.subjectID;
		if (studyReference.projectID != null && patientID != null){
			found = projectOperations.isStudyInProjectAndSubject(studyReference.projectID,
					studyReference.subjectID, studyReference.studyUID);
		}

		if (!found) {
			log.warning("Count not find study " + studyReference.studyUID + " for subject " + studyReference.subjectID
					+ " in project " + studyReference.projectID);
			return null;
		} else {
			DCM4CHEEStudy dcm4CheeStudy = Dcm4CheeQueries.getStudy(studyReference.studyUID);
			if (dcm4CheeStudy != null)
			{
				if (patientID == null) patientID = dcm4CheeStudy.patientID;
				return dcm4cheeStudy2EpadStudy(sessionID, studyReference.projectID, patientID, dcm4CheeStudy, username, includeAnnotationStatus);
			}
			else {
				log.warning("Count not find dcm4chee study " + studyReference.studyUID + " for subject "
						+ studyReference.subjectID + " in project " + studyReference.projectID);
				return null;
			}
		}
	}

	@Override
	public EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID)
	{
		return getSeriesDescription(seriesReference, username, sessionID, false);

	}
	@Override
	public EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID, boolean includeAnnotationStatus)
	{
		String patientID = seriesReference.subjectID;
		DCM4CHEESeries dcm4cheeSeries = Dcm4CheeQueries.getSeries(seriesReference.seriesUID);

		if (dcm4cheeSeries != null)
		{
			if (patientID == null) patientID = dcm4cheeSeries.patientID;
			return dcm4cheeSeries2EpadSeries(sessionID, seriesReference.projectID, patientID, dcm4cheeSeries, username, includeAnnotationStatus);
		}
		else if (seriesReference.seriesUID.equalsIgnoreCase("FLAGGED")){
			try {
				StudyReference studyReference=new StudyReference(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID);
				EPADImageList flaggedImages=getFlaggedImageDescriptions(username, studyReference, sessionID);
				if (flaggedImages!=null && flaggedImages.ResultSet.totalRecords>0) {
					EPADStudy st=null;
					try {
						st=getStudyDescription(studyReference, username, sessionID);
					} catch (Exception e) {
						log.warning("Couldn't get study for flagged images");
					}
					String date=(st!=null)?st.insertDate:"ND";
					String patientName=(st!=null)?st.patientName:"";
						
					EPADSeries series =	new EPADSeries(studyReference.projectID, studyReference.subjectID, patientName, studyReference.studyUID, 
							"FLAGGED", 
							date, 
							"FLAGGED", 
							"", "", "", flaggedImages.ResultSet.totalRecords, 0, 0, "","","",SeriesProcessingStatus.DONE,"","", false);
					return series;
				}
			} catch (Exception e) {
				log.warning("Error getting flagged series", e);
			}
			return null;
		}else {
			try {
				NonDicomSeries ndSeries = projectOperations.getNonDicomSeries(seriesReference.seriesUID);
				if (ndSeries != null) {
					EPADSeries series = new EPADSeries(seriesReference.projectID, seriesReference.subjectID, "", seriesReference.studyUID, 
							ndSeries.getSeriesUID(), 
							dateformat.format(ndSeries.getSeriesDate()), 
							ndSeries.getDescription(), 
							"", "", "", 0, 0, 0, "","","",null,"","", "SEG".equalsIgnoreCase(ndSeries.getModality()));
					series.isNonDicomSeries = true;
					return series;
				}
			} catch (Exception e) {
				log.warning("Error getting non-dicom series", e);
			}
			log.warning("Could not find series description for series " + seriesReference.seriesUID);
			return null;
		}
	}

	@Override
	public EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs)
	{
		return getSeriesDescriptions(studyReference, username, sessionID, searchFilter, filterDSOs, false);
	}

	@Override
	public EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs, boolean includeAnnotationStatus)
	{
		return getSeriesDescriptions(studyReference, username, sessionID, searchFilter, filterDSOs, includeAnnotationStatus, false);
	}
	
	@Override
	public EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs, boolean includeAnnotationStatus, boolean getFlagged)
	{
		EPADSeriesList epadSeriesList = new EPADSeriesList();

		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyReference.studyUID);
		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			EPADSeries epadSeries = dcm4cheeSeries2EpadSeries(sessionID, studyReference.projectID, studyReference.subjectID,
					dcm4CheeSeries, username, includeAnnotationStatus);
			boolean filter = searchFilter.shouldFilterSeries(epadSeries.patientID, epadSeries.patientName,
					epadSeries.examType, epadSeries.numberOfAnnotations);
			//			log.info("Series:" + epadSeries.seriesDescription + " filterDSO:" + filterDSOs + " isDSO:"+ epadSeries.isDSO + " annotation:"+ epadSeries.annotationStatus.toString());
			if (!filter && !(filterDSOs && epadSeries.isDSO))
			{
				if (epadSeries.isDSO){ //bad solution!!
					//ml filter dsos with no permission
					log.info("filter");
					List<EPADAIM> aims = null;
					if (studyReference.projectID!=null && !studyReference.projectID.equals("")){
						aims=this.epadDatabaseOperations.getAIMsByDSOSeries(studyReference.projectID,epadSeries.seriesUID);
						if (aims.size() != 0)
	                    {
	                           epadSeries.referencedSeriesUID = aims.get(0).seriesUID;
	                    }
					}else{
						aims=this.epadDatabaseOperations.getAIMsByDSOSeries(epadSeries.seriesUID);
						 if (aims.size() != 0)
	                     {
	                            epadSeries.referencedSeriesUID = aims.get(0).seriesUID;
	                     }
						log.info("No project id. Returning all series for this project. may download dso series that are in other projects");
					}
					try {
						EPADAIMList aimList = AIMUtil.filterPermittedImageAnnotations(new EPADAIMList(aims), username, sessionID, studyReference.projectID);
						if (aimList!=null && aimList.ResultSet.totalRecords!=0) {
							log.info("putting dso series:"+epadSeries.seriesUID);
							epadSeriesList.addEPADSeries(epadSeries);
						}
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AimException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				//log.info("Series:" + epadSeries.seriesDescription + " createdtime:" + epadSeries.createdTime);
				else
					epadSeriesList.addEPADSeries(epadSeries);
			}
			else if (epadSeries.isDSO)
			{
				// Check if AIM exists
				List<EPADAIM> aims = this.epadDatabaseOperations.getAIMsByDSOSeries(epadSeries.seriesUID);
				if (aims == null || aims.size() == 0)
				{
					log.info("No aim found for DSO:" + epadSeries.seriesUID);
					Set<DICOMFileDescription> dicomFileDescriptions = dcm4CheeDatabaseOperations.getDICOMFilesForSeries(epadSeries.seriesUID);
					if (dicomFileDescriptions.size() > 0)
					{
						File dsoDICOMFile = null;
						DICOMFileDescription lastDSO = dicomFileDescriptions.iterator().next();
						String createdTime = lastDSO.createdTime;
						for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
							if (createdTime.compareTo(dicomFileDescription.createdTime) < 0)
							{
								createdTime = dicomFileDescription.createdTime;
								lastDSO = dicomFileDescription;
							}
						}
						String dicomFilePath = EPADConfig.dcm4cheeDirRoot + "/" + lastDSO.filePath;
						dsoDICOMFile = new File(dicomFilePath);
						try {
							List<ImageAnnotation> ias = AIMQueries.getAIMImageAnnotations(AIMSearchType.SERIES_UID, epadSeries.seriesUID, username, 1, 50);
							if (ias == null || ias.size() == 0)
							{
								AIMUtil.generateAIMFileForDSO(dsoDICOMFile, "shared", studyReference.projectID);
							}
							//							else
							//							{
							//								log.info("Adding entries to annotations table");
							//								Aim aim = new Aim(ias.get(0));
							//								ImageReference reference = new ImageReference(epadSeries.projectID, epadSeries.patientID, epadSeries.studyUID, aim.getFirstSeriesID(), aim.getFirstImageID());
							//								this.epadDatabaseOperations.addDSOAIM(username, reference, epadSeries.seriesUID, ias.get(0).getUniqueIdentifier());
							//							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}



			}

		}
		try {
			List<NonDicomSeries> ndSerieses = projectOperations.getNonDicomSeriesForStudy(studyReference.studyUID);
			for (NonDicomSeries ndSeries: ndSerieses) {
				EPADSeries series = new EPADSeries(studyReference.projectID, studyReference.subjectID, "", studyReference.studyUID, 
						ndSeries.getSeriesUID(), 
						dateformat.format(ndSeries.getSeriesDate()), 
						ndSeries.getDescription(), 
						"", "", "", 0, 0, 0, "","","",null,"","", "SEG".equalsIgnoreCase(ndSeries.getModality()));
				series.isNonDicomSeries = true;
				epadSeriesList.addEPADSeries(series);
			}
		} catch (Exception e) {
			log.warning("Error getting non-dicom series", e);
		}
		
		EPADImageList flaggedImages=getFlaggedImageDescriptions(username, studyReference, sessionID);
		if (flaggedImages!=null && flaggedImages.ResultSet.totalRecords>0) {
			EPADStudy st=null;
			try {
				st=getStudyDescription(studyReference, username, sessionID);
			} catch (Exception e) {
				log.warning("Couldn't get study for flagged images");
			}
			String date=(st!=null)?st.insertDate:"ND";
			String patientName=(st!=null)?st.patientName:"";
				
			EPADSeries series =	new EPADSeries(studyReference.projectID, studyReference.subjectID, patientName, studyReference.studyUID, 
					"FLAGGED", 
					date, 
					"FLAGGED", 
					"", "", "", flaggedImages.ResultSet.totalRecords, 0, 0, "","","",SeriesProcessingStatus.DONE,"","", false);
			epadSeriesList.addEPADSeries(series);
		}
		
		return epadSeriesList;
	}

	Set<String> seriesInProcess = new HashSet<String>();
	
	@Override
	public EPADImageList getFlaggedImageDescriptions(String username, StudyReference studyReference, String sessionID){
		String projectID=null;
		String subjectID=null;
		String studyID=null;
		if (studyReference!=null) {
			projectID=studyReference.projectID;
			subjectID=studyReference.subjectID;
			studyID=studyReference.studyUID;
		}
		List<String> imageUIDs=projectOperations.getFlaggedImageUIDs(username, projectID,subjectID, studyID);
		EPADImageList epadImageList = new EPADImageList();
		for (String imageUID:imageUIDs){
			EPADImage eim=getImageDescription(projectID,imageUID,sessionID);
			eim.isFlaggedImage=true;
			epadImageList.addImage(eim);
		}
		return epadImageList;
	}
	
	@Override
	public void setFlagged(String username, StudyReference studyReference, String imageUID, String sessionID, boolean flag){
		projectOperations.setFlagged(username, imageUID, studyReference.projectID, flag, studyReference.subjectID, studyReference.studyUID);
	}
	
	@Override
	public EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID,
			EPADSearchFilter searchFilter)
	{
	 	return getImageDescriptions(null, seriesReference, sessionID, searchFilter);
	}
	@Override
	public EPADImageList getImageDescriptions(String username,SeriesReference seriesReference, String sessionID,
			EPADSearchFilter searchFilter)
	{
		if (seriesReference.seriesUID.equalsIgnoreCase("FLAGGED")){
			try {
				StudyReference studyReference=new StudyReference(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID);
				return getFlaggedImageDescriptions(username, studyReference, sessionID);
				
			} catch (Exception e) {
				log.warning("Error getting flagged images", e);
				return null;
			}
		}
		
		List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
				seriesReference.studyUID, seriesReference.seriesUID);


		

		int numImages = imageDescriptions.size();
		if (numImages == 0)
			throw new RuntimeException("This series " + seriesReference.seriesUID + " has no images");
		if (numImages == 1)
		{
			List<String> pngs = epadDatabaseOperations.getAllPNGLocations(imageDescriptions.get(0).imageUID);
			if (pngs.size() > 1)
				imageDescriptions.get(0).multiFrameImage = true;
		}
		DICOMElementList suppliedDICOMElementsFirst = getDICOMElements(imageDescriptions.get(0).studyUID,
				imageDescriptions.get(0).seriesUID, imageDescriptions.get(0).imageUID);
		String pixelSpacing1 = getDICOMElement(suppliedDICOMElementsFirst, PixelMedUtils.PixelSpacingCode);
		String rows1 = getDICOMElement(suppliedDICOMElementsFirst, PixelMedUtils.RowsCode);
		String columns1 = getDICOMElement(suppliedDICOMElementsFirst, PixelMedUtils.ColumnsCode);
		
		String modality = getDICOMElement(suppliedDICOMElementsFirst, PixelMedUtils.ModalityCode);
		String sopClassUID = getDICOMElement(suppliedDICOMElementsFirst, PixelMedUtils.SOPClassUIDCode);
		
		DICOMElementList suppliedDICOMElementsLast = getDICOMElements(imageDescriptions.get(numImages-1).studyUID,
				imageDescriptions.get(numImages-1).seriesUID, imageDescriptions.get(numImages-1).imageUID);
		String pixelSpacing2 = getDICOMElement(suppliedDICOMElementsLast, PixelMedUtils.PixelSpacingCode);
		String rows2 = getDICOMElement(suppliedDICOMElementsLast, PixelMedUtils.RowsCode);
		String columns2 = getDICOMElement(suppliedDICOMElementsLast, PixelMedUtils.ColumnsCode);
		boolean getMetaDataForAllImages = false;
		log.debug("pixelSpacing1:" + pixelSpacing1 + " pixelSpacing2:" + pixelSpacing2);
		//if ultrasound send all the images no need to check the others
		if ( modality.equalsIgnoreCase("US") || sopClassUID.equals("1.2.840.10008.5.1.4.1.1.6.1")
				|| ((pixelSpacing1 != null && !pixelSpacing1.equals(pixelSpacing2)) || (rows1 != null && !rows1.equals(rows2)) || (columns1 != null && !columns1.equals(columns2)) ))
		{
			log.info("Series: " +  seriesReference.seriesUID + " returning metadata for all images");
			getMetaDataForAllImages = true;
			epadDatabaseOperations.insertEpadEvent(
					EPADSessionOperations.getSessionUser(sessionID), 
					"This Image may take some time to load. Please do not retry", 
					seriesReference.seriesUID, imageDescriptions.get(0).imageUID,
					seriesReference.subjectID, 
					seriesReference.subjectID, 
					seriesReference.studyUID, 
					seriesReference.projectID,
					"Getting Variable Metadata Slices " + imageDescriptions.size());					
			seriesInProcess.add(seriesReference.seriesUID);
		}
		DICOMElementList defaultDICOMElements = null;
		EPADImageList epadImageList = new EPADImageList();
		int i = 0;
		boolean isFirst = true;
		for (DCM4CHEEImageDescription dcm4cheeImageDescription : imageDescriptions) {
			i++;
			EPADImage epadImage = null;
			if (isFirst) {
				DICOMElementList suppliedDICOMElements = suppliedDICOMElementsFirst;				
				defaultDICOMElements = getDefaultDICOMElements(dcm4cheeImageDescription.studyUID,
						dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID, suppliedDICOMElements, dcm4cheeImageDescription.instanceNumber, dcm4cheeImageDescription.multiFrameImage);

				epadImage = createEPADImage(seriesReference, dcm4cheeImageDescription, suppliedDICOMElements, defaultDICOMElements);
				log.info("Returning DICOM metadata, supplied Elements:" + suppliedDICOMElements.getNumberOfElements() + " default Elements:" + defaultDICOMElements.getNumberOfElements());
				epadImage.multiFrameImage = dcm4cheeImageDescription.multiFrameImage;
				//get if flagged and put it in epadimage
				if (username!=null) {
					epadImage.isFlaggedImage=projectOperations.isFlagged(username,dcm4cheeImageDescription.imageUID,seriesReference.projectID,seriesReference.subjectID, seriesReference.studyUID);
				}
				epadImageList.addImage(epadImage);
				isFirst = false;
			} else { 
				DICOMElementList suppliedDICOMElements = suppliedDICOMElementsFirst;				
				// We do not always add DICOM headers to remaining image descriptions because it would be too expensive
				if (getMetaDataForAllImages) {
					if (i%300 == 0) {
						epadDatabaseOperations.insertEpadEvent(
								EPADSessionOperations.getSessionUser(sessionID), 
								"This Image still being loaded. Please do not retry", 
								seriesReference.seriesUID, imageDescriptions.get(0).imageUID,
								seriesReference.subjectID, 
								seriesReference.subjectID, 
								seriesReference.studyUID, 
								seriesReference.projectID,
								"Getting Variable Metadata Slice " + i);					
					}
					suppliedDICOMElements = replaceSliceSpecificElements(dcm4cheeImageDescription.studyUID,
							dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID,suppliedDICOMElements);	
					//suppliedDICOMElements = getDICOMElements(dcm4cheeImageDescription.studyUID,
					//		dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID);				
					defaultDICOMElements = getDefaultDICOMElements(dcm4cheeImageDescription.studyUID,
							dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID, suppliedDICOMElements, dcm4cheeImageDescription.instanceNumber);
					log.info("Getting metadata for image " + i);
					epadImage = createEPADImage(seriesReference, dcm4cheeImageDescription, suppliedDICOMElements, defaultDICOMElements);
				}
				else
					epadImage = createEPADImage(seriesReference, dcm4cheeImageDescription, new DICOMElementList(), new DICOMElementList());
				//get if flagged and put it in epadimage
				if (username!=null) {
					epadImage.isFlaggedImage=projectOperations.isFlagged(username,dcm4cheeImageDescription.imageUID,seriesReference.projectID,seriesReference.subjectID, seriesReference.studyUID);
				}
				epadImageList.addImage(epadImage);
			}
			//log.info("Image UID:" + epadImage.imageUID + " LossLess:" + epadImage.losslessImage);
		}
		log.info("Returning image list:" + imageDescriptions.size());
		//return after sorting for correct slice order (using image position and orientation)
		//		epadImageList.sort(); //doesnt work after (first slice should hold dicom info), sort before
		return epadImageList;
	}

	@Override
	public EPADImage getImageDescription(ImageReference imageReference, String sessionID)
	{
		DCM4CHEEImageDescription dcm4cheeImageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageReference);
		List<String> pngs = epadDatabaseOperations.getAllPNGLocations(imageReference.imageUID);
		if (pngs.size() > 1)
			dcm4cheeImageDescription.multiFrameImage = true;
		DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference);
		DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, suppliedDICOMElements);

		EPADImage eImage = createEPADImage(imageReference, dcm4cheeImageDescription, suppliedDICOMElements, defaultDICOMElements);
		eImage.multiFrameImage = dcm4cheeImageDescription.multiFrameImage;
		log.info("Returning DICOM metadata, supplied Elements:" + suppliedDICOMElements.getNumberOfElements() + " default Elements:" + defaultDICOMElements.getNumberOfElements());
		return eImage;
	}
	
	@Override
	public EPADImage getImageDescription(String projectID, String imageUID, String sessionID)
	{
		DCM4CHEEImageDescription dcm4cheeImageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageUID);
		String subjectID=dcm4CheeDatabaseOperations.getSubjectUIDForStudy(dcm4cheeImageDescription.studyUID);
		ImageReference imageReference=new ImageReference(projectID, subjectID, dcm4cheeImageDescription.studyUID, dcm4cheeImageDescription.seriesUID, dcm4cheeImageDescription.imageUID);
		List<String> pngs = epadDatabaseOperations.getAllPNGLocations(imageUID);
		if (pngs.size() > 1)
			dcm4cheeImageDescription.multiFrameImage = true;
		DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference);
		DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, suppliedDICOMElements);

		EPADImage eImage = createEPADImage(imageReference, dcm4cheeImageDescription, suppliedDICOMElements, defaultDICOMElements);
		eImage.multiFrameImage = dcm4cheeImageDescription.multiFrameImage;
		log.info("Returning DICOM metadata, supplied Elements:" + suppliedDICOMElements.getNumberOfElements() + " default Elements:" + defaultDICOMElements.getNumberOfElements());
		return eImage;
	}

	
	@Override
	public EPADFrameList getFrameDescriptions(ImageReference imageReference)
	{
		return getFrameDescriptions(imageReference,false,false);
		
	}
	@Override
	public EPADFrameList getFrameDescriptions(ImageReference imageReference, boolean all, boolean pixelData)
	{
		if (imageReference.seriesUID.equals("*")) { //ml no series uid. probably dso. fill it!
			imageReference.seriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(imageReference.imageUID);
			log.info("image reference of image "+imageReference.imageUID +" series uid filled with "+ imageReference.seriesUID);
		}
		if (imageReference.studyUID.equals("*")) { //ml no study uid. probably dso. fill it!
			imageReference.studyUID = dcm4CheeDatabaseOperations.getStudyUIDForSeries(imageReference.seriesUID);
			log.info("image reference of image "+imageReference.imageUID +" studyUID uid filled with "+ imageReference.studyUID);
		}
		DCM4CHEEImageDescription dcm4cheeImageDescription = dcm4CheeDatabaseOperations.getImageDescription(imageReference);
		List<EPADFrame> frames = new ArrayList<>();

		if (dcm4cheeImageDescription != null && isDSO(dcm4cheeImageDescription)) {
			log.info("Getting referenced series for DSO, subjectID:" + imageReference.subjectID 
					+ " seriesID:" +imageReference.seriesUID + " imageUID:" + imageReference.imageUID);
			SegmentedProperty catTypeProp=new SegmentedProperty();
			DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference,catTypeProp);
			List<DICOMElement> referencedSOPInstanceUIDDICOMElements = getDICOMElementsByCode(suppliedDICOMElements,
					PixelMedUtils.ReferencedSOPInstanceUIDCode);
			int numberOfFrames = getNumberOfFrames(imageReference.imageUID, suppliedDICOMElements);
			int numberOfSegments = getNumberOfSegments(suppliedDICOMElements);
			log.info("numberOfFrames for " + imageReference.imageUID + ":" + numberOfFrames + " numberOfSegments:" + numberOfSegments);

			if (numberOfFrames > 0 && numberOfSegments < 2) {
				DICOMElement firstDICOMElement = referencedSOPInstanceUIDDICOMElements.get(0);
				String studyUID = imageReference.studyUID; // DSO will be in same study as original images
				String referencedFirstImageUID = firstDICOMElement.value;
				String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedFirstImageUID);
				int i = 1;
				while (referencedSeriesUID == null || referencedSeriesUID.equals("") && i < referencedSOPInstanceUIDDICOMElements.size())
				{
					firstDICOMElement = referencedSOPInstanceUIDDICOMElements.get(i);
					referencedFirstImageUID = firstDICOMElement.value;
					referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedFirstImageUID);
					i++;
				}

				if (!referencedSeriesUID.equals("")) {
					
					DICOMElementList referencedDICOMElements = getDICOMElements(studyUID, referencedSeriesUID,
							referencedFirstImageUID );
					DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, referencedDICOMElements);
					boolean isFirst = true;
					List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
							studyUID, referencedSeriesUID);
					int instanceOffset = imageDescriptions.size();
					Map<String, DCM4CHEEImageDescription> descMap = new HashMap<String, DCM4CHEEImageDescription>();
					for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
						descMap.put(imageDescription.imageUID, imageDescription);
						if (imageDescription.instanceNumber < instanceOffset)
							instanceOffset = imageDescription.instanceNumber;
					}
					List<DCM4CHEEImageDescription> referencedImages = new ArrayList<DCM4CHEEImageDescription>();
					for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
						String referencedImageUID = dicomElement.value;
						DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = descMap.get(referencedImageUID);
						referencedImages.add(dcm4cheeReferencedImageDescription);
					}
					
					if (instanceOffset == 0) instanceOffset = 1;
					if (referencedSOPInstanceUIDDICOMElements.size() < imageDescriptions.size())
						instanceOffset = 1;
					int index = 0;
					boolean instanceOneFound = false;
					int instanceCount = 0;
					log.info("is multiframe="+imageDescriptions.get(0).multiFrameImage);
					if (referencedSOPInstanceUIDDICOMElements.size()==1 && imageDescriptions.get(0).multiFrameImage) {
						log.info("This is a dso on a multiframe image. lets write the pngs. assuming starting from 0!");
						for (int frameNumber=0; frameNumber<numberOfFrames; frameNumber++) {
							String referencedImageUID = referencedSOPInstanceUIDDICOMElements.get(0).value;
							DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = referencedImages.get(0);
							index++;
							if (dcm4cheeReferencedImageDescription == null)
							{
								// Note: These referenced images that are not found probably are extra images referenced in the DICOM. 
								//		 There seems to be no way to tell them apart using this PixelMed api - need to use something else
								log.info("Did not find referenced image, seriesuid:" + referencedSeriesUID + " imageuid:" + referencedImageUID 
										+ " for DSO seriesUID:" + imageReference.seriesUID + " DSO imageUID:" + imageReference.imageUID);
								continue;
							}
							String insertDate = dcm4cheeReferencedImageDescription.createdTime;
							String imageDate = dcm4cheeReferencedImageDescription.contentTime;
							String sliceLocation = dcm4cheeReferencedImageDescription.sliceLocation;
							
							String losslessImage = getPNGMaskPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
									frameNumber);
							String contourImage = "";
							contourImage = getPNGContourPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
									frameNumber);
							String lossyImage = ""; // We do not have a lossy image for the DSO frame
							String sourceLosslessImage = getPNGPath(studyUID, referencedSeriesUID, referencedImageUID);
							String sourceLossyImage = getWADOPath(studyUID, referencedSeriesUID, referencedImageUID);
							//log.info("Frame:" + frameNumber + " losslessImage:" + losslessImage);
							
							
							if (isFirst || all) {
								EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
										imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
										sliceLocation, frameNumber, losslessImage, lossyImage, suppliedDICOMElements, defaultDICOMElements,
										referencedSeriesUID, referencedImageUID, sourceLosslessImage, sourceLossyImage, catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
								frames.add(frame);
								isFirst = false;
							} else { // We do not add DICOM headers to remaining frame descriptions because it would be too expensive
								EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
										imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
										sliceLocation, frameNumber, losslessImage, lossyImage, new DICOMElementList(),
										new DICOMElementList(), referencedSeriesUID, referencedImageUID, sourceLosslessImage,
										sourceLossyImage,catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
								frames.add(frame);
							}
						}
					}else{
						for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
							String referencedImageUID = dicomElement.value;
							DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = referencedImages.get(index);
							index++;
							if (dcm4cheeReferencedImageDescription == null)
							{
								// Note: These referenced images that are not found probably are extra images referenced in the DICOM. 
								//		 There seems to be no way to tell them apart using this PixelMed api - need to use something else
								log.info("Did not find referenced image, seriesuid:" + referencedSeriesUID + " imageuid:" + referencedImageUID 
										+ " for DSO seriesUID:" + imageReference.seriesUID + " DSO imageUID:" + imageReference.imageUID);
								continue;
							}
							String insertDate = dcm4cheeReferencedImageDescription.createdTime;
							String imageDate = dcm4cheeReferencedImageDescription.contentTime;
							String sliceLocation = dcm4cheeReferencedImageDescription.sliceLocation;
							int instanceNumber = dcm4cheeReferencedImageDescription.instanceNumber;
							// In case all instanceNumbers are 1
							if (instanceNumber == 1 && !instanceOneFound)
							{
								instanceOneFound = true;
								instanceCount = 1;
							}
							else if (instanceNumber == 1 && instanceOneFound)
							{
								instanceCount++;
								instanceNumber = instanceCount;
							}
							int frameNumber = instanceNumber - instanceOffset; // Frames 0-based, instances 1 or more
							String losslessImage = getPNGMaskPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
									frameNumber);
							String contourImage = "";
							contourImage = getPNGContourPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
									frameNumber);
							String lossyImage = ""; // We do not have a lossy image for the DSO frame
							String sourceLosslessImage = getPNGPath(studyUID, referencedSeriesUID, referencedImageUID);
							String sourceLossyImage = getWADOPath(studyUID, referencedSeriesUID, referencedImageUID);
							//log.info("Frame:" + frameNumber + " losslessImage:" + losslessImage);
							
							
							if (isFirst || all) {
								EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
										imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
										sliceLocation, frameNumber, losslessImage, lossyImage, suppliedDICOMElements, defaultDICOMElements,
										referencedSeriesUID, referencedImageUID, sourceLosslessImage, sourceLossyImage, catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
								frames.add(frame);
								isFirst = false;
							} else { // We do not add DICOM headers to remaining frame descriptions because it would be too expensive
								EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
										imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
										sliceLocation, frameNumber, losslessImage, lossyImage, new DICOMElementList(),
										new DICOMElementList(), referencedSeriesUID, referencedImageUID, sourceLosslessImage,
										sourceLossyImage,catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
								frames.add(frame);
							}
						} 
					}
					log.info("Returning :" + frames.size() + " frames for DSO");
					return new EPADFrameList(frames);
				} else {
					log.warning("Could not find original series for DSO image " + imageReference.imageUID + " in series "
							+ imageReference.seriesUID);
				}
			} else if (numberOfFrames > 0 && numberOfSegments > 0) {
				log.debug("Multi-segment DSO frames, numberOfSegment:" + numberOfSegments);
				referencedSOPInstanceUIDDICOMElements = getDICOMElementsByCodeWithParent(suppliedDICOMElements,
						PixelMedUtils.ReferencedSOPInstanceUIDCode,
						"Source Image Sequence");
				List<DICOMElement> segmentNumberDICOMElements = getDICOMElementsByCodeWithParent(suppliedDICOMElements,
						PixelMedUtils.ReferencedSegmentNumberCode,
						"Segment Identification Sequence");
				log.debug("Number frames:" + numberOfFrames + " referencedUIDs:" + referencedSOPInstanceUIDDICOMElements.size() + " segmenNumbers:" + segmentNumberDICOMElements.size());
				String studyUID = imageReference.studyUID; // DSO will be in same study as original images
				String referencedFirstImageUID = referencedSOPInstanceUIDDICOMElements.get(0).value;
				String referencedSeriesUID = dcm4CheeDatabaseOperations.getSeriesUIDForImage(referencedFirstImageUID);
				if (!referencedSeriesUID.equals("")) {
					DICOMElementList referencedDICOMElements = getDICOMElements(studyUID, referencedSeriesUID,
							referencedFirstImageUID);
					DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, referencedDICOMElements);
					boolean isFirst = true;
					List<DCM4CHEEImageDescription> imageDescriptions = dcm4CheeDatabaseOperations.getImageDescriptions(
							studyUID, referencedSeriesUID);
					Map<String, DCM4CHEEImageDescription> descMap = new HashMap<String, DCM4CHEEImageDescription>();
					for (DCM4CHEEImageDescription imageDescription : imageDescriptions) {
						descMap.put(imageDescription.imageUID, imageDescription);
					}
					List<DCM4CHEEImageDescription> referencedImages = new ArrayList<DCM4CHEEImageDescription>();
					for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
						String referencedImageUID = dicomElement.value;
						DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = descMap.get(referencedImageUID);
						referencedImages.add(dcm4cheeReferencedImageDescription);
					}
					int index = 0;
					for (DICOMElement dicomElement : referencedSOPInstanceUIDDICOMElements) {
						String referencedImageUID = dicomElement.value;
						DCM4CHEEImageDescription dcm4cheeReferencedImageDescription = descMap.get(referencedImageUID);
						if (dcm4cheeReferencedImageDescription == null)
						{
							// Note: These referenced images that are not found probably are extra images referenced in the DICOM. 
							//		 There seems to be no way to tell them apart using this PixelMed api - need to use something else
							log.info("Did not find referenced image, seriesuid:" + referencedSeriesUID + " imageuid:" + referencedImageUID 
									+ " for DSO seriesUID:" + imageReference.seriesUID + " DSO imageUID:" + imageReference.imageUID);
							continue;
						}
						String insertDate = dcm4cheeReferencedImageDescription.createdTime;
						String imageDate = dcm4cheeReferencedImageDescription.contentTime;
						String sliceLocation = dcm4cheeReferencedImageDescription.sliceLocation;
						int instanceNumber = dcm4cheeReferencedImageDescription.instanceNumber;
						int frameNumber = instanceNumber - 1; // Frames 0-based, instances 1 or more
						String losslessImage = getPNGMaskPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
								frameNumber, segmentNumberDICOMElements.get(index).value);
						String contourImage = "";
						contourImage = getPNGContourPath(studyUID, imageReference.seriesUID, imageReference.imageUID,
								frameNumber);
						String lossyImage = ""; // We do not have a lossy image for the DSO frame
						String sourceLosslessImage = getPNGPath(studyUID, referencedSeriesUID, referencedImageUID);
						String sourceLossyImage = getWADOPath(studyUID, referencedSeriesUID, referencedImageUID);
						//log.info("Frame:" + frameNumber + " losslessImage:" + losslessImage);
						if (isFirst || all) {
							EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
									imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
									sliceLocation, frameNumber, losslessImage, lossyImage, suppliedDICOMElements, defaultDICOMElements,
									referencedSeriesUID, referencedImageUID, sourceLosslessImage, sourceLossyImage,catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
							frame.segmentNumber = getInt(segmentNumberDICOMElements.get(index).value);
							frame.multiSegment = true;
							frames.add(frame);
							isFirst = false;
						} else { // We do not add DICOM headers to remaining frame descriptions because it would be too expensive
							EPADDSOFrame frame = new EPADDSOFrame(imageReference.projectID, imageReference.subjectID,
									imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
									sliceLocation, frameNumber, losslessImage, lossyImage, new DICOMElementList(),
									new DICOMElementList(), referencedSeriesUID, referencedImageUID, sourceLosslessImage,
									sourceLossyImage,catTypeProp.getId(),catTypeProp.getName(),catTypeProp.getDefColor());
							frame.segmentNumber = getInt(segmentNumberDICOMElements.get(index).value);
							frame.multiSegment = true;
							frames.add(frame);
						}
						index++;
					}
				}
				log.info("Returning :" + frames.size() + " frames for multisegment DSO");
				return new EPADFrameList(frames);
			} else {
				log.warning("Could not find frames for DSO image " + imageReference.imageUID + " in series "
						+ imageReference.seriesUID);
			}
		} else if (dcm4cheeImageDescription == null) {
			try {
				EpadFile file = projectOperations.getEpadFile(null, imageReference.subjectID, imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
				// TODO: Return frames for NIFTI??
			} catch (Exception e) {
				log.warning("Error getting file for non-dicom image " + imageReference.imageUID + " in series "
						+ imageReference.seriesUID, e);
			}

		} else { // Multiframe DICOMS (non-DSO)
			List<String> pngs = epadDatabaseOperations.getAllPNGLocations(imageReference.imageUID);
			if (pngs.size() > 1)
			{
				// Sort by frame number
				for (int i = 0; i < pngs.size(); i++)
				{
					for (int j = i; j < pngs.size(); j++)
					{
						String pngNamei = pngs.get(i).substring(pngs.get(i).lastIndexOf("/")+1);
						int framei = getInt(pngNamei.substring(0, pngNamei.indexOf(".")));
						String pngNamej = pngs.get(j).substring(pngs.get(j).lastIndexOf("/")+1);
						int framej = getInt(pngNamej.substring(0, pngNamej.indexOf(".")));
						if (framei > framej)
						{
							String temp = pngs.get(i);
							pngs.set(i, pngs.get(j));
							pngs.set(j, temp);
						}
					}
					//log.debug("png " + i + ":" + pngs.get(i).substring(pngs.get(i).lastIndexOf("/")+1));
				}
				Map<String, String> pixelValues=null;
				if (pixelData) {
					pixelValues= epadDatabaseOperations.getPixelValues(imageReference.imageUID);
				}
				DICOMElementList suppliedDICOMElements = getDICOMElements(imageReference);
				DICOMElementList defaultDICOMElements = getDefaultDICOMElements(imageReference, suppliedDICOMElements);
				String insertDate = dcm4cheeImageDescription.createdTime;
				String imageDate = dcm4cheeImageDescription.contentTime;
				String sliceLocation = dcm4cheeImageDescription.sliceLocation;
				String lossyImage = getWADOPath(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);

				for (int i = 0; i < pngs.size(); i++)
				{
					
					String pixelValue=null;
					if (pixelData) {
						pixelValue=pixelValues.get(pngs.get(i));
					}
					if (i == 0 || all) {
						EPADFrame frame = new EPADFrame(imageReference.projectID, imageReference.subjectID,
								imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
								sliceLocation, i, pngs.get(i), lossyImage, suppliedDICOMElements, defaultDICOMElements,pixelValue);
						frames.add(frame);
					} else { // We do not add DICOM headers to remaining frame descriptions because it would be too expensive
						EPADFrame frame = new EPADFrame(imageReference.projectID, imageReference.subjectID,
								imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, insertDate, imageDate,
								sliceLocation, i, pngs.get(i), lossyImage, new DICOMElementList(),
								new DICOMElementList(), pixelValue);
						frames.add(frame);
					}
				}
				log.info("Returning :" + frames.size() + " frames for multiframe image");
				return new EPADFrameList(frames);
			}
			else
				log.warning("Attempt to get frames of non multi-frame image " + imageReference.imageUID + " in series "
						+ imageReference.seriesUID);
		}
		log.info("Returning : 0 frames for DSO");
		return new EPADFrameList();
	}

	private int getInt(String value)
	{
		try {
			return new Integer(value.trim()).intValue();
		} catch (Exception x) {
			return 0;
		}
	}

	public static List<DICOMElement> getDICOMElementsByCode(DICOMElementList dicomElementList, String tagCode)
	{
		Set<DICOMElement> matchingDICOMElements = new LinkedHashSet<>(); // Maintain insertion order

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			// Do not allow duplicates.
			if (dicomElement.tagCode.equalsIgnoreCase(tagCode) && !matchingDICOMElements.contains(dicomElement))
				matchingDICOMElements.add(dicomElement);
		}

		return new ArrayList<>(matchingDICOMElements);
	}

	public static List<DICOMElement> getDICOMElementsByCodeWithParent(DICOMElementList dicomElementList, String tagCode, String parentSequenceName)
	{
		log.debug("Searching for, tagCode:" + tagCode + " parent:" + parentSequenceName);
		List<DICOMElement> matchingDICOMElements = new ArrayList<DICOMElement>();

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			if (dicomElement.tagCode.equalsIgnoreCase(tagCode) && dicomElement.parentSequenceName.equals(parentSequenceName))
				matchingDICOMElements.add(dicomElement);
		}

		return matchingDICOMElements;
	}

	private boolean isDSO(DCM4CHEEImageDescription dcm4cheeImageDescription)
	{
		return dcm4cheeImageDescription.classUID.equals(SOPClass.SegmentationStorage);
	}

	@Override
	public EPADFrame getFrameDescription(FrameReference frameReference, String sessionID)
	{
		return getFrameDescription(frameReference, sessionID, false);
	}
	@Override
	public EPADFrame getFrameDescription(FrameReference frameReference, String sessionID, boolean pixelData)
	{
		ImageReference imageReference=new ImageReference(frameReference.projectID, frameReference.subjectID, frameReference.studyUID, frameReference.seriesUID, 
				frameReference.imageUID);
		//basic version use getFrameDescriptions, but getFrameDescriptions handles frame numbers, makes 0 based (pixelmed created 1 based).
		//so using this for now. TODO create specific frame getter for better performance
		EPADFrameList frames=getFrameDescriptions(imageReference,true,pixelData);
		for (EPADFrame frame:frames.ResultSet.Result) {
			if (frame.frameNumber==frameReference.frameNumber)
				return frame;
		}
		return null;
		
	}

	/**
	 * Creation operations
	 */

	@Override
	public void createSubjectAndStudy(String username, String projectID, String subjectID, String subjectName, String studyUID,
			String sessionID) throws Exception
	{
		if (projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")))
			throw new Exception("Patient can not be added to project:" + projectID);
		projectOperations.createEventLog(username, projectID, subjectID, studyUID, null, null, null, "CREATE SUBJECT", subjectName);
		Subject subject = projectOperations.getSubject(subjectID);
		if (subject == null)
			subject = projectOperations.createSubject(username, subjectID, subjectName, null, "");
		projectOperations.createStudy(username, studyUID, subjectID, "", new Date());
	}

	SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
	@Override
	public EPADSeries createSeries(String username, SeriesReference seriesReference, String description, Date seriesDate, String modality, String referencedSeries, String sessionID) throws Exception
	{
		log.info("Creating new series:" + seriesReference.seriesUID + " description:" + description + " modality:" + modality);
		DCM4CHEESeries dcm4CheeSeries = Dcm4CheeQueries.getSeries(seriesReference.seriesUID);
		if (dcm4CheeSeries != null)
			throw new Exception("Series " + seriesReference.seriesUID + " already exists in DCM4CHE");
		String seriesUID = seriesReference.seriesUID;
		if (seriesUID.equalsIgnoreCase("new"))
		{
			UIDGenerator u = new UIDGenerator();
			seriesUID = u.getNewUID();
			if ("SEG".equals(modality) && (referencedSeries == null || referencedSeries.trim().length() == 0))
				throw new Exception("Segmentation series should specify a referenced Series UID");
		}
		projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, null, "CREATE SERIES", description+":" + seriesDate + ":" + modality);
		if (seriesDate == null) seriesDate = new Date();
		NonDicomSeries series = projectOperations.createNonDicomSeries(username, seriesUID, seriesReference.studyUID, description, seriesDate, modality, referencedSeries);
		projectOperations.addStudyToProject(username, seriesReference.studyUID, seriesReference.subjectID, seriesReference.projectID);
		return new EPADSeries(seriesReference.projectID, seriesReference.subjectID, "", seriesReference.studyUID, seriesUID,
				dateformat.format(seriesDate), description, "", "", "", 0, 0, 0, "","","",null,"","", "seg".equalsIgnoreCase(modality));
	}

	@Override
	public void updateSeriesTags(String username,
			SeriesReference seriesReference, String defaultTags,
			String sessionID) throws Exception {
		epadDatabaseOperations.updateSeriesDefaultTags(seriesReference.seriesUID, defaultTags);
	}


	@Override
	public String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username) throws Exception
	{
		return this.seriesDelete(seriesReference, sessionID, deleteAims, username, false);
	}
	@Override
	public String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username, boolean all) throws Exception
	{
		User user = projectOperations.getUser(username);
		if (!user.isAdmin() && !projectOperations.isOwner(username, seriesReference.projectID))
			throw new Exception("No permissions to delete series:" + seriesReference.seriesUID + " in project " + seriesReference.projectID);
		//defer check and see if it only exists in this project
//		if (!user.isAdmin() && all)
//			throw new Exception("No permissions to delete series:" + seriesReference.seriesUID  + " from system. You are not admin. Please select delete from project. ");

		try {
			projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, null, "DELETE SERIES", "deleteAims:" + deleteAims);
			Set<String>projectIds = UserProjectService.getAllProjectIDs();
			//ml if the user is admin and all is not true check whether the series is in use
			String existInOtherProject="";
			if (!user.isAdmin() || !all) {
				for (String projectId: projectIds)
				{
					if (projectId.equals(seriesReference.projectID)) continue;
					if (projectId.equals(EPADConfig.xnatUploadProjectID)) continue;
					if (projectOperations.isOwner(username, projectId)) continue;
					Set<String> allStudyUIDs = UserProjectService.getAllStudyUIDsForProject(projectId);
					if (allStudyUIDs.contains(seriesReference.studyUID.replace('.', '_')) || allStudyUIDs.contains(seriesReference.studyUID))
					{
						existInOtherProject=projectId;
						break;
						//the user chose to select from project we do not need that anymore. 
						//should we tell user when we are also deleting from system?
//						return "";
						//						log.info("Series " + seriesReference.studyUID + " in use by other projects:" + projectId + ", so series will not be deleted from DCM4CHEE");
						//						return "Series " + seriesReference.seriesUID + " in use by other projects:" + projectId + ", so series will not be deleted from DCM4CHEE";
					}
				}
			}
			if (!existInOtherProject.isEmpty() && !user.isAdmin() && all){
				log.info("Series " + seriesReference.studyUID + " in use by other projects:" + existInOtherProject + " and the user is not admin, so series will not be deleted from DCM4CHEE");
				return "Series " + seriesReference.seriesUID + " in use by other projects:" + existInOtherProject + " and the user is not admin, so series will not be deleted from DCM4CHEE";

			}
			return deleteSeries(seriesReference, deleteAims);
		} catch (Exception e) {
			String msg = "Error deleting Series " + seriesReference.seriesUID + " for patient " + seriesReference.subjectID + " in project " + seriesReference.projectID + ", " + e.getMessage();
			log.warning(msg, e);
			return msg;
		}
	}

	public String deleteSeries(SeriesReference seriesReference, boolean deleteAims)
	{
		String seriesPk = null;
		List<Map<String, String>> seriesList = dcm4CheeDatabaseOperations.getAllSeriesInStudy(seriesReference.studyUID);
		for (Map<String, String> seriesData: seriesList)
		{
			String uid = seriesData.get("series_iuid");
			if (uid.equals(seriesReference.seriesUID))
			{
				seriesPk = seriesData.get("pk");
			}
		}
		if (seriesPk == null)
		{
			log.warning("Series not found in DCM4CHE database, uid:" + seriesReference.seriesUID);
			NonDicomSeries nds;
			try {
				nds = projectOperations.getNonDicomSeries(seriesReference.seriesUID);
				if (nds != null)
				{
					projectOperations.deleteNonDicomSeries(seriesReference.seriesUID);
				}
			} catch (Exception e) {
				throw new RuntimeException("Error deleting series:" + e.getMessage());
			}
			epadDatabaseOperations.deleteSeries(seriesReference.seriesUID);
			if (deleteAims)
				deleteAllSeriesAims(seriesReference.seriesUID, false);
			if (nds == null)
				log.warning("Series not found in DCM4CHE database, uid:" + seriesReference.seriesUID);
			return "";
		}
		if (Dcm4CheeOperations.deleteSeries(seriesReference.seriesUID, seriesPk))
		{
			epadDatabaseOperations.deleteSeries(seriesReference.seriesUID);
			deleteSeriesPNGs(seriesReference);
			if (deleteAims)
				deleteAllSeriesAims(seriesReference.seriesUID, false);
			return "";
		}
		else
		{
			return "Error deleting Series in DCM4CHE database";
		}

	}

	@Override
	public void deleteSeriesPNGs(SeriesReference seriesReference) {
		String pngPath = EPADConfig.getEPADWebServerPNGDir() + "studies/" + seriesReference.studyUID + "/series/" + seriesReference.seriesUID + "/images/";
		log.debug("Deleting all files in:" + pngPath);
		File pngDir = new File(pngPath);
		if (pngDir.exists())
			EPADFileUtils.deleteDirectoryAndContents(pngDir);
	}

	@Override
	public void deleteAllSeriesAims(String seriesUID, boolean deleteDSOs) {
		// Delete all Series AIMs
		SeriesReference sref = new SeriesReference(null, null, null, seriesUID);
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(sref);
		for (EPADAIM aim :aims)
		{
			epadDatabaseOperations.deleteAIM("", aim.aimID);
			AIMUtil.deleteAIM(aim.aimID, aim.projectID);
		}
	}

	@Override
	public void deleteAllAims(String projectID, String subjectID,
			String studyUID, String seriesUID, boolean deleteDSOs) {
		if (projectID == null || projectID.trim().length() == 0) return;
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(projectID, subjectID, studyUID, seriesUID);
		for (EPADAIM aim :aims)
		{
			epadDatabaseOperations.deleteAIM("", aim.aimID);
			AIMUtil.deleteAIM(aim.aimID, aim.projectID);
			if (deleteDSOs && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(aim.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
		}
	}

	@Override
	public Set<String> getExamTypesForSubject(String projectID, String patientID, String sessionID,
			EPADSearchFilter searchFilter) throws Exception
	{
		Set<String> studyUIDs = UserProjectService.getStudyUIDsForSubject(projectID, patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForSubject(String patientID)
	{ // Probably could make this a single query to dcm4chee database.
		Set<String> studyUIDs = Dcm4CheeQueries.getStudyUIDsForPatient(patientID);

		Set<String> examTypes = new HashSet<String>();

		for (String studyUID : studyUIDs)
			examTypes.addAll(getExamTypesForStudy(studyUID));

		return examTypes;
	}

	@Override
	public Set<String> getExamTypesForStudy(String studyUID)
	{ // Probably could make this a single query to dcm4chee database.
		DCM4CHEESeriesList dcm4CheeSeriesList = Dcm4CheeQueries.getSeriesInStudy(studyUID);
		Set<String> examTypes = new HashSet<String>();

		for (DCM4CHEESeries dcm4CheeSeries : dcm4CheeSeriesList.ResultSet.Result) {
			examTypes.add(dcm4CheeSeries.examType);
		}
		return examTypes;
	}

	@Override
	public Set<String> getSeriesUIDsForSubject(String projectID, String patientID, String sessionID,
			EPADSearchFilter searchFilter)
	{
		// Set<String> studyUIDs = XNATQueries.dicomStudyUIDsForSubject(sessionID, projectID, patientID);
		Set<String> studyUIDs = dcm4CheeDatabaseOperations.getStudyUIDsForPatient(patientID);
		Set<String> seriesIDs = new HashSet<String>();

		for (String studyUID : studyUIDs) {
			Set<String> seriesIDsForStudy = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
			seriesIDs.addAll(seriesIDsForStudy);
		}
		return seriesIDs;
	}

	/**
	 * Called by {@link Dcm4CheeDatabaseWatcher} to see if new series have been uploaded to DCM4CHEE that ePAD does not
	 * know about.
	 * <p>
	 * We might want to consider getting series from dcm4chee where their upload time (DCM4CHEESeries.createdTime) is
	 * after ePAD's processing time (EPADSeries.createdTime), indicating a repeat upload.
	 */
	@Override
	public List<DCM4CHEESeries> getNewDcm4CheeSeries()
	{
		List<DCM4CHEESeries> newDcm4CheeSeries = new ArrayList<DCM4CHEESeries>();

		Set<String> allReadyDcm4CheeSeriesUIDs = dcm4CheeDatabaseOperations.getAllReadyDcm4CheeSeriesUIDs();
		Set<String> allEPADSeriesUIDs = epadDatabaseOperations.getAllSeriesUIDsFromEPadDatabase();
		//log.info("Series in dcm4chee:" + allReadyDcm4CheeSeriesUIDs.size()+ " Series in epad:" + allEPADSeriesUIDs.size());
		allReadyDcm4CheeSeriesUIDs.removeAll(allEPADSeriesUIDs);

		List<String> newSeriesUIDs = new ArrayList<String>(allReadyDcm4CheeSeriesUIDs);
		//if (newSeriesUIDs.size() > 0) log.info("newSeriesUIDs:" + newSeriesUIDs.size());

		for (String seriesUID : newSeriesUIDs) {
			DCM4CHEESeries dcm4CheeSeries = Dcm4CheeQueries.getSeries(seriesUID);
			if (dcm4CheeSeries != null) {
				newDcm4CheeSeries.add(dcm4CheeSeries);
			} else
				log.warning("Could not find new series " + seriesUID + " in dcm4chee");
		}
		return newDcm4CheeSeries;
	}

	@Override
	public Set<String> getDeletedDcm4CheeSeries() {
		Set<String> allReadyDcm4CheeSeriesUIDs = dcm4CheeDatabaseOperations.getAllDcm4CheeSeriesUIDs();
		Set<String> allEPADSeriesUIDs = epadDatabaseOperations.getAllSeriesUIDsFromEPadDatabase();
		//log.info("Series in dcm4chee:" + allReadyDcm4CheeSeriesUIDs.size()+ " Series in epad:" + allEPADSeriesUIDs.size());
		allEPADSeriesUIDs.removeAll(allReadyDcm4CheeSeriesUIDs);

		return allEPADSeriesUIDs;
	}

	@Override
	public Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID)
	{
		Set<DICOMFileDescription> dicomFilesWithoutPNGs = new HashSet<DICOMFileDescription>();

		try {
			// Get list of DICOM file descriptions from DCM4CHEE.
			Set<DICOMFileDescription> dicomFileDescriptions = dcm4CheeDatabaseOperations.getDICOMFilesForSeries(seriesUID);

			// Get list of image UIDs in series for images recorded in ePAD database table epaddb.epad_files.
			Set<String> imageUIDs = epadDatabaseOperations.getImageUIDsInSeries(seriesUID);

			// Make a list of image UIDs that have no entry in ePAD files_table.
			for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
				String modality = dicomFileDescription.modality;
				if ("RTPLAN".equals(modality) || "PR".equals(modality) || "SR".equals(modality)) continue; // no images to generate
				if (!imageUIDs.contains(dicomFileDescription.imageUID))
				{
					log.info("ImageUID without png: " + dicomFileDescription.imageUID);
					dicomFilesWithoutPNGs.add(dicomFileDescription);
				}
				else if ("SEG".equalsIgnoreCase(dicomFileDescription.modality))
				{
					File dsoFile = new File(EPADConfig.dcm4cheeDirRoot + "/" + dicomFileDescription.filePath);
					if (!dsoFile.exists())
					{
						try {
							log.info("Downloading remote DICOM file with image " + dicomFileDescription.imageUID + " for series UID " + seriesUID);
							dsoFile = File.createTempFile(dicomFileDescription.imageUID, ".tmp");
							DCM4CHEEUtil.downloadDICOMFileFromWADO(dicomFileDescription, dsoFile);
						} catch (Exception e) {
							log.warning("Exception when downloading DICOM file with series UID " + seriesUID + " and image UID "
									+ dicomFileDescription.imageUID, e);
						}

					}
					if (PixelMedUtils.isDicomSegmentationObject(dsoFile.getAbsolutePath())) { //check if pixelmed thinks it is a segmentation object (does not support surface segmentation yet)
						if (!DSOUtil.checkDSOMaskPNGs(dsoFile))
							dicomFilesWithoutPNGs.add(dicomFileDescription);				
					}
				}
			}
		} catch (Exception e) {
			log.warning("Error finding unprocessed file descriptions: " + e.getMessage(), e);
		}
		return dicomFilesWithoutPNGs;
	}

	@Override
	public Set<DICOMFileDescription> getDICOMFilesInSeries(String seriesUID, String imageUID)
	{
		Set<DICOMFileDescription> dicomFilesWithoutPNGs = new HashSet<DICOMFileDescription>();

		try {
			// Get list of DICOM file descriptions from DCM4CHEE.
			Set<DICOMFileDescription> dicomFileDescriptions = dcm4CheeDatabaseOperations.getDICOMFilesForSeries(seriesUID);

			// Make a list of image UIDs that have no entry in ePAD files_table.
			for (DICOMFileDescription dicomFileDescription : dicomFileDescriptions) {
				if (imageUID == null || imageUID.equals(dicomFileDescription.imageUID))
					dicomFilesWithoutPNGs.add(dicomFileDescription);
			}
		} catch (Exception e) {
			log.warning("Error finding DICOM file descriptions: " + e.getMessage(), e);
		}
		return dicomFilesWithoutPNGs;
	}

	@Override
	public int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate,
			String sessionID, ProjectType type) throws Exception
	{
		if (projectReference.projectID == null || projectReference.projectID.trim().length() == 0)
			throw new Exception("Invalid Project ID");
		projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, null, "CREATE PROJECT", projectName +":" + projectDescription);
		projectOperations.createProject(username, projectReference.projectID, projectName, projectDescription, defaultTemplate, type);
		projectOperations.addUserToProject(username, projectReference.projectID, username, UserRole.OWNER, defaultTemplate);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate,
			String sessionID) throws Exception
	{
		return createProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID, ProjectType.PRIVATE);

	}

	@Override
	public int updateProject(String username,
			ProjectReference projectReference, String projectName,
			String projectDescription, String defaultTemplate, String sessionID, ProjectType type) throws Exception {
		projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, null, "UPDATE PROJECT", projectName +":" + projectDescription);
		if (projectOperations.isOwner(username, projectReference.projectID))
			projectOperations.updateProject(username, projectReference.projectID, projectName, projectDescription, defaultTemplate, type);
		else
			throw new Exception("No privilege to modify project:" + projectReference.projectID);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int updateProject(String username,
			ProjectReference projectReference, String projectName,
			String projectDescription, String defaultTemplate, String sessionID) throws Exception {
		//type was null. left the same but why??
		return updateProject(username, projectReference, projectName, projectDescription, defaultTemplate, sessionID, null);

	}

	@Override
	public int createSubject(String username, SubjectReference subjectReference, String subjectName, Date dob, String gender, String sessionID) throws Exception
	{
		if (subjectReference.projectID != null && subjectReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")))
			throw new Exception("Patient can not be added to project:" + subjectReference.projectID);
		if (subjectReference.subjectID == null || subjectReference.subjectID.trim().length() == 0)
			throw new Exception("Invalid Subject ID");
		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, "CREATE SUBJECT", subjectName +":" + dob + ":" + gender);
		String subjectID = subjectReference.subjectID;
		if (subjectID.equalsIgnoreCase("new"))
		{
			IdGenerator idGenerator = new IdGenerator();
			subjectID = idGenerator.generateId(8);
			Subject subject = projectOperations.getSubject(subjectID);
			while (subject != null)
			{
				subjectID = idGenerator.generateId(8);
				subject = projectOperations.getSubject(subjectID);
			}
		}
		Subject subject = projectOperations.getSubject(subjectID);
		if (subject == null)
			subject = projectOperations.createSubject(username, subjectID, subjectName, null, "");
		if (subjectReference.projectID != null && subjectReference.projectID.length() != 0)
			projectOperations.addSubjectToProject(username, subjectID, subjectReference.projectID);
		if (!EPADConfig.xnatUploadProjectID.equals(subjectReference.projectID))
			projectOperations.addSubjectToProject(username, subjectID, EPADConfig.xnatUploadProjectID);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int updateSubject(String username,
			SubjectReference subjectReference, String subjectName, Date dob,
			String gender, String sessionID) throws Exception {
		Subject subject = projectOperations.getSubject(subjectReference.subjectID);
		if (subject == null)
			throw new Exception("Subject " + subjectReference.subjectID + " not found");
		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, "UPDATE SUBJECT", subjectName +":" + dob + ":" + gender);
		projectOperations.createSubject(username, subjectReference.subjectID, subjectName, dob, gender);
		projectOperations.addSubjectToProject(username, subjectReference.subjectID, subjectReference.projectID);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createStudy(String username, StudyReference studyReference, String description, Date studyDate, String sessionID) throws Exception
	{
		if (studyReference.projectID != null && studyReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")))
			throw new Exception("Study can not be added to project:" + studyReference.projectID);
		if (studyReference.studyUID != null && studyReference.studyUID.trim().length() == 0)
			throw new Exception("Invalid Study UID");
		String studyUID = studyReference.studyUID;
		if (studyUID.equalsIgnoreCase("new"))
		{
			UIDGenerator u = new UIDGenerator();
			studyUID = u.getNewUID();
		}
		Study study = projectOperations.getStudy(studyUID);
		if (study == null)
		{
			projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, null, "CREATE STUDY", description +":" + studyDate);
			study = projectOperations.createStudy(username, studyUID, studyReference.subjectID, description, studyDate);
		}
		if (studyReference.projectID != null && studyReference.projectID.length() != 0)
		{
			projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, null, "ADD STUDY", description +":" + studyDate);
			log.info("adding study:" + studyUID + " to project:" + studyReference.projectID);
			projectOperations.addStudyToProject(username, studyUID, studyReference.subjectID, studyReference.projectID);
		}
		if (!EPADConfig.xnatUploadProjectID.equals(studyReference.projectID))
			projectOperations.addStudyToProject(username, studyUID, studyReference.subjectID, EPADConfig.xnatUploadProjectID);
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createFile(String username, ProjectReference projectReference,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		if (projectReference.projectID != null && projectReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")))
			throw new Exception("Files can not be uploaded to this project:" + projectReference.projectID);
		projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, null, "CREATE FILE", description +":" + fileType);
		if (fileType != null && fileType.equalsIgnoreCase("annotation")) {
			if (AIMUtil.saveAIMAnnotation(uploadedFile, projectReference.projectID, sessionID, username))
				throw new Exception("Error saving AIM file");
		}
		else {
			createFile(username, projectReference.projectID, null, null, null,
					uploadedFile, description, fileType, sessionID);
		}
		return HttpServletResponse.SC_OK;
	}


	@Override
	public void createFile(String username, String projectID, String subjectID, String studyID, String seriesID,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		//		String templateLevelType="image";//default
		//fill if template
		Template template =null;

		if (!projectOperations.hasAccessToProject(username, projectID))
			throw new Exception("No permissions to upload to project " + projectID);
		String filename = uploadedFile.getName();
		log.info("filename:" + filename);
		if (filename.startsWith("temp"))
		{
			int dash = filename.indexOf("-");
			filename = filename.substring(dash+1);
		}
		if (UserProjectService.isDicomFile(uploadedFile))
		{
			createImage(username, projectID, uploadedFile, sessionID);
		}
		else if (uploadedFile.getName().toLowerCase().endsWith(".zip"))
		{
			FileType type = null;
			if (fileType != null && fileType.equals(FileType.PARAMETERS.getName()))
			{
				type = FileType.PARAMETERS;
			}
			if (fileType != null && fileType.equals(FileType.TEMPLATE.getName()))
			{
				type = FileType.TEMPLATE;
			}
			else if (fileType != null && fileType.equals(FileType.IMAGE.getName()))
			{
				type = FileType.IMAGE;
			}
			projectOperations.createFile(username, projectID, subjectID, studyID, seriesID, uploadedFile, filename, description, type);
			log.info("Unzipping " + uploadedFile.getAbsolutePath());
			EPADFileUtils.extractFolder(uploadedFile.getAbsolutePath());
			//to prevent infinite loop for zip uploads
			File parent = uploadedFile.getParentFile();
            File zipDirectory = new File(parent, uploadedFile.getName().substring(0, uploadedFile.getName().length()-4));
            File[] files1 = zipDirectory.listFiles();
            boolean hasDICOMs = false;
            List<File> files = new ArrayList<File>();
            for (File f: files1)
            {
                  files.add(f);
            }
            for (int i = 0; i < files.size(); i++)
            {
                  File file = files.get(i);
                  if (file.isDirectory())
                  {
                         File[] files2 = file.listFiles();
                         for (File f: files2)
                         {
                                files.add(f);
                         }
                         continue;
                  }
			
			
				if (!UserProjectService.isDicomFile(file) || file.getName().toLowerCase().endsWith(".zip"))
				{
					if (file.getName().toLowerCase().endsWith(".xml"))
						createFile(username, projectID, subjectID, studyID, seriesID, file, description, FileType.TEMPLATE.getName(), sessionID);
	                 else
	                	createFile(username, projectID, subjectID, studyID, seriesID, file, description, null, sessionID);
	                 file.delete();
						
				}
				else
				{
					UserProjectService.createProjectEntitiesFromDICOMFile(file, projectID, sessionID, username);
					hasDICOMs = true;
				}
			}
			uploadedFile.delete();
			if (hasDICOMs)
				Dcm4CheeOperations.dcmsnd(zipDirectory, true);			
		}
		else
		{
			FileType type = null;
			if (fileType != null && fileType.equals(FileType.PARAMETERS.getName()))
			{
				type = FileType.PARAMETERS;
			}
			if (fileType != null && fileType.equals(FileType.TEMPLATE.getName()))
			{
				type = FileType.TEMPLATE;
				if (EPADFileUtils.isImage(uploadedFile) || uploadedFile.getName().toLowerCase().endsWith(".zip"))
					throw new Exception("This does not appear to be a template file. The client should check this.");
				//check if the template code exists in the system 
				//				String codeValue=getTemplateCode(uploadedFile);
				//				if (projectOperations.get) {
				//					throw new Exception("Template with code "+ codeValue + " already exists");
				//				}
				if (!EPADFileUtils.isValidXml(uploadedFile, EPADConfig.templateXSDPath))
				{
					String error = EPADFileUtils.validateXml(uploadedFile, EPADConfig.templateXSDPath);
					if (!(error.contains("content of element 'Template' is not complete") && getTemplateType(uploadedFile).startsWith("SEG")))
						throw new Exception("Invalid Template file: " + error);
				}
				//read the file and extract a template object, it will be completed and saved after the file is created 
				template = getFirstTemplateInfo(uploadedFile);
				
				//this should be uncommented once the ui is able to add project template relation from the interface
//				if (templateExists(template.getTemplateCode()))
//					throw new Exception("Invalid Template code "+ template.getTemplateCode() +" already exists in the system ");
				
				projectOperations.createEventLog(username, projectID, subjectID, studyID, seriesID, null, null, uploadedFile.getName(), "UPLOAD TEMPLATE", description, false);
			}
			else if (fileType != null && fileType.equals(FileType.IMAGE.getName()))
			{
				type = FileType.IMAGE;
			}
			else if (EPADFileUtils.isImage(uploadedFile))
			{
				type = FileType.IMAGE;
			}
			log.info("filename:" + filename + " type:" + type);
			if (type == null && filename.toLowerCase().endsWith(".xml"))
			{
				if (EPADFileUtils.isValidXml(uploadedFile, EPADConfig.templateXSDPath)) {
					type = FileType.TEMPLATE;
				} else if (AnnotationValidator.ValidateXML(uploadedFile.getAbsolutePath(), EPADConfig.xsdFilePathV4)) {
					type = FileType.ANNOTATION;
					if (!AIMUtil.saveAIMAnnotation(uploadedFile, projectID, sessionID, username)) {
						return;
					}
					else
						log.warning("Error saving AIM file to Exist DB:" + uploadedFile.getName());									
				} else if (AnnotationValidator.ValidateXML(uploadedFile.getAbsolutePath(), EPADConfig.xsdFilePath)) {
					type = FileType.ANNOTATION;
					if (!AIMUtil.saveAIMAnnotation(uploadedFile, projectID, sessionID, username)) {
						return;
					}
					else
						log.warning("Error saving AIM file to Exist DB:" + uploadedFile.getName());					
				}				
			}
			
			if (type == null && PixelMedUtils.isDicomSR(uploadedFile.getAbsolutePath())) {
				log.info("DicomSR found in createFile. processing");
				type=FileType.DICOMSR;
				Aim2DicomSRConverter converter=new Aim2DicomSRConverter();
				String xml=converter.DicomSR2Aim(uploadedFile.getAbsolutePath(), projectID);
				if (xml==null) {
					log.info("Could not convert from dicom sr");
				}else {
					String tmpAimName="/tmp/tmpAim"+System.currentTimeMillis()+".xml";
					File tmpAim=new File(tmpAimName);
					EPADFileUtils.write(tmpAim, xml);
					log.info("tmp aim path:"+ tmpAim.getAbsolutePath());
					
					if (AnnotationValidator.ValidateXML(tmpAim.getAbsolutePath(), EPADConfig.xsdFilePath)) {
						log.info("xml produced from dicom sr is valid");
						if (AIMUtil.saveAIMAnnotation(tmpAim, projectID, 0, sessionID, username, false, true))
							log.warning("Error processing aim file:" + uploadedFile.getName());
					}
					else 
						log.warning("xml produced from dicom sr is NOT valid");
				}
			}
			
			EpadFile file=null;
			if (type == null || !type.equals(FileType.TEMPLATE)) {
				projectOperations.createEventLog(username, projectID, subjectID, studyID, seriesID, null, null, uploadedFile.getName(), "UPLOAD FILE", description, false);
				file=projectOperations.createFile(username, projectID, subjectID, studyID, seriesID, uploadedFile, filename, description, type);
			}
					//if it is a template put the file information and create the template entry in db
			if (type != null && fileType!=null && fileType.equals(FileType.TEMPLATE.getName())) {
				//temporary fix for uploading same template to different projects. should be removed once the ui is able to add project template relation from the interface
				Template existingTemplate=getTemplate(template.getTemplateCode());
				if (existingTemplate!=null){
					//two different possibilities; either the user uploaded same file to different projects or uses the same code
					EpadFile ef=(EpadFile)projectOperations.getDBObject(EpadFile.class, existingTemplate.getFileId());
					
					
					//if same file just remove the extras and put the project relations
					if (filename.equalsIgnoreCase(ef.getName()) && uploadedFile.length()==ef.getLength() && !projectID.equals(ef.getProjectId()) ) {
						log.info("same file for template="+template.getTemplateName() +" just putting the project relation");
						file=ef;
						template=existingTemplate;
					}
					else {
						throw new Exception("Invalid Template code "+ template.getTemplateCode() +" already exists in the system ");
					}
				} else {
					//get this out of if else once the ui is able to add project template relation from the interface
					file=projectOperations.createFile(username, projectID, subjectID, studyID, seriesID, uploadedFile, filename, description, type, template.getTemplateLevelType());
					template.setFileId(file.getId());
					template.save();
				}
				
				
				
				log.info("template db entry is created for template="+template.getTemplateName());
				projectOperations.setProjectTemplate(username, projectID, template.getTemplateCode(), true);

			}

			if (type != null && type.equals(FileType.IMAGE) && seriesID != null) {
				NonDicomSeries ndSeries = projectOperations.getNonDicomSeries(seriesID);
				if (ndSeries != null && ndSeries.getReferencedSeries() != null && ndSeries.getReferencedSeries().length() > 0) {
					try {
						AIMUtil.generateAIMForNiftiDSO(username, projectID, subjectID, studyID, seriesID, EPADFileUtils.removeExtension(filename), uploadedFile);
					} catch (Exception x) {
						log.warning("Error generating Annotation for Nifti DSO", x);
					}
					try {
						DSOUtil.writePNGMasksForNiftiDSO(subjectID, studyID, seriesID, EPADFileUtils.removeExtension(filename), uploadedFile);
					} catch (Exception x) {
						log.warning("Error generating PNG masks for Nifti DSO", x);
					}
				}
			}
			if (type != null && type.equals(FileType.IMAGE) && filename.endsWith(".nii") && !filename.equalsIgnoreCase(EPADConfig.getParamValue("GroundTruthDSOName", "GroundTruth.nii"))) {
				(new Thread(new DSOEvaluationTask(username, projectID, subjectID, studyID, seriesID, filename))).start();
			}
		}
	}

	private boolean templateExists(String templateCode) throws Exception {
		List<Template> templates=new Template().getObjects("LOWER(templateCode)='"+ templateCode.toLowerCase() +"'");
		if (templates!=null && !templates.isEmpty())
			return true;
		return false;
	}
	
	private List<Template> getTemplates(String templateCode) throws Exception {
		List<Template> templates=new Template().getObjects("LOWER(templateCode)='"+ templateCode.toLowerCase() +"'");
		if (templates!=null && !templates.isEmpty())
			return templates;
		return null;
	}
	
	private Template getTemplate(String templateCode) throws Exception {
		Template template=(Template) new Template().getObject("LOWER(templateCode)='"+ templateCode.toLowerCase() +"'");
		return template;
	}

	private Template getFirstTemplateInfo(File templateFile)
	{
		Template template=new Template();
		try {
			String xml = EPADFileUtils.readFileAsString(templateFile);
			JSONObject root = XML.toJSONObject(xml);
			JSONObject container = root.getJSONObject("TemplateContainer");
			JSONArray templateObjs = new JSONArray();
			try {
				JSONObject templateObj = container.getJSONObject("Template");
				templateObjs.put(templateObj);
			}
			catch (Exception x) {
				templateObjs = container.getJSONArray("Template");
			}
			for (int i = 0; i < templateObjs.length(); i++)
			{
				JSONObject templateObj = templateObjs.getJSONObject(i);
				//extract template information and put it in template
				//returns the first
				template.setTemplateLevelType(templateObj.optString("templateType").toLowerCase());
				if (template.getTemplateLevelType().equals(""))
					template.setTemplateLevelType("image");
				template.setTemplateUID(templateObj.optString("uid"));

				template.setTemplateName(templateObj.optString("name"));
				template.setAuthors(templateObj.optString("authors"));
				template.setVersion(templateObj.optString("version"));
				template.setTemplateCreationDate(templateObj.optString("creationDate"));
				template.setTemplateDescription(templateObj.optString("description"));
				template.setCodingSchemeVersion(templateObj.optString("codingSchemeVersion"));
				template.setTemplateType(templateObj.optString("codeMeaning"));
				template.setTemplateCode(templateObj.optString("codeValue"));
				template.setCodingSchemeDesignator(templateObj.optString("codingSchemeDesignator"));
				template.setModality(templateObj.optString("modality"));
				return template;
			}
		} catch (Exception x) {
		}
		return template;		
	}

	private String getTemplateType(File templateFile)
	{
		try {
			String xml = EPADFileUtils.readFileAsString(templateFile);
			JSONObject root = XML.toJSONObject(xml);
			JSONObject container = root.getJSONObject("TemplateContainer");
			JSONArray templateObjs = new JSONArray();
			try {
				JSONObject templateObj = container.getJSONObject("Template");
				templateObjs.put(templateObj);
			}
			catch (Exception x) {
				templateObjs = container.getJSONArray("Template");
			}
			for (int i = 0; i < templateObjs.length(); i++)
			{
				JSONObject templateObj = templateObjs.getJSONObject(i);
				return templateObj.getString("codeMeaning");
			}
		} catch (Exception x) {
		}
		return "";		
	}

	private String getTemplateLevelType(File templateFile)
	{
		try {
			String xml = EPADFileUtils.readFileAsString(templateFile);
			JSONObject root = XML.toJSONObject(xml);
			JSONObject container = root.getJSONObject("TemplateContainer");
			JSONArray templateObjs = new JSONArray();
			try {
				JSONObject templateObj = container.getJSONObject("Template");
				templateObjs.put(templateObj);
			}
			catch (Exception x) {
				templateObjs = container.getJSONArray("Template");
			}
			for (int i = 0; i < templateObjs.length(); i++)
			{
				JSONObject templateObj = templateObjs.getJSONObject(i);
				if (templateObj!=null && templateObj.getString("templateType")!=null)
					return templateObj.getString("templateType").toLowerCase();
				return "image";
			}
		} catch (Exception x) {
		}
		return "";		
	}

	@Override
	public int createFile(String username, SubjectReference subjectReference,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, uploadedFile.getName(), "CREATE FILE", description +":" + fileType, false);
		if (fileType != null && fileType.equalsIgnoreCase(FileType.ANNOTATION.getName())) {
			if (AIMUtil.saveAIMAnnotation(uploadedFile, subjectReference.projectID, sessionID, username))
				throw new Exception("Error saving AIM file");
		}
		else {
			createFile(username, subjectReference.projectID, subjectReference.subjectID, null, null,
					uploadedFile, description, fileType, sessionID);
		}
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createFile(String username, StudyReference studyReference,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, null, uploadedFile.getName(), "CREATE FILE", description +":" + fileType, false);
		if (fileType != null && fileType.equalsIgnoreCase(FileType.ANNOTATION.getName())) {
			if (AIMUtil.saveAIMAnnotation(uploadedFile, studyReference.projectID, sessionID, username))
				throw new Exception("Error saving AIM file");
		}
		else {
			createFile(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null,
					uploadedFile, description, fileType, sessionID);
		}
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createFile(String username, SeriesReference seriesReference,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, null, uploadedFile.getName(), "CREATE FILE", description +":" + fileType, false);
		return createFile(username, seriesReference, uploadedFile, description, fileType, sessionID, 
				false, null, null);
	}

	@Override
	public int createFile(String username, SeriesReference seriesReference,
			File uploadedFile, String description, String fileType, String sessionID, 
			boolean convertToDICOM, String modality, String instanceNumber) throws Exception {
		projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, null, uploadedFile.getName(), "CREATE FILE", description +":" + fileType + ":" + modality, false);
		if (fileType != null && fileType.equalsIgnoreCase(FileType.ANNOTATION.getName())) {
			if (AIMUtil.saveAIMAnnotation(uploadedFile, seriesReference.projectID, sessionID, username))
				throw new Exception("Error saving AIM file");
		}
		else {
			if (convertToDICOM) {
				Subject subject = projectOperations.getSubject(seriesReference.subjectID);
				String patientName = "";
				if (subject != null)
					patientName = subject.getName();
				// TODO: use modality
				File dicomFile = new File(replaceExtension(uploadedFile.getAbsolutePath(), "dcm"));
				new ImageToDicom(uploadedFile.getAbsolutePath(), dicomFile.getAbsolutePath(), patientName, 
						seriesReference.seriesUID, 
						seriesReference.studyUID, 
						seriesReference.seriesUID, instanceNumber);
				uploadedFile.delete();
				createImage(username, seriesReference.projectID, dicomFile, sessionID);
			} else {
				createFile(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID,
						uploadedFile, description, fileType, sessionID);
			}
		}
		return HttpServletResponse.SC_OK;
	}

	private String replaceExtension(String filePath, String newExt) {
		int dot = filePath.lastIndexOf(".");
		if (dot != -1)
			filePath = filePath.substring(0, dot);
		filePath = filePath + "." + newExt;
		return filePath;
	}

	@Override
	public int createFile(String username, ImageReference imageReference,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception {
		projectOperations.createEventLog(username, imageReference.projectID, imageReference.subjectID, imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, null, uploadedFile.getName(), "CREATE FILE", description +":" + fileType, false);
		if (fileType != null && fileType.equalsIgnoreCase(FileType.ANNOTATION.getName())) {
			if (AIMUtil.saveAIMAnnotation(uploadedFile, imageReference.projectID, sessionID, username))
				throw new Exception("Error saving AIM file");
		}
		else {
			createFile(username, imageReference.projectID, imageReference.subjectID, imageReference.studyUID, null,
					uploadedFile, description, fileType, sessionID);
		}
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createImage(String username, String projectID,
			File dicomFile, String sessionID) throws Exception {
		projectOperations.createEventLog(username, projectID, null, null, null, null, null, dicomFile.getName(), "UPLOAD DICOM", dicomFile.getName(), false);
		if (UserProjectService.isDicomFile(dicomFile))
		{
			UserProjectService.createProjectEntitiesFromDICOMFile(dicomFile, projectID, sessionID, username);
			Dcm4CheeOperations.dcmsnd(dicomFile.getParentFile(), true);
		}
		else
			throw new Exception("Invalid DICOM file");
		return HttpServletResponse.SC_OK;
	}

	@Override
	public int createSystemTemplate(String username, File templateFile,
			String sessionID) throws Exception {
		projectOperations.createEventLog(username, null, null, null, null, null, null, "CREATE SYSTEM TEMPLATE", templateFile.getName());
		if (!EPADFileUtils.isValidXml(templateFile, EPADConfig.templateXSDPath))
			throw new Exception("Invalid Template file:" + templateFile.getName());
		FileUtils.copyFileToDirectory(templateFile, new File(EPADConfig.getEPADWebServerTemplatesDir()));
		return HttpServletResponse.SC_OK;
	}

	@Override
	public EPADFileList getFileDescriptions(ProjectReference projectReference,
			String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly)
					throws Exception {
		List<EpadFile> files = projectOperations.getProjectFiles(projectReference.projectID, toplevelOnly);
		List<EPADFile> efiles = new ArrayList<EPADFile>();
		for (EpadFile file: files) {
			String subjectId = null;
			String patientName = null;
			String studyId = null;
			if (file.getSubjectId() != null && file.getSubjectId() != 0)
			{
				Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
				subjectId = subject.getSubjectUID();
				patientName = subject.getName();
			}
			if (file.getStudyId() != null && file.getStudyId() != 0)
			{
				Study study = (Study) projectOperations.getDBObject(Study.class, file.getStudyId());
				studyId = study.getStudyUID();
			}
			EPADFile efile = new EPADFile(projectReference.projectID, subjectId, patientName, studyId, file.getSeriesUid(),
					file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
					getEpadFilePath(file), file.isEnabled(), file.getDescription());
			boolean filter = searchFilter.shouldFilterFileType(file.getFileType());
			if (!filter)
				efiles.add(efile);
		}
		return new EPADFileList(efiles);
	}

	private String getEpadFilePath(EpadFile file)
	{
		String path = "files/"+ file.getRelativePath();
		String fileName = file.getId() + file.getExtension();
		if (path.endsWith("/"))
			return path + fileName;
		else
			return path + "/" + fileName;
	}

	@Override
	public EPADFile getFileDescription(ProjectReference projectReference, String filename,
			String username, String sessionID) throws Exception {
		EpadFile file = projectOperations.getEpadFile(projectReference.projectID, null, null, null, filename);
		if (file == null) return null;
		String subjectId = null;
		String patientName = null;
		String studyId = null;
		if (file.getSubjectId() != null && file.getSubjectId() != 0)
		{
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
			subjectId = subject.getSubjectUID();
			patientName = subject.getName();
		}
		if (file.getStudyId() != null && file.getStudyId() != 0)
		{
			Study study = (Study) projectOperations.getDBObject(Study.class, file.getStudyId());
			studyId = study.getStudyUID();
		}
		EPADFile efile = new EPADFile(projectReference.projectID, subjectId, patientName, studyId, file.getSeriesUid(),
				file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
				getEpadFilePath(file), file.isEnabled(), file.getDescription());
		return efile;
	}

	@Override
	public EPADFileList getFileDescriptions(SubjectReference subjectReference,
			String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly)
					throws Exception {
		List<EpadFile> files = projectOperations.getSubjectFiles(subjectReference.projectID, subjectReference.subjectID, toplevelOnly);
		List<EPADFile> efiles = new ArrayList<EPADFile>();
		for (EpadFile file: files) {
			String patientName = null;
			String studyId = null;
			if (file.getSubjectId() != null && file.getSubjectId() != 0)
			{
				Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
				patientName = subject.getName();
			}
			if (file.getStudyId() != null && file.getStudyId() != 0)
			{
				Study study = (Study) projectOperations.getDBObject(Study.class, file.getStudyId());
				studyId = study.getStudyUID();
			}
			EPADFile efile = new EPADFile(subjectReference.projectID, subjectReference.subjectID, patientName, studyId, file.getSeriesUid(),
					file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
					getEpadFilePath(file), file.isEnabled(), file.getDescription());
			boolean filter = searchFilter.shouldFilterFileType(file.getFileType());
			if (!filter)
				efiles.add(efile);
		}
		return new EPADFileList(efiles);
	}

	@Override
	public EPADFile getFileDescription(SubjectReference subjectReference, String filename,
			String username, String sessionID) throws Exception {
		EpadFile file = projectOperations.getEpadFile(subjectReference.projectID, subjectReference.subjectID, null, null, filename);
		if (file == null) return null;
		String patientName = null;
		String studyId = null;
		if (file.getSubjectId() != null && file.getSubjectId() != 0)
		{
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
			patientName = subject.getName();
		}
		if (file.getStudyId() != null && file.getStudyId() != 0)
		{
			Study study = (Study) projectOperations.getDBObject(Study.class, file.getStudyId());
			studyId = study.getStudyUID();
		}
		EPADFile efile = new EPADFile(subjectReference.projectID, subjectReference.subjectID, patientName, studyId, file.getSeriesUid(),
				file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
				getEpadFilePath(file), file.isEnabled(), file.getDescription());
		return efile;
	}

	@Override
	public EPADFileList getFileDescriptions(StudyReference studyReference,
			String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly)
					throws Exception {
		List<EPADFile> efiles = getEPADFiles(studyReference, username, sessionID, searchFilter, toplevelOnly);
		return new EPADFileList(efiles);
	}

	@Override
	public List<EPADFile> getEPADFiles(StudyReference studyReference,
			String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly)
					throws Exception {
		List<EpadFile> files = projectOperations.getStudyFiles(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, toplevelOnly);
		List<EPADFile> efiles = new ArrayList<EPADFile>();
		for (EpadFile file: files) {
			String patientName = null;
			if (file.getSubjectId() != null && file.getSubjectId() != 0)
			{
				Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
				patientName = subject.getName();
			}
			EPADFile efile = new EPADFile(studyReference.projectID, studyReference.subjectID, patientName, studyReference.studyUID, file.getSeriesUid(),
					file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
					getEpadFilePath(file), file.isEnabled(), file.getDescription());
			boolean filter = searchFilter.shouldFilterFileType(file.getFileType());
			if (!filter)
				efiles.add(efile);
		}
		return efiles;
	}

	@Override
	public EPADFile getFileDescription(StudyReference studyReference, String filename,
			String username, String sessionID) throws Exception {
		EpadFile file = projectOperations.getEpadFile(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, filename);
		if (file == null) return null;
		String patientName = null;
		if (file.getSubjectId() != null && file.getSubjectId() != 0)
		{
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
			patientName = subject.getName();
		}
		EPADFile efile = new EPADFile(studyReference.projectID, studyReference.subjectID, patientName, studyReference.studyUID, file.getSeriesUid(),
				file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
				getEpadFilePath(file), file.isEnabled(), file.getDescription());
		return efile;
	}

	@Override
	public EPADFileList getFileDescriptions(SeriesReference seriesReference,
			String username, String sessionID, EPADSearchFilter searchFilter)
					throws Exception {
		List<EPADFile> efiles = getEPADFiles(seriesReference, username, sessionID, searchFilter);
		return new EPADFileList(efiles);
	}

	@Override
	public List<EPADFile> getEPADFiles(SeriesReference seriesReference,
			String username, String sessionID, EPADSearchFilter searchFilter)
					throws Exception {
		List<EpadFile> files = projectOperations.getSeriesFiles(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID);
		List<EPADFile> efiles = new ArrayList<EPADFile>();
		for (EpadFile file: files) {
			String subjectId = null;
			String patientName = null;
			if (file.getSubjectId() != null && file.getSubjectId() != 0)
			{
				Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
				subjectId = subject.getSubjectUID();
				patientName = subject.getName();
			}
			EPADFile efile = new EPADFile(seriesReference.projectID, seriesReference.subjectID, patientName, seriesReference.studyUID, file.getSeriesUid(),
					file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
					getEpadFilePath(file), file.isEnabled(), file.getDescription());
			boolean filter = searchFilter.shouldFilterFileType(file.getFileType());
			if (!filter)
				efiles.add(efile);
		}
		return efiles;
	}

	@Override
	public EPADFile getFileDescription(SeriesReference seriesReference, String filename,
			String username, String sessionID) throws Exception {
		EpadFile file = projectOperations.getEpadFile(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, filename);
		String patientName = null;
		if (file.getSubjectId() != null && file.getSubjectId() != 0)
		{
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, file.getSubjectId());
			patientName = subject.getName();
		}
		EPADFile efile = new EPADFile(seriesReference.projectID, seriesReference.subjectID, patientName, seriesReference.studyUID, file.getSeriesUid(),
				file.getName(), file.getLength(), file.getFileType(), new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(file.getCreatedTime()), 
				getEpadFilePath(file), file.isEnabled(), file.getDescription());
		return efile;
	}


	@Override
	public EPADTemplateContainerList getTemplateDescriptions(String username,
			String sessionID) throws Exception {
		return getTemplateDescriptions(username, sessionID, null);
	}

	@Override
	public EPADTemplateContainerList getTemplateDescriptions(String username,
			String sessionID, String templateLevelFilter) throws Exception {
		return getTemplateDescriptions(username, sessionID, templateLevelFilter, false);
	}

	@Override
	public EPADTemplateContainer dbtemplate2Container(Template t) throws Exception {
		List<EPADTemplate> epadTmpls = new ArrayList<EPADTemplate>();
		//the template is the first template anyway. not supporting multiple templates now
		EPADTemplate epadTmpl = new EPADTemplate(t.getTemplateUID(), t.getTemplateName(), t.getTemplateType(), t.getTemplateCode(),
				t.getTemplateDescription(), t.getModality(), t.getTemplateLevelType());
		epadTmpls.add(epadTmpl);
		EpadFile f = (EpadFile) projectOperations.getDBObject(EpadFile.class, t.getFileId());
		if (f!=null) {
			EPADTemplateContainer template = new EPADTemplateContainer("", "", "", "", "", f.getName(), f.getLength(), FileType.TEMPLATE.getName(), 
					formatDate(f.getCreatedTime()), getEpadFilePath(f), f.isEnabled(), f.getDescription(), t.getTemplateLevelType());
			template.templateName = t.getTemplateName();
			template.templateType = t.getTemplateType();
			template.templateCode = t.getTemplateCode();
			template.templateDescription = t.getTemplateDescription();
			template.modality = t.getModality();
			template.templates = epadTmpls;
			return template;
		}
		return null;

	}
	
	//lexicons accepted by bioportal
//	private static List<String> validLexicon=Arrays.asList("CPT", "SNOMEDCT", "RXNORM", "NDDF", "MEDDRA", "LOINC", "DERMO", 
//			"NDFRT", "NCIT", "EDAM", "CTV3", "RCD", "DOID", "VANDF", "NDC", "MESH", "MESH", "ICD9CM", "RADLEX", "ICD10CM", 
//			"SNMI", "SIO", "ICD10", "ICD-10", "FMA", "RCTV2", "BAO", "SYMP", "ENVO", "OMIM", "TM", "HP", "ICD10PCS", "BTO", 
//			"CL", "GO", "ATC", "EDAM-BIOIMAGING", "FB-DV", "UBERON", "OBI", "DRON", "ORDO", "PAE", "PO", "MONDO", "DINTO", 
//			"DDI", "MFOEM", "OBIB", "ICF", "ECO", "PSIMOD", "DAG", "EFO", "FTC", "EDDA", "SCIO", "ICW", "RH-MESH", "ZEA", 
//			"ECG", "DDO", "CTCAE", "HRDO", "VIVO", "FYPO", "MS", "MDDB", "ECSO", "PW", "PR", "ICPC", "FIX", "PATO", "HUGO", 
//			"XAO", "NPO", "ROO", "VSAO", "AMINO-ACID", "MEO", "BRO", "ZFA", "ICNP", "PLOSTHES", "TAO", "OGMS", "COGAT", 
//			"OCRE", "CHEMINF", "ADO", "TTO", "OFSMR", "BCGO", "STY", "PDO", "OPE", "CTO", "BIRNLEX", "SWO", "RB", "SCI", 
//			"CHEBI", "ICPC2P", "GALEN", "APAONTO", "EXO", "DMTO", "MA", "MUS", "ONTOPSYCHIA", "CRISP", "FOODON", "DIAB", 
//			"ENM", "UO", "ONTODT", "PHARE", "TEO", "EXON", "DIKB", "MO", "BMT", "PTO", "HCPCS", "LBO", "SPD", "REX", "CNO", 
//			"MP", "MFOMD", "MSO", "SWEET", "JERM", "PECO", "RO", "IDO", "OBIWS", "LCSH", "ICD11-BODYSYSTEM", "PHENOMEBLAST", 
//			"ACGT-MO", "SNP", "SNPO", "OWL-DL", "PCO", "FB-BT", "IFAR", "FA", "AAO", "PDON", "OBOREL", "TOP-MENELAS", "VO", 
//			"SMASH", "ONTOLURGENCES", "SO", "NBO", "ONTOAD",  "COGPO", "DOCCC", "CCC", "PATHLEX", "IDOMAL", "HFO", "OMRSE", "CO",
//			"ROLEO", "BIBFRAME", "MAMO", "NMOBR", "SEP", "CARELEX", "PDQ", "BFO", "GAMUTS", "AERO", "DIDEO", "MF", "GEXO", "MEDO", 
//			"KORO", "LHN", "GML", "RCTONT", "GENO", "BCTT", "BCO", "DCM", "NGSONTO", "OAE", "EDDA_PT", "AO", "OMIT", "MIR", "VHOG", 
//			"OGG", "SBO", "PROVO", "CSEO", "WB-BT", "APATREATMENT", "CSSO", "SSO", "GBM", "CHMO", "GEO", "DIAGONT", "HPIO", "STATO", 
//			"GEOSPECIES", "SCHEMA", "CIO", "BIOMODELS", "DDIO", "PMR", "ASDPTO", "LPT", "COSTART", "CLO", "CMO", "LCGFT", "OBCS",
//			"DTO", "VTO", "DCAT", "PMA", "SBOL", "SBOLV", "REPO", "VSO", "CN", "GENEPIO", "GENEPIO", "AEO", "IAO", "RXNO", "EPO", 
//			"GO-PLUS", "CSO", "NCCO", "OMP", "PHENX", "HEIO", "GRO", "MI", "PEO", "PDRO", "SSN", "OPB", "CVAO", "NLMVS", "IAML-MOP", 
//			"BDO", "BAO-GPCR", "NIHSS", "GEOSPARQL", "MAT", "QUDT", "WB-PHENOTYPE", "OHD", "ROS", "DC", "ADMIN", "IDODEN", "OGDI", 
//			"GWAS", "IMGT-ONTOLOGY", "IMMDIS", "MCCL", "HAO", "LIPRO", "CANONT", "HORD", "HINO", "MPO", "BRCT", "GAZ", "OGI", "ICECI",
//			"NIDM-RESULTS", "ADAR", "SCDO", "OHMI", "GFVO", "EPSO", "EPSO", "PTRANS", "EUPATH", "EO", "SYN", "PVONTO", "FISHO", "CBO", 
//			"KIOSASLIQQ", "SP", "MEDLINEPLUS", "ISO19108TO", "ALLERGYDETECTOR", "CHEAR", "FALDO", "OMV", "TMO", "AGRO", "OPL", "FHHO",
//			"OA", "ISO19110", "MOP", "HL7", "CCONT", "GTO", "HIV", "OCDM", "ISO639-2", "ISO 639-1", "CANCO", "OBA", "TRAK", "ISO19115MI",
//			"NMR", "CDAO", "BHN", "TGMA", "ONTOTOXNUC", "FLU", "ICO", "ONTOPARON_SOCIAL", "TIME", "MSTDE-FRE", "ONTODM-CORE", "MMO",
//			"ERO", "FBBI", "RS", "EHDA", "TRON", "MCCV", "ISO19115", "AI-RHEUM", "PEAO", "ONTOVIP", "REXO", "BCTEO", "CEDARVS", 
//			"MPATH", "BIOMO", "OMIABIS", "OVAE", "TAXRANK", "WSIO", "PPIO", "WIKIPATHWAYS", "PROCCHEMICAL", "FB-SP", "BRIDG", "VT", 
//			"PLIO", "FDSAJFAHSJK", "IDOBRU", "ODNAE", "MOC", "ONTOMA", "STUFF", "PMO", "EMO", "SHR", "DCMITYPE", "ABD", "SAO", "NEMO", 
//			"CARO", "OBOE-SBC", "SBC-LTER", "FLOPO", "GBOL", "RAO", "PROCESS", "ATO", "PLANA", "MIXS", "PTS", "ISO19115CC", "CNO_ACRONYM",
//			"APAOCUEMPLOY", "EHDAA2", "ORTH", "RETO", "CTX", "CO-WHEAT", "IWIS", "ATOL", "NMOSP", "TEDDY", "MIM", "ICPS", "EXACT", "SOPHARM",
//			"GO-EXT", "APANEUROCLUSTER", "ONS", "ONL-MSA", "SDO", "NATPRO", "PEDTERM", "HO", "MOOCCUADO", "MATRCOMPOUND", "EGO", 
//			"ISO19115ROLES", "FB-CV", "EMAPA", "TMA", "HIVO004", "PIDS", "GDCO", "BIPON", "CTENO", "COMB JELLIES", "OGSF", "NCRO", 
//			"NCRNA", "MOOCCIADO", "CNOT", "BP-METADATA", "BTSE", "CEPH", "GFO-BIO", "ONTODM-KDD", "FOAF", "IDCLOUDSEO", "DAO", "TRANS", 
//			"BE", "OARCS", "PE-O", "ONTOKBCF", "PDUMDV", "ISO19115CON", "EPILONT", "TM-OTHER-FACTORS", "ICTM", "APADISORDERS", "GENE-CDS",
//			"PSO", "SIBO", "TMF", "GINAS", "MHCRO", "VARIO", "OGMD", "ISO-ANNOTATIONS", "TM-SIGNS-AND-SYMPTS", "PHAGE", "LEGALAPATEST2",
//			"FIRE", "MICRO", "NTDO", "PHMAMMADO", "GLYCO", "MWLA", "APASTATISTICAL", "MATRROCK", "GMM", "VEO", "SITBAC", "FALL", "ZFS",
//			"M-PARTOF", "BOLA57", "ISO19115PR", "PHFUMIADO", "IXNO", "CU-VO", "CCON", "HAROREADO", "IGTO", "CISAVIADO", "MOVIE",
//			"ANCESTRO", "ANCESTRO", "TM-CONST", "SSE",  "ISO19115EX", "FAO", "SEQ", "RNRMU", "TADS", "SURAT-LAMAR", "CKDO", "RDAU",
//			"TYPON", "PE", "GFO", "TRIKMENANGJUDI", "HCODONONT", "SD3", "CHD", "RSA", "PHARMGKB", "RDL", "ADALAB", "APAEDUCLUSTER",
//			"ISO19115DI", "XEO", "GRO-CPGA", "EBP", "SPTO", "DCO-DEBUGIT", "BHO", "MFMO", "IT", "MRO", "CYTO", "CIDOC-CRM", "CRM", 
//			"BP", "SURGICAL", "EOL", "INM", "INM-NPI", "SPO", "NONRCTO", "CEPATHAMIL", "CARRE", "HIVCRS", "OLATDV", "HGNC", "PSEUDO", 
//			"HUPSON", "VPH", "VCARD", "ENTITYCANDIDATES", "ADALAB-META", "RNAO", "ROC", "SEDI", "MIAPA", "OGR", "OOSTT", "GLYCORDF", 
//			"PSDS", "MSV", "ISO19115TCC", "HAAURAADO", "CIINTEADO", "MEDEON", "PCMO", "CPTAC", "MADS-RDF", "KOS", "CCTOO", "ENTITY", 
//			"CABRO", "NORREG", "APO", "FISH-AST", "TRIAGE", "KERIS99SAKONG", "MSTDE", "SOY", "LDA", "APOLLO-SV", "NEUMORE", "MANTANPESINDEN",
//			"MEGO", "MIRNAO", "BRO_ACRONYM", "IDQA", "DCCDFV", "GVANOS", "NHSQI2009", "TEST", "EHDAA", "CCO", "ONLIRA", "ONTOPNEUMO", 
//			"FLYGLYCODB", "CMF", "NEOMARK3", "CLASSY-FIRE", "INIKEEPO", "MATR", "MDCO", "MCBCC", "DDPHENO", "FO", "DIMASFAN", "ONL-MR-DA", 
//			"DERMLEX", "INTERNANO", "KOINHOKI", "BT", "BIBLIOTEK-O", "EMAP", "AS", "NEUDIGS", "COPDO", "OCHV", "CMPO", "ONSTR", "VICO", "MERA",
//			"XCO", "DUO", "MATRELEMENT", "GVP", "UNITSONT", "NEOMARK4", "NIGO", "NPIS", "NPI", "APACOMPUTER", "DCT", "CEDARPC", "BSAO", "HC",
//			"MONO", "MINERAL", "INO", "DCTERMS", "MNR", "ONTONEO", "EHRS", "BIM", "EP", "UPHENO", "TDWGSPEC", "TDWG", "TOK", "ISO19115CI", "MFO", 
//			"PGXO", "PANDA", "ELIG", "OGG-MM", "MOUSE", "ONL-DP", "APATANDT", "OCMR", "LEGALAPA", "PIERO", "MATRROCKIGNEOUS", "VRACORE", "COLL",
//			"ADW", "KISAO", "BSPO", "BUSNESS", "ISO19115SRS", "MIRO", "BNO", "GRO-CPD", "TM-MER", "NIC", "OF", "CVDO", "GAYAA", "GMO", "COMODI", 
//			"ISO19115ID", "ACRONYM", "OGROUP", "CTONT", "PPO", "PORO", "MINI-FAST-1", "RNPRIO", "CPRO", "SUICIDEO", "PP", "GPML", "CHEMBIO", 
//			"MOOCULADO", "PXO", "OBI_BCGO", "ECP", "INVERSEROLES", "ZECO", "WB-LS", "ISO-15926-2_2003", "ESSO", "DCMI", "DCO", "PROPREO", "DSEO", 
//			"HOM", "OOEVV", "PAV", "ESFO", "KONTES", "RBMS", "PHYLONT", "ITEMAS", "QIBO", "MMUSDV", "GCO", "MHC", "DLORO", "PDO_CAS", "HSAPDV", 
//			"VIVO-ISF", "BOF", "RDA-CONTENT", "INSECTH", "DDANAT", "OCVDAE", "NCCNEHR", "ISO19115DTC", "AURA","NCI","NCIM,","SNOMED","SRT","ACR",
//			"ASTM-SIGPURPOSE","BARI","BI","C4","C5","CD2","DCMUID","FMA","HPC","I10","I10P","I9","I9C","ISO639_1","ISO639_2","ISO3166_1",
//			"ISO5218_1","ISO_OID","LN","MDC","MDNS","MSH","NBD","NBG","NCDR","NICIP","NPI","POS","RFC3066","99SDM","SCPECG","SNM3","UCUM","UMLS",
//			"UPC");
	
	public static void analyzeNode(Node node) {
		try{
			Lexicon lx=Lexicon.getInstance();
		    NodeList nodeList = node.getChildNodes();
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node currentNode = nodeList.item(i);
		        edu.stanford.hakan.aim4api.base.CD cd =new edu.stanford.hakan.aim4api.base.CD();
	        	NamedNodeMap attrs = currentNode.getAttributes();
	        	if (attrs!=null){
	//        		traverse and find the code first, we cannot depend on the order
	        		for (int j = 0; j < attrs.getLength(); j++){
	        			Node attr = attrs.item(j);
	        			if (attr.getNodeName().equalsIgnoreCase("codeValue"))
		    				cd.setCode(attr.getNodeValue());
	        		}
	        		for (int j = 0; j < attrs.getLength(); j++){
		        		Node attr = attrs.item(j);
		        		switch(attr.getNodeName()){
		    			case "codingSchemeDesignator":
		    				cd.setCodeSystemName(attr.getNodeValue());
		    				//see it is is one of our old templates
		    				if (AnnotationBuilder.oldNoLexicon.contains(cd.getCodeSystemName())){
		    					if (cd.getCode()!=null){
		    		    			CD lexCD=lx.get(cd.getCode());
		    		    			if (lexCD!=null){
				    		    		//fix the cd
				    					log.info("Changing "+ cd.getCodeSystemName().toUpperCase() + " to "+lexCD.getCodeSystemName() );
				    					cd.setCodeSystemName(lexCD.getCodeSystemName());
				    					attr.setNodeValue(lexCD.getCodeSystemName());
				    					cd.setCodeSystemVersion(lexCD.getCodeSystemVersion());
				    					for (int k = 0; k < attrs.getLength(); k++){
				    					 if(attrs.item(k).getNodeName().equals("codingSchemeVersion")){
				    						 attrs.item(k).setNodeValue(lexCD.getCodeSystemVersion());
				    	    				
				    					 }
				    					}
		    		    			}else{
		    		    				//see if it is one of the rid ones in recist_v2
		    		    				if (cd.getCode().equals("RID7488") || cd.getCode().equals("RID396")){
		    		    					cd.setCodeSystemName("Radlex");
		    		    					attr.setNodeValue("Radlex");
		    		    	    			cd.setCodeSystemVersion(null);
		    		    				}
		    		    			}
		    		    		}
		    		    		else{
		    		    			log.warning("Controlled term code is not present, cannot fix this controlled term. codingSchemeDesignator=" + attr.getNodeValue());
		    		    		}
		    		    	}
		    			
		    				break;
		    			case "codingSchemeVersion":
		    				cd.setCodeSystemVersion(attr.getNodeValue());
		    				CD lexcsCD=lx.get(cd.getCode());
		    				if (cd.getCodeSystemName()!=null && AnnotationBuilder.oldNoLexicon.contains(cd.getCodeSystemName())){
		    					if (lexcsCD!=null){
		    						cd.setCodeSystemVersion(lexcsCD.getCodeSystemVersion());
		    						attr.setNodeValue(lexcsCD.getCodeSystemVersion());
		    					}else{
		    						//shouldn't happen these are just what we put wrong
		    						if (cd.getCode().equals("RID7488") || cd.getCode().equals("RID396")){
	    		    					cd.setCodeSystemVersion(null);
	    		    					attr.setNodeValue("");
	    		    				}
		    					}
		    					
		    				}
		    				break;
		    			case "codeMeaning":
		    				cd.setDisplayName(new ST(attr.getNodeValue()));
	
	    					//should also set the name, epad-plugin doesn't make sense
		    				if (cd.getDisplayName().getValue().equals("epad-plugin")){
		    					CD lexcmCD=lx.get(cd.getCode());
		    		    		if (lexcmCD!=null){
		    		    			cd.setDisplayName(lexcmCD.getDisplayName());
			    		    		log.info("set displayname to "+lexcmCD.getDisplayName());
			    		    		attr.setNodeValue(lexcmCD.getDisplayName().getValue());
		    		    		}
	    					}
		    				break;	    			
		    			}
	        		}
//	        		if (!(cd.getCodeSystemName()==null && cd.getDisplayName()==null && cd.getCode()==null))
//	        			log.info(cd.getCode()+ " " +cd.getCodeSystemName());
	        	}
		        
		        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		            analyzeNode(currentNode);
		        }
		    }
		}catch(Exception e){
			log.warning("Analyze template failed", e);
		}
	}
	
	public static void fixTemplateXML(File tfile, String xml) {
		try {
			
			Document doc = edu.stanford.hakan.aim4api.utility.XML.getDocumentFromString(xml);
			Node root = doc.getDocumentElement();
			analyzeNode(root);
			edu.stanford.hakan.aim4api.utility.XML.SaveDocucument(doc,tfile.getAbsolutePath());
		}catch(Exception e){
			log.warning("couldn't fix the template "+ tfile.getName() + " " + e.getMessage());
		}
		
	}
	
	/**
	 * checks and fixes the old templates
	 */
	@Override
	public void checkOldTemplates(){
		try{
			
			
			List<EpadFile> efiles = projectOperations.getEpadFiles(null, null, null, null, FileType.TEMPLATE, false);
			for (EpadFile efile: efiles)
			{
				log.info("file "+ efile.getName());
				File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
				//do backup
				File copyfile =new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile)+"_copy");
				String xml = EPADFileUtils.readFileAsString(tfile);
				EPADFileUtils.overwrite(copyfile, xml);
				fixTemplateXML(tfile,xml);

			}
			
			
		} catch (Exception e) {
			log.warning("Couldn't retrieve the templates", e);
		}
		
				
	}
	
	@Override
	public void migrateTemplates() throws Exception {
		List<EpadFile> efiles = projectOperations.getEpadFiles(null, null, null, null, FileType.TEMPLATE, false);
		for (EpadFile efile: efiles)
		{
			File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
			Template template = getFirstTemplateInfo(tfile);
			if (template.getTemplateCode()==null) {
				log.warning("Template code couldn't be retrieved. Skipping file");
				continue;
			}
			Template existingTemplate=getTemplate(template.getTemplateCode());
			if (existingTemplate!=null){
				//two different possibilities; either the user uploaded same file to different projects or uses the same code
				EpadFile ef=(EpadFile)projectOperations.getDBObject(EpadFile.class, existingTemplate.getFileId());
				
				
				//if same file just remove the extras and put the project relations
				if (efile.getName().equals(ef.getName()) && efile.getLength()==ef.getLength() && !efile.getProjectId().equals(ef.getProjectId()) ) {
					log.info("same file for template="+template.getTemplateName() +" just putting the project relation");
				}
				//if same code for different files put it in the log
				else {
					Project project= (Project) projectOperations.getDBObject(Project.class, efile.getProjectId());
					projectOperations.createEventLog("admin", project.getProjectId(), null, null, null, null, null, efile.getName(), "The same template code cannot be used for different templates code saved as "+ template.getTemplateCode()+"-2", template.getTemplateCode(), true);
					template.setTemplateCode(template.getTemplateCode()+"-2");
					template.setFileId(efile.getId());
					template.save();
					log.info("template db entry is created for template="+template.getTemplateName() + " as " + template.getTemplateCode());
				}
				
			}else {
				template.setFileId(efile.getId());
				template.setCreator(efile.getCreator());
				template.save();
				log.info("template db entry is created for template="+template.getTemplateName() + " as " + template.getTemplateCode());
			}
			Project project= (Project) projectOperations.getDBObject(Project.class, efile.getProjectId());
			projectOperations.setProjectTemplate("admin", project.getProjectId(), template.getTemplateCode(), efile.isEnabled());
			
		}
		
		//get the disabled templates and set their enable attribute
		List<DisabledTemplate> dts = new DisabledTemplate().getObjects("");
		if (dts!=null && !dts.isEmpty()) {
			for (DisabledTemplate dt:dts) {
				//it was actually file name but the if check uses filename,templatename and templateCode so lets check all
				//if (disabledTemplatesNames.contains(template.fileName) || disabledTemplatesNames.contains(template.templateName) || disabledTemplatesNames.contains(template.templateCode))
				for (EpadFile efile: efiles)
				{
					File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
					//we do not actually need to get this project id, but putting to get the correct template container object
					Project fileProject= (Project) projectOperations.getDBObject(Project.class, efile.getProjectId());
					Project disableProject= (Project) projectOperations.getDBObject(Project.class, dt.getProjectId());
					EPADTemplateContainer tc=convertEpadFileToTemplate(fileProject.getProjectId(), efile, tfile); 
					if (tc.fileName.equalsIgnoreCase(dt.getTemplateName()) || tc.templateName.equalsIgnoreCase(dt.getTemplateName()) || tc.templateCode.equalsIgnoreCase(dt.getTemplateName())){
						projectOperations.setProjectTemplate("admin", disableProject.getProjectId(), tc.templateCode, false);
						dt.delete();
					}
				
				}
				
				
			}
		}
		
	}

	@Override
	public EPADTemplateContainerList getTemplateDescriptions(String username,
			String sessionID, String templateLevelFilter, boolean includeSystemTemplates) throws Exception {

		EPADTemplateContainerList fileList = new EPADTemplateContainerList();

		//not supported anymore but should we put just in case
		if (includeSystemTemplates)
			throw new Exception("System templates are not supported anymore");
		//		EPADTemplateContainerList oldList = getSystemTemplateDescriptions(username, sessionID);
		//		if (includeSystemTemplates)
		//			fileList = oldList;

		String criteria="";
		if (templateLevelFilter!=null) 
			criteria = "LOWER(templateLevelType) like '"+templateLevelFilter.toLowerCase()+"%'";
		//get all templates
		List<Template> dbTemplates = new Template().getObjects(criteria);
		//new method. use templates table. version 2.3
		if (dbTemplates!=null && !dbTemplates.isEmpty()) {
			for (Template t: dbTemplates) {
				Set<EPADProjectTemplate> userProjects = new HashSet<EPADProjectTemplate>();

				//for each create the EPADTemplate and EPADTemplateContainer
				EPADTemplateContainer template = dbtemplate2Container(t);
				//if template couldn't be retrieved skip
				if (template==null) continue;
				//get project information and put it in
				List<Long> projects = projectOperations.getProjectsForTemplate(t.getId());
				//new format for the template, got project-template records
				if (projects!=null && !projects.isEmpty()){
					for (Long pId:projects) {
						Project project = (Project) projectOperations.getDBObject(Project.class, pId);

						if (projectOperations.hasAccessToProject(username, project.getProjectId())
								|| project.getProjectId().equals(EPADConfig.xnatUploadProjectID))
						{
							String defTemplate = null;
							boolean defaultTemplate=false;
							Project userProj = projectOperations.getProjectForUser(username, project.getProjectId());
							if (userProj != null)
								defTemplate = userProj.getDefaultTemplate();
							if (t.getTemplateCode().equals(defTemplate))
								defaultTemplate = true;
							EPADProjectTemplate pt=new EPADProjectTemplate(project.getProjectId(), projectOperations.getProjectTemplate(username, project.getProjectId(), t.getTemplateCode()), defaultTemplate);
									
							userProjects.add(pt);
						}
					}


					//default template? for what should be related to project.
					

					//disabled templates get them and remove from user projects
					//is done for project
//					List<Long> disabledProjects = projectOperations.getDisabledProjectsForTemplate(t.getId());
//					for (Long pId:disabledProjects) {
//						Project project = (Project) projectOperations.getDBObject(Project.class, pId);
//						userProjects.remove(project.getProjectId());
//					}


					//??? it is related to project!!
					//				boolean enabled = efile.isEnabled();
					//				template.enabled = enabled;

					template.projectTemplates=new ArrayList<EPADProjectTemplate>();
					template.projectTemplates.addAll(userProjects);
					//if there are project-template records we do not care about the file tuple anymore
					//it is ignoring the file projectid
					//we need a way to put the projectid initially. it is in main

					//just to put smt for ui. should be removed
					if (template.projectTemplates!=null && !template.projectTemplates.isEmpty()){
						for (EPADProjectTemplate pt:template.projectTemplates) {
							template.projectID+=pt.projectID+",";
						}
						template.projectID=template.projectID.trim().replaceAll(",+$", "");
//						template.projectID=template.projectTemplates.get(0).projectID;
					}
					fileList.addTemplate(template);

				}

			}
		}
		//old format, no project-template records. use the old retrieval
		else {
			List<EpadFile> efiles = projectOperations.getEpadFiles(null, null, null, null, FileType.TEMPLATE, false);
			//			if (efiles.size() == 0 && includeSystemTemplates)
			//				fileList = oldList;
			Map<String, List<String>> disabledTemplates = new HashMap<String, List<String>>();
			for (EpadFile efile: efiles)
			{
				Set<String> userProjects = new HashSet<String>();

				//get projects from project_template
				//if there are tuples in project_template for this template use that instead
				//we need the code to check in the project_template table.
				//convert to template container object here
				File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
				//sending null as project id instead of project.getProjectId()
				EPADTemplateContainer template = convertEpadFileToTemplate(null, efile, tfile);
				Project project = (Project) projectOperations.getDBObject(Project.class, efile.getProjectId());
				String defTemplate = null;
				Project userProj = projectOperations.getProjectForUser(username, project.getProjectId());
				if (userProj != null)
				{
					defTemplate = userProj.getDefaultTemplate();
				}
				List<String> disabledTemplatesNames = disabledTemplates.get(project.getProjectId());
				if (disabledTemplatesNames == null)
				{
					disabledTemplatesNames = projectOperations.getDisabledTemplates(project.getProjectId());
					disabledTemplates.put(project.getProjectId(), disabledTemplatesNames);
				}
				if (userProjects.contains(project.getProjectId()) || projectOperations.hasAccessToProject(username, project.getProjectId())
						|| project.getProjectId().equals(EPADConfig.xnatUploadProjectID))
				{
					userProjects.add(project.getProjectId());

					List<EPADTemplate> templates = template.templates;
					for (EPADTemplate t: templates)
					{
						if (t.getTemplateCode().equals(defTemplate))
						{
							t.defaultTemplate = true;
						}
						else
							t.defaultTemplate = false;
					}
					boolean enabled = efile.isEnabled();
					if (disabledTemplatesNames.contains(template.fileName) || disabledTemplatesNames.contains(template.templateName) || disabledTemplatesNames.contains(template.templateCode))
						enabled = false;
					template.enabled = enabled;
					template.projectID=project.getProjectId();
					template.projectIDs=new ArrayList<String>();
					template.projectIDs.add(project.getProjectId());

					if (templateLevelFilter==null || template.templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase()))
						fileList.addTemplate(template);

				}
			}
		}
		return fileList;
	}

	//	@Override
	//	public EPADTemplateContainerList getTemplateDescriptions(String username,
	//			String sessionID, String templateLevelFilter, boolean includeSystemTemplates) throws Exception {
	//		EPADTemplateContainerList oldList = getSystemTemplateDescriptions(username, sessionID);
	//		EPADTemplateContainerList fileList = new EPADTemplateContainerList();
	//		List<EpadFile> efiles = projectOperations.getEpadFiles(null, null, null, null, FileType.TEMPLATE, false);
	//		if (efiles.size() == 0 && includeSystemTemplates)
	//			fileList = oldList;
	//		Map<String, List<String>> disabledTemplates = new HashMap<String, List<String>>();
	//		for (EpadFile efile: efiles)
	//		{
	//			Set<String> userProjects = new HashSet<String>();
	//			
	//			//get projects from project_template
	//			//if there are tuples in project_template for this template use that instead
	//			//we need the code to check in the project_template table.
	//			//convert to template container object here
	//			File tfile = new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile));
	//			//sending null as project id instead of project.getProjectId()
	//			EPADTemplateContainer template = convertEpadFileToTemplate(null, efile, tfile);
	//			//check the project_template table
	//			//disable
	//			List<Long> projects = null; //projectOperations.getProjectsForTemplate(template.fileName);
	//			//new format for the template, got project-template records
	//			if (projects!=null && !projects.isEmpty()){
	//				for (Long pId:projects) {
	//					Project project = (Project) projectOperations.getDBObject(Project.class, pId);
	//					String defTemplate = null;
	//					Project userProj = projectOperations.getProjectForUser(username, project.getProjectId());
	//					if (userProj != null)
	//					{
	//						defTemplate = userProj.getDefaultTemplate();
	//					}
	//					List<String> disabledTemplatesNames = disabledTemplates.get(project.getProjectId());
	//					if (disabledTemplatesNames == null)
	//					{
	//						disabledTemplatesNames = projectOperations.getDisabledTemplates(project.getProjectId());
	//						disabledTemplates.put(project.getProjectId(), disabledTemplatesNames);
	//					}
	//					if (userProjects.contains(project.getProjectId()) || projectOperations.hasAccessToProject(username, project.getProjectId())
	//							|| project.getProjectId().equals(EPADConfig.xnatUploadProjectID))
	//					{
	//						userProjects.add(project.getProjectId());
	//						
	//						List<EPADTemplate> templates = template.templates;
	//						for (EPADTemplate t: templates)
	//						{
	//							if (t.getTemplateCode().equals(defTemplate))
	//							{
	//								t.defaultTemplate = true;
	//							}
	//							else
	//								t.defaultTemplate = false;
	//						}
	//						if (disabledTemplatesNames.contains(template.fileName) || disabledTemplatesNames.contains(template.templateName) || disabledTemplatesNames.contains(template.templateCode))
	//							userProjects.remove(project.getProjectId());
	//						
	//						
	//					}
	//					
	//					
	//				}
	//				
	//				boolean enabled = efile.isEnabled();
	//				
	//				template.enabled = enabled;
	//				
	//				template.projectIDs=new ArrayList<String>();
	//				template.projectIDs.addAll(userProjects);
	//				//if there are project-template records we do not care about the file tuple anymore
	//				//it is ignoring the file projectid
	//				//we need a way to put the projectid initially. it is in main
	//				
	//				//just to put smt for ui. should be removed
	//				if (template.projectIDs!=null && !template.projectIDs.isEmpty())
	//					template.projectID=template.projectIDs.get(0);
	//				
	//				if (templateLevelFilter==null || template.templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase()))
	//					fileList.addTemplate(template);
	//				
	//			}
	//			//old format, no project-template records. use the old retrieval
	//			else {
	//				Project project = (Project) projectOperations.getDBObject(Project.class, efile.getProjectId());
	//				String defTemplate = null;
	//				Project userProj = projectOperations.getProjectForUser(username, project.getProjectId());
	//				if (userProj != null)
	//				{
	//					defTemplate = userProj.getDefaultTemplate();
	//				}
	//				List<String> disabledTemplatesNames = disabledTemplates.get(project.getProjectId());
	//				if (disabledTemplatesNames == null)
	//				{
	//					disabledTemplatesNames = projectOperations.getDisabledTemplates(project.getProjectId());
	//					disabledTemplates.put(project.getProjectId(), disabledTemplatesNames);
	//				}
	//				if (userProjects.contains(project.getProjectId()) || projectOperations.hasAccessToProject(username, project.getProjectId())
	//						|| project.getProjectId().equals(EPADConfig.xnatUploadProjectID))
	//				{
	//					userProjects.add(project.getProjectId());
	//					
	//					List<EPADTemplate> templates = template.templates;
	//					for (EPADTemplate t: templates)
	//					{
	//						if (t.getTemplateCode().equals(defTemplate))
	//						{
	//							t.defaultTemplate = true;
	//						}
	//						else
	//							t.defaultTemplate = false;
	//					}
	//					boolean enabled = efile.isEnabled();
	//					if (disabledTemplatesNames.contains(template.fileName) || disabledTemplatesNames.contains(template.templateName) || disabledTemplatesNames.contains(template.templateCode))
	//						enabled = false;
	//					template.enabled = enabled;
	//					template.projectID=project.getProjectId();
	//					template.projectIDs=new ArrayList<String>();
	//					template.projectIDs.add(project.getProjectId());
	//					
	//					if (templateLevelFilter==null || template.templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase()))
	//						fileList.addTemplate(template);
	//					
	//				}
	//			}
	//		}
	//		return fileList;
	//	}

	@Override
	public EPADTemplateContainerList getSystemTemplateDescriptions(String username,
			String sessionID) throws Exception {
		return getSystemTemplateDescriptions(username, sessionID, null);
	}

	@Override
	public EPADTemplateContainerList getSystemTemplateDescriptions(String username,
			String sessionID, String templateLevelFilter) throws Exception {
		EPADTemplateContainerList fileList = new EPADTemplateContainerList();
		File[] templates = new File(EPADConfig.getEPADWebServerTemplatesDir()).listFiles();
		//this method is not used any more. leaving the old version of disabled templates
		List<String> disabledTemplatesNames = projectOperations.getDisabledTemplates(EPADConfig.xnatUploadProjectID);
		for (File template: templates)
		{
			if (template.isDirectory()) continue;
			String name = template.getName();
			String description = "";
			if (!name.toLowerCase().endsWith(".xml")) continue;
			String templateUID = "";
			String templateName = "";
			String templateType = "";
			String templateLevelType = "";
			String templateCode = "";
			String templateDescription = "";
			String modality = "";
			List<EPADTemplate> epadTmpls = new ArrayList<EPADTemplate>();
			try {
				String xml = EPADFileUtils.readFileAsString(template);
				JSONObject root = XML.toJSONObject(xml);
				JSONObject container = root.getJSONObject("TemplateContainer");
				JSONArray templateObjs = new JSONArray();
				try {
					JSONObject templateObj = container.getJSONObject("Template");
					templateObjs.put(templateObj);
				}
				catch (Exception x) {
					templateObjs = container.getJSONArray("Template");
				}
				for (int i = 0; i < templateObjs.length(); i++)
				{
					JSONObject templateObj = templateObjs.getJSONObject(i);
					templateUID = templateObj.getString("uid");
					templateName = templateObj.getString("name");
					templateType = templateObj.getString("codeMeaning");
					try {
						templateCode = templateObj.getString("codeValue");
					} catch (Exception x) {
						log.warning("Error getting code value for template " + template.getAbsolutePath());
					}
					try {
						templateDescription = templateObj.getString("description");
						modality = templateObj.getString("modality");
					} catch (Exception x) {}

					try {
						templateLevelType = templateObj.getString("templateType");
					} catch (Exception x) {
						templateLevelType = "image";
					}
					if (templateLevelType==null)
						templateLevelType = "image";

					if (templateLevelFilter==null || templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase())) {
						EPADTemplate epadTmpl = new EPADTemplate(templateUID, templateName, templateType, templateCode,
								templateDescription, modality, templateLevelType);
						epadTmpls.add(epadTmpl);
					}
				}
			} catch (Exception x) {
				log.warning("JSON Error", x);
			}
			boolean enabled = true;
			if (disabledTemplatesNames.contains(template.getName()) || disabledTemplatesNames.contains(templateName) || disabledTemplatesNames.contains(templateCode))
				enabled = false;
			if (description == null || description.trim().length() == 0)
			{
				description = "image"; // Image template type
				//				if (templateCode.startsWith("SEG"))
				//					description = "segmentation"; // Image template type
			}
			//ml changed first param from "" to default project
			EPADTemplateContainer epadContainer = new EPADTemplateContainer(EPADConfig.xnatUploadProjectID, "", "", "", "", name, template.length(), FileType.TEMPLATE.getName(), 
					formatDate(new Date(template.lastModified())), "templates/" + template.getName(), enabled, description, templateLevelType);
			epadContainer.templateName = templateName;
			epadContainer.templateType = templateType;
			epadContainer.templateCode = templateCode;
			epadContainer.templateDescription = templateDescription;			
			epadContainer.modality = modality;
			epadContainer.templates = epadTmpls;
			fileList.addTemplate(epadContainer);
		}
		return fileList;
	}


	@Override
	public EPADTemplateContainerList getProjectTemplateDescriptions(String projectID,
			String username, String sessionID) throws Exception {
		return getProjectTemplateDescriptions(projectID,username,sessionID,null);

	}
	@Override
	public EPADTemplateContainerList getProjectTemplateDescriptions(String projectID,
			String username, String sessionID, String templateLevelFilter) throws Exception {
		EPADTemplateContainerList fileList=getTemplateDescriptions(username, sessionID, templateLevelFilter);
		EPADTemplateContainerList templates=new EPADTemplateContainerList();
//		boolean isInAllProject=false;
		for (EPADTemplateContainer t:fileList.ResultSet.Result){
			if (t.projectTemplates!=null) {
				for (EPADProjectTemplate pt: t.projectTemplates) {
					if (pt.projectID.equalsIgnoreCase(projectID)) {
						log.info("adding template "+ t.templateCode);
						templates.addTemplate(t);
						
					}
					if (pt.projectID.equals(EPADConfig.xnatUploadProjectID)) {
						
//						isInAllProject=true;
//						//if the template's project is all project and template is not disabled for this project
//						if (isInAllProject) 
						{
							List<String> disabledTemplatesNames = projectOperations.getDisabledTemplateCodes(projectID);
							//can keep the same it is using templatecode (starting 2.3)
							if (!(disabledTemplatesNames.contains(t.fileName) || disabledTemplatesNames.contains(t.templateName) || disabledTemplatesNames.contains(t.templateCode))){
								if (!templates.ResultSet.Result.contains(t)){ 
									templates.addTemplate(t);
								}
//							}else {
//								log.info("disabled in project, removing template "+ t.templateCode);
//								templates.ResultSet.Result.remove(t);
//								templates.ResultSet.totalRecords--;
							}
								
						}
					}
				}
			}
			else if ((t.projectID!=null && t.projectID.equalsIgnoreCase(projectID))){
				templates.addTemplate(t);
			}
			


		}
		return templates;


		//		EPADTemplateContainerList fileList = new EPADTemplateContainerList();
		//		List<EpadFile> efiles = projectOperations.getEpadFiles(projectID, null, null, null, FileType.TEMPLATE, false);
		//		Set<String> templateCodes = new HashSet<String>();
		//		//ml defaulttemplate
		//		Project userProj = projectOperations.getProjectForUser(username, projectID);
		//		String defTemplate="";
		//		if (userProj != null)
		//		{
		//			defTemplate = userProj.getDefaultTemplate();
		//		}
		//
		//		for (EpadFile efile: efiles)
		//		{
		//			EPADTemplateContainer template = convertEpadFileToTemplate(projectID, efile, new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile)));
		//			if (!template.templateLevelType.equalsIgnoreCase(efile.getTemplateLevelType())) {//file (db) and template (xml) different
		//				log.info("xml and db different for template "+template.templateName +"!! Updating db. it was "+ efile.getTemplateLevelType()+ " changing to " +template.templateLevelType);
		//				efile.setTemplateLevelType(template.templateLevelType);
		//				efile.save();
		//			}
		//			if (template.enabled && (templateLevelFilter==null || template.templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase())))
		//			{
		//				log.info("description " + templateLevelFilter+ " template " + template.templateLevelType);
		//				//ml default template
		//				List<EPADTemplate> templates = template.templates;
		//				for (EPADTemplate t: templates)
		//				{
		//					log.info("template : " +t.getTemplateCode() + " defaulttemplate :"+defTemplate);
		//					if (t.getTemplateCode().equals(defTemplate))
		//					{
		//						t.defaultTemplate = true;
		//					}
		//					else
		//						t.defaultTemplate = false;
		//				}
		//				fileList.addTemplate(template);
		//				templateCodes.add(template.templateCode);
		//			}
		//		}
		//		efiles = projectOperations.getEpadFiles(EPADConfig.xnatUploadProjectID, null, null, null, FileType.TEMPLATE, false);
		//		List<String> disabledTemplatesNames = projectOperations.getDisabledTemplates(projectID);
		//
		//
		//		log.info("Default template for project "+ userProj.getName() + " is "+defTemplate);
		//
		//		for (EpadFile efile: efiles)
		//		{
		//
		//			EPADTemplateContainer template = convertEpadFileToTemplate(projectID, efile, new File(EPADConfig.getEPADWebServerResourcesDir() + getEpadFilePath(efile)));
		//			log.info("template enabled: " +template.enabled + " code: "+ template.templateCode + " codecontain:"+templateCodes.contains(template.templateCode));
		//			if (template.enabled && !disabledTemplatesNames.contains(template.fileName) 
		//					&& !disabledTemplatesNames.contains(template.templateName) 
		//					&& !disabledTemplatesNames.contains(template.templateCode) 
		//					&& !templateCodes.contains(template.templateCode)  
		//					&& (templateLevelFilter==null || template.templateLevelType.toLowerCase().startsWith(templateLevelFilter.toLowerCase()))){
		//
		//				//ml default template
		//				List<EPADTemplate> templates = template.templates;
		//				for (EPADTemplate t: templates)
		//				{
		//					log.info("template : " +t.getTemplateCode() + " defaulttemplate :"+defTemplate);
		//					if (t.getTemplateCode().equals(defTemplate))
		//					{
		//						t.defaultTemplate = true;
		//					}
		//					else
		//						t.defaultTemplate = false;
		//				}
		//
		//				fileList.addTemplate(template);
		//			}
		//
		//		}
		//		return fileList;
	}


	private EPADTemplateContainer convertEpadFileToTemplate(String projectId, EpadFile efile, File templateFile)
	{
		String description = efile.getDescription();
		String templateName = "";
		String templateType = "";
		String templateLevelType = "";
		String templateCode = "";
		String templateDescription = "";
		String modality = "";
		List<EPADTemplate> epadTmpls = new ArrayList<EPADTemplate>();
		try {
			String xml = EPADFileUtils.readFileAsString(templateFile);
			JSONObject root = XML.toJSONObject(xml);
			JSONObject container = root.getJSONObject("TemplateContainer");
			JSONArray templateObjs = new JSONArray();
			try {
				JSONObject templateObj = container.getJSONObject("Template");
				templateObjs.put(templateObj);
			}
			catch (Exception x) {
				templateObjs = container.getJSONArray("Template");
			}
			for (int i = 0; i < templateObjs.length(); i++)
			{
				JSONObject templateObj = templateObjs.getJSONObject(i);
				String templateUID = templateObj.getString("uid");
				templateName = templateObj.getString("name");
				templateType = templateObj.getString("codeMeaning");
				templateCode = templateObj.getString("codeValue");
				try {
					templateDescription = templateObj.getString("description");
					modality = templateObj.getString("modality");
				} catch (Exception x) {}
				try {
					templateLevelType = templateObj.getString("templateType").toLowerCase();
				} catch (Exception x) {
					templateLevelType = "image";
				}
				if (templateLevelType==null) //if still null change to default
					templateLevelType = "image";
				EPADTemplate epadTmpl = new EPADTemplate(templateUID, templateName, templateType, templateCode,
						templateDescription, modality, templateLevelType);
				epadTmpls.add(epadTmpl);
			}
		} catch (Exception x) {}
		if (description == null || description.trim().length() == 0)
		{
			description = "image"; // Image template type
			//			if (templateCode.startsWith("SEG"))
			//				description = "segmentation"; // Image template type				
		}
		EPADTemplateContainer template = new EPADTemplateContainer(projectId, "", "", "", "", efile.getName(), efile.getLength(), FileType.TEMPLATE.getName(), 
				formatDate(efile.getCreatedTime()), getEpadFilePath(efile), efile.isEnabled(), description, templateLevelType);
		template.templateName = templateName;
		template.templateType = templateType;
		template.templateCode = templateCode;
		template.templateDescription = templateDescription;
		template.modality = modality;
		template.templates = epadTmpls;
		return template;
	}

	@Override
	public void deleteFile(String username, ProjectReference projectReference,
			String fileName) throws Exception {
		projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, null, fileName, "DELETE FILE", fileName, false);
		User user = projectOperations.getUser(username);
		Project project = projectOperations.getProject(projectReference.projectID);
		if (user.isAdmin() && fileName.equals("*"))
		{
			List<EpadFile> files = projectOperations.getEpadFiles(projectReference.projectID, null, null, null, null, true);
			for (EpadFile file:files)
			{
				try {
					projectOperations.deleteFile(username, projectReference.projectID, null, null, null, file.getName());
				} catch (Exception x) {

				}
			}

		}
		else
		{
			EpadFile file = projectOperations.getEpadFile(projectReference.projectID, null, null, null, fileName);
			if (file.getFileType().equalsIgnoreCase(FileType.TEMPLATE.getName())) {
				throw new Exception("Do not use this endpoint for deleting templates. Use delete template endpoint");
			}
			if (file == null && !user.isAdmin())
				throw new Exception("No permissions to delete system template");
			if (file != null && !username.equals(file.getCreator()) && !projectOperations.isOwner(username, projectReference.projectID))
				throw new Exception("No permissions to delete this template");
			if (file != null)
			{
				projectOperations.deleteFile(username, projectReference.projectID, null, null, null, fileName);
			}
			else
			{
				File template = new File(EPADConfig.getEPADWebServerTemplatesDir(), fileName);
				if (template.exists())
					template.delete();
				else
					throw new Exception("Template file not found");
			}
		}
	}

	
	
	@Override
	public void deleteTemplate(String username, 
			String templatecode) throws Exception {
		Template t=getTemplate(templatecode);
		if (t==null) {
			log.warning("Template with code "+templatecode+" does not exist");
			return;
		}
			
		User requestor = projectOperations.getUser(username);
		EpadFile f=(EpadFile) projectOperations.getDBObject(EpadFile.class, t.getFileId());
		if (f==null) {
			log.warning("No file for template with code "+templatecode);
			return;
		}
		List <EpadFile> files= new EpadFile().getObjects(" name='"+f.getName() + "' and length="+f.getLength()); 
		int usersFiles=0;
		for (EpadFile fl:files) { 
			log.info("found file:"+fl.getId() );
			if (username.equals(fl.getCreator()))
					usersFiles++;
		}
		if (!requestor.isAdmin() && !(usersFiles==files.size()))
			throw new Exception("No permissions to delete template");
		log.info("Deleting Template, templatecode: " + templatecode);
		
		new ProjectToTemplate().deleteObjects("template_id=" + t.getId());
		//migration check multiple files
		if (usersFiles>1) {
			for (EpadFile fl:files) { 
				deleteFile(username,fl.getId());
			}
		}else {
			deleteFile(username,t.getFileId());
		}
		//deleteFile also deletes the template and projecttemplate entries. it is ok, I am handling migration first 
		new Template().deleteObjects("id=" + t.getId());
	
	}

	@Override
	public void deleteFile(String username, long fileId) throws Exception {
		EpadFile f=(EpadFile) projectOperations.getDBObject(EpadFile.class, fileId);
		Project p=(Project) projectOperations.getDBObject(Project.class, f.getProjectId());
		projectOperations.deleteFile(username,p.getProjectId(),null,null,null, f.getName(), f.getFileType());
	}
	
	@Override
	public void deleteFile(String username, SubjectReference subjectReference,
			String fileName) throws Exception {
		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, fileName, "DELETE FILE", fileName, false);
		projectOperations.deleteFile(username, subjectReference.projectID, subjectReference.subjectID, null, null, fileName);		
	}

	@Override
	public void deleteFile(String username, StudyReference studyReference,
			String fileName) throws Exception {
		projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, null, fileName, "DELETE FILE", fileName, false);
		projectOperations.deleteFile(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, fileName);		
	}

	@Override
	public void deleteFile(String username, SeriesReference seriesReference,
			String fileName) throws Exception {
		projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, null, fileName, "DELETE FILE", fileName, false);
		projectOperations.deleteFile(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, fileName);		
	}

	@Override
	public void deleteFile(String username, String fileName) throws Exception {
		projectOperations.createEventLog(username, null, null, null, null, null, null, fileName, "DELETE FILE", fileName, false);
		projectOperations.deleteFile(username, null, null, null, null, fileName);		
	}

	@Override
	public String setSubjectStatus(SubjectReference subjectReference, String sessionID, String username) throws Exception {
		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, "SET STATUS", subjectReference.status);
		projectOperations.setUserStatusForProjectAndSubject(username, subjectReference.projectID, subjectReference.subjectID, subjectReference.status);
		return "";
	}

	@Override
	public int projectDelete(String projectID, String sessionID, String username) throws Exception
	{
		int xnatStatusCode;

		if (projectID.equals(EPADConfig.xnatUploadProjectID)) 
			throw new RuntimeException("Project " + EPADConfig.xnatUploadProjectID + " can not be deleted");
		projectOperations.createEventLog(username, projectID, null, null, null, null, null, "DELETE PROJECT", null);
		projectOperations.deleteProject(username, projectID);
		this.deleteAllAims(projectID, null, null, null, true);
		xnatStatusCode = HttpServletResponse.SC_OK;
		return xnatStatusCode;
	}

	@Override
	public int subjectDelete(SubjectReference subjectReference, String sessionID, String username) throws Exception
	{
		return subjectDelete(subjectReference, sessionID, username,false);
	}

	@Override
	public int subjectDelete(SubjectReference subjectReference, String sessionID, String username,boolean all) throws Exception
	{
		List<Study> studies = projectOperations.getStudiesForSubject(subjectReference.subjectID);
		int xnatStatusCode;
		User user = projectOperations.getUser(username);
		if (!user.isAdmin() && !projectOperations.isOwner(username, subjectReference.projectID))
			throw new Exception("No permissions to delete Patient: " + subjectReference.subjectID + " in project " + subjectReference.projectID);

		if (!user.isAdmin() && all) {
			throw new Exception("No permissions to delete Patient: " + subjectReference.subjectID + " from system. You are not admin. Please select delete from project. ");
		}

		projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, null, "DELETE SUBJECT", null);
		log.info("Scheduling deletion task for patient " + subjectReference.subjectID + " in project "
				+ subjectReference.projectID + " from user " + username);

		//ml all added for deleting from system 
		(new Thread(new SubjectDataDeleteTask(subjectReference.projectID, subjectReference.subjectID, username, all))).run();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		projectOperations.deleteSubject(username, subjectReference.subjectID, subjectReference.projectID);
		this.deleteAllAims(subjectReference.projectID, subjectReference.subjectID, null, null, true);
		xnatStatusCode = HttpServletResponse.SC_OK;

		return xnatStatusCode;
	}


	@Override
	public String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username) throws Exception
	{
		return this.studyDelete(studyReference, sessionID, deleteAims, username, false);

	}
	@Override
	public String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username, boolean all) throws Exception
	{
		int xnatStatusCode;
		User user = projectOperations.getUser(username);
		if (!user.isAdmin() && !projectOperations.isOwner(username, studyReference.projectID))
			throw new Exception("No permissions to delete Study: " + studyReference.studyUID + " in project " + studyReference.projectID);
		if (!user.isAdmin() && all)
			throw new Exception("No permissions to delete Study: " + studyReference.studyUID + " from system. You are not admin. Please select delete from project. ");

		projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, null, "DELETE STUDY", null);
		
		
		if (projectOperations.getProjectsForStudy(studyReference.studyUID).size()>1) {
			//do this if not trying to delete from all. 
			//if we do it when the study is in only all project the StudyDataTask throws null pointer exception
			log.info("Deleting in XNAT: study " + studyReference.studyUID + " for patient "
					+ studyReference.subjectID + " in project " + studyReference.projectID + " from user " + username);
	
			projectOperations.deleteStudy(username, studyReference.studyUID, studyReference.subjectID, studyReference.projectID);
		}
		this.deleteAllAims(studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, true);
		xnatStatusCode = HttpServletResponse.SC_OK;

		log.info("Delete Study Status from XNAT:" + xnatStatusCode);
		if (XNATDeletionOperations.successStatusCode(xnatStatusCode))
		{
			log.info("Scheduling deletion task for study " + studyReference.studyUID + " for patient "
					+ studyReference.subjectID + " in project " + studyReference.projectID + " from user " + username);
			//ml all added
			(new Thread(new StudyDataDeleteTask(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, deleteAims, all)))
			.run();
			return "";
		}
		else
			return "Error deleting Study in XNAT";
	}

	@Override
	public void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID, boolean deleteAims)
	{
		// Now delete studies from dcm4chee and ePAD's database; includes deleting PNGs for studies.
		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
		log.info("Found " + seriesUIDs.size() + " series in study " + studyUID);
		if (!seriesUIDs.isEmpty()){
			log.info("Deleting study " + studyUID + " from dcm4chee's database");
			boolean success = Dcm4CheeOperations.deleteStudy(studyUID); // Must run after finding series in DCM4CHEE
			if (!success) { //do not throw error if the study doesn't have any series
				throw new RuntimeException("Error deleting study in dcm4chee");
			}
		}
		// First delete all series in study from ePAD's database; then delete the study itself.
		// Should not delete until after deleting study in DCM4CHEE or PNG pipeline will activate.
		for (String seriesUID : seriesUIDs) {
			log.info("Deleting series " + seriesUID + " from ePAD database");
			epadDatabaseOperations.deleteSeries(seriesUID);
		}
		log.info("Deleting study " + studyUID + " from ePAD database");
		epadDatabaseOperations.deleteStudy(studyUID);

		// Delete the underlying PNGs for the study
		PNGFilesOperations.deletePNGsForStudy(studyUID);

		if (deleteAims)
			deleteAllStudyAims(studyUID, false);
	}

	@Override
	public void deleteAllStudyAims(String studyUID, boolean deleteDSOs) {
		// Delete all Study AIMs
		StudyReference sref = new StudyReference(null, null, studyUID);
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(sref);
		for (EPADAIM aim :aims)
		{
			epadDatabaseOperations.deleteAIM("", aim.aimID);
			AIMUtil.deleteAIM(aim.aimID, aim.projectID);
		}
	}

	@Override
	public void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs)
	{
		for (String studyUID : studyUIDs)
			deleteStudyFromEPadAndDcm4CheeDatabases(studyUID, true);
	}

	@Override
	public String createProjectAIM(String username,
			ProjectReference projectReference, String aimID, File aimFile,
			String sessionID) {
		try {
			EPADAIM aim = epadDatabaseOperations.getAIM(aimID);
			if (!"admin".equals(username) && !projectOperations.hasAccessToProject(username, projectReference.projectID) && (aim == null || !aim.userName.equalsIgnoreCase(username)))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, aimID, "CREATE AIM", aimFile.getName());
			if (aim != null && !aim.projectID.equals(projectReference.projectID)) {
				moveAIMtoProject(aim, projectReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, projectReference.projectID, sessionID, username))
				return "";
			else
				return "Error saving AIM file";
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	private void moveAIMtoProject(EPADAIM aim, String projectID, String username) throws Exception {
		if (projectID.equals(EPADConfig.xnatUploadProjectID))
			throw new Exception("Invalid projectID for an AIM");
		String aimID = aim.aimID;
		if (aim.studyUID != null && aim.studyUID.length() > 0
				&& projectOperations.isStudyInProjectAndSubject(projectID, aim.subjectID, aim.studyUID)) {
			epadDatabaseOperations.updateAIM(aimID, projectID, username);
		} else if (aim.subjectID != null && aim.subjectID.length() > 0
				&& projectOperations.isSubjectInProject(aim.subjectID, projectID)) {
			epadDatabaseOperations.updateAIM(aimID, projectID, username);					
		} else if (aim.subjectID == null || aim.subjectID.length() == 0) {
			epadDatabaseOperations.updateAIM(aimID, projectID, username);					
		} else {
			throw new Exception("Invalid projectID for this AIM");
		}		
	}

	@Override
	public String createSubjectAIM(String username,
			SubjectReference subjectReference, String aimID, File aimFile,
			String sessionID) {
		if (subjectReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) ||
				subjectReference.projectID.equals(EPADConfig.xnatUploadProjectID)
				)
			return "AIM can not be added to project:" + subjectReference.projectID;
		try {
			projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, aimID, "CREATE AIM", aimFile.getName());
			EPADAIM aim = epadDatabaseOperations.addAIM(username, subjectReference, aimID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(subjectReference.projectID)) {
				moveAIMtoProject(aim, subjectReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, aim.projectID, sessionID, username))
				return "";
			else
				return "Error saving AIM file";
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	@Override
	public String createStudyAIM(String username, StudyReference studyReference, String aimID, File aimFile, String sessionID)
	{
		if (studyReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) ||
				studyReference.projectID.equals(EPADConfig.xnatUploadProjectID)
				)
			return "AIM can not be added to project:" + studyReference.projectID;
		try {
			projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, aimID, "CREATE AIM", aimFile.getName());
			EPADAIM aim = epadDatabaseOperations.addAIM(username, studyReference, aimID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(studyReference.projectID)) {
				moveAIMtoProject(aim, studyReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, aim.projectID, sessionID, username))
				return "";
			else
				return "Error saving AIM file";
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	@Override
	public String createSeriesAIM(String username, SeriesReference seriesReference, String aimID, File aimFile, String sessionID)
	{
		if (seriesReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) ||
				seriesReference.projectID.equals(EPADConfig.xnatUploadProjectID)
				)
			return "AIM can not be added to project:" + seriesReference.projectID;
		try {
			projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, aimID, "CREATE AIM", aimFile.getName());
			EPADAIM aim = epadDatabaseOperations.addAIM(username, seriesReference, aimID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(seriesReference.projectID)) {
				moveAIMtoProject(aim, seriesReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, aim.projectID, sessionID, username))
				return "";
			else
				return "Error saving AIM file";
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	@Override
	public String createImageAIM(String username, ImageReference imageReference, String aimID, File aimFile, String sessionID)
	{
		if (imageReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) ||
				imageReference.projectID.equals(EPADConfig.xnatUploadProjectID)
				)
			return "AIM can not be added to project:" + imageReference.projectID;
		try {
			projectOperations.createEventLog(username, imageReference.projectID, imageReference.subjectID, imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, aimID, "CREATE AIM", aimFile.getName());
			//see if it an update to decide whether to delete or not
			EPADAIM oldAim = epadDatabaseOperations.getAIM(aimID);
			
			EPADAIM aim = epadDatabaseOperations.addAIM(username, imageReference, aimID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(imageReference.projectID)) {
				moveAIMtoProject(aim, imageReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, aim.projectID, sessionID, username))
				return "";
			else{
				//see if it an update and do not delete if so
				if (oldAim==null){
					//delete the db entry if you couldn't save the file!
					epadDatabaseOperations.deleteAIM(username, aim.aimID);
				}
				return "Error saving AIM file";
			}
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	@Override
	public String createFrameAIM(String username, FrameReference frameReference, String aimID, File aimFile, String sessionID)
	{
		if (frameReference.projectID.equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned")) ||
				frameReference.projectID.equals(EPADConfig.xnatUploadProjectID)
				)
			return "AIM can not be added to project:" + frameReference.projectID;
		try {
			projectOperations.createEventLog(username, frameReference.projectID, frameReference.subjectID, frameReference.studyUID, frameReference.seriesUID, frameReference.imageUID, aimID, "CREATE AIM", aimFile.getName() + ":" + frameReference.frameNumber);
			EPADAIM aim = epadDatabaseOperations.addAIM(username, frameReference, aimID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to update AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to update AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(frameReference.projectID)) {
				moveAIMtoProject(aim, frameReference.projectID, username);
			}
			if (!AIMUtil.saveAIMAnnotation(aimFile, aim.projectID, frameReference.frameNumber, sessionID, username, false))
				return "";
			else
				return "Error saving AIM file";
		} catch (Exception e) {
			log.warning("Error saving AIM file ",e);
			return e.getMessage();
		}
	}

	@Override
	public int projectAIMDelete(ProjectReference projectReference, String aimID,
			String sessionID, boolean deleteDSO, String username) throws Exception {
		try {
			projectOperations.createEventLog(username, projectReference.projectID, null, null, null, null, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (aim == null)
			{
				log.warning("AIM " + aimID + " not found");
				return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			}
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (!aim.projectID.equals(projectReference.projectID))
			{
				epadDatabaseOperations.removeProjectFromAIM(projectReference.projectID, aimID);
				return HttpServletResponse.SC_OK;
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			log.info("Deleting AIM, deleteDSO:" + deleteDSO + " dsoSeriesUID:" + aim.dsoSeriesUID + " aimID:" + aimID);
			AIMUtil.deleteAIM(aimID, projectReference.projectID);
			epadDatabaseOperations.deleteAIM(username, projectReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				log.info("Deleting DSO Series:" + aim.dsoSeriesUID + " In project:" + aim.projectID);
				this.deleteSeries(new SeriesReference(projectReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0)
			{
				List<EPADAIM> otheraims = epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID);
				if (otheraims.size() == 0 || (otheraims.size() == 1 && otheraims.get(0).projectID.equals(EPADConfig.xnatUploadProjectID)))
				{
					String error =  this.deleteSeries(new SeriesReference(projectReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
					if (error != null && error.length() > 0) {
						log.warning("Error deleting DSO, seriesUID:" + aim.dsoSeriesUID);
						epadDatabaseOperations.insertEpadEvent(
								username, 
								"Error deleting DSO Series", 
								aim.dsoSeriesUID, "", aim.subjectID, aim.subjectID, aim.studyUID, projectReference.projectID, error);					
					}
				}
				else
				{
					log.info("DSO not deleted, seriesUID:" + aim.dsoSeriesUID);
					epadDatabaseOperations.insertEpadEvent(
							username, 
							"DSO Series not deleted", 
							aim.dsoSeriesUID, "", aim.subjectID, aim.subjectID, aim.studyUID, projectReference.projectID, "DSO referenced by another AIM in " + otheraims.get(0).projectID);					
				}
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int subjectAIMDelete(SubjectReference subjectReference, String aimID,
			String sessionID, boolean deleteDSO, String username) throws Exception {
		try {
			projectOperations.createEventLog(username, subjectReference.projectID, subjectReference.subjectID, null, null, null, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, subjectReference.projectID);
			epadDatabaseOperations.deleteAIM(username, subjectReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(subjectReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception
	{
		try {
			projectOperations.createEventLog(username, studyReference.projectID, studyReference.subjectID, studyReference.studyUID, null, null, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, studyReference.projectID);
			epadDatabaseOperations.deleteAIM(username, studyReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(studyReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception
	{
		try {
			projectOperations.createEventLog(username, seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID, seriesReference.seriesUID, null, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, seriesReference.projectID);
			epadDatabaseOperations.deleteAIM(username, seriesReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0)
			{
				List<EPADAIM> otheraims = epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID);
				if (otheraims.size() == 0)
				{
					String error = this.deleteSeries(new SeriesReference(seriesReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
					if (error != null && error.length() > 0)
						log.warning("Error deleting DSO, seriesUID:" + aim.dsoSeriesUID);
					epadDatabaseOperations.insertEpadEvent(
							username, 
							"Error deleting DSO Series", 
							aim.dsoSeriesUID, "", aim.subjectID, aim.subjectID, aim.studyUID, seriesReference.projectID, error);					
				}
				else
				{
					log.info("DSO not deleted, seriesUID:" + aim.dsoSeriesUID);
					epadDatabaseOperations.insertEpadEvent(
							username, 
							"DSO Series not deleted", 
							aim.dsoSeriesUID, "", aim.subjectID, aim.subjectID, aim.studyUID, seriesReference.projectID, "DSO referenced by another AIM in " + otheraims.get(0).projectID);					
				}
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception
	{
		try {
			projectOperations.createEventLog(username, imageReference.projectID, imageReference.subjectID, imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, imageReference.projectID);
			epadDatabaseOperations.deleteAIM(username, imageReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(imageReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception
	{
		try {
			projectOperations.createEventLog(username, frameReference.projectID, frameReference.subjectID, frameReference.studyUID, frameReference.seriesUID, frameReference.imageUID, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO + ":" + frameReference.frameNumber);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, frameReference.projectID);
			epadDatabaseOperations.deleteAIM(username, frameReference, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(frameReference.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public int aimDelete(String aimID, String sessionID, boolean deleteDSO, String username) throws Exception {
		try {
			projectOperations.createEventLog(username, null, null, null, null, null, aimID, "DELETE AIM", "deleteDSO:" + deleteDSO);
			EPADAIM aim = getAIMDescription(aimID, username, sessionID);
			if (!"admin".equals(username) && !aim.userName.equalsIgnoreCase(username) && !aim.userName.equalsIgnoreCase("shared") && !UserProjectService.isOwner(sessionID, username, aim.projectID))
			{
				log.warning("No permissions to delete AIM:" + aimID + " for user " + username);
				throw new Exception("No permissions to delete AIM:" + aimID + " for user " + username);
			}
			if (AIMUtil.isPluginStillRunning(aimID))
				throw new Exception(aimID + " is still being processed by the plugin");
			AIMUtil.deleteAIM(aimID, aim.projectID);
			epadDatabaseOperations.deleteAIM(username, aimID);
			if (deleteDSO && aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0 && epadDatabaseOperations.getAIMsByDSOSeries(aim.dsoSeriesUID).size() == 0)
			{
				this.deleteSeries(new SeriesReference(aim.projectID, aim.subjectID, aim.studyUID, aim.dsoSeriesUID), false);
			}
			return HttpServletResponse.SC_OK;
		} catch (Exception e) {
			log.warning("Error deleting AIM file ",e);
			throw e;
		}
	}

	@Override
	public EPADAIMList getProjectAIMDescriptions(ProjectReference projectReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(projectReference);
		for (int i = 0; i < aims.size(); i++)
		{
			if (!projectReference.projectID.equals(aims.get(i).projectID))
			{
				aims.remove(i);
				i--;
				continue;
			}
			EPADAIM aim = aims.get(i);
			if (aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0) {
				Map<String, String> seriesMap = dcm4CheeDatabaseOperations.getSeriesData(aim.dsoSeriesUID);
				if (seriesMap.keySet().isEmpty())
				{
					aims.remove(i--);
					if (aim.template != null && aim.template.equals("SEG"))
					{
						try {
							AIMUtil.deleteAIM(aim.aimID, aim.projectID);
						} catch (Exception x) {};
					}
				}
			}
			SeriesProcessingStatus status = epadDatabaseOperations.getSeriesProcessingStatus(aim.dsoSeriesUID);
			if (status != null)
				aim.dsoStatus = status.name();
		}
		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(subjectReference);
		List<EPADAIM> sharedAims = epadDatabaseOperations.getSharedAIMs(subjectReference.projectID, subjectReference.subjectID, null);
		for (EPADAIM aim: sharedAims)
		{
			aims.add(aim);
		}
		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIM getProjectAIMDescription(ProjectReference projectReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(projectReference, aimID);
	}

	@Override
	public EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(subjectReference, aimID);
	}

	@Override
	public EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(studyReference, aimID);
	}

	@Override
	public EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username,
			String sessionID)
	{
		return epadDatabaseOperations.getAIM(seriesReference, aimID);
	}

	@Override
	public EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(imageReference, aimID);
	}

	@Override
	public EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID)
	{
		return epadDatabaseOperations.getAIM(frameReference, aimID);
	}


	@Override
	public EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(studyReference);

		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(seriesReference);
		List<EPADAIM> sharedAims = epadDatabaseOperations.getSharedAIMs(seriesReference.projectID, seriesReference.subjectID, seriesReference.seriesUID);
		for (EPADAIM aim: sharedAims)
		{
			aims.add(aim);
		}
		for (int i = 0; i < aims.size(); i++)
		{
			EPADAIM aim = aims.get(i);
			if (aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0) {
				Map<String, String> seriesMap = dcm4CheeDatabaseOperations.getSeriesData(aim.dsoSeriesUID);
				if (seriesMap.keySet().isEmpty())
				{
					aims.remove(i--);
				}
			}
			SeriesProcessingStatus status = epadDatabaseOperations.getSeriesProcessingStatus(aim.dsoSeriesUID);
			if (status != null)
				aim.dsoStatus = status.name();
		}
		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getSeriesAIMDescriptions(
			SeriesReference seriesReference, String username, String sessionID,
			boolean includeStudyAims) {
		EPADAIMList aims = this.getSeriesAIMDescriptions(seriesReference, username, sessionID);
		StudyReference studyReference = new StudyReference(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID);
		EPADAIMList studyaims = this.getStudyAIMDescriptions(studyReference, username, sessionID);
		for (EPADAIM aim: aims.ResultSet.Result)
		{
			studyaims.addAIM(aim);
		}
		return studyaims;
	}

	@Override
	public EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(imageReference);

		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID)
	{
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(frameReference);

		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getAIMDescriptions(String projectID, AIMSearchType aimSearchType, String searchValue, String username, String sessionID, int start, int count) {
		List<EPADAIM> aims = epadDatabaseOperations.getAIMs(projectID, aimSearchType, searchValue, start, count);
		for (int i = 0; i < aims.size(); i++)
		{
			EPADAIM aim = aims.get(i);
			if (aim.dsoSeriesUID != null && aim.dsoSeriesUID.length() > 0) {
				Map<String, String> seriesMap = dcm4CheeDatabaseOperations.getSeriesData(aim.dsoSeriesUID);
				if (seriesMap.keySet().isEmpty())
				{
					aims.remove(i--);
					if (aim.template != null && aim.template.equals("SEG"))
					{
						try {
							AIMUtil.deleteAIM(aim.aimID, aim.projectID);
						} catch (Exception x) {};
					}
				}
			}
			SeriesProcessingStatus status = epadDatabaseOperations.getSeriesProcessingStatus(aim.dsoSeriesUID);
			if (status != null)
				aim.dsoStatus = status.name();
		}

		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIMList getAIMDescriptionsForUser(String username,
			String sessionID) {
		List<EPADAIM> aims = epadDatabaseOperations.getAIMsByQuery("UserLoginName = '" + username + "' order by PatientID asc, UpdateTime desc");

		return new EPADAIMList(aims);
	}

	@Override
	public EPADAIM getAIMDescription(String aimID, String username,
			String sessionID) {
		return epadDatabaseOperations.getAIM(aimID);
	}

	@Override
	public Collection<EPADSession> getCurrentSessions(String username) throws Exception {
		User user = projectOperations.getUser(username);
		if (!user.isAdmin())
			throw new Exception("No permissions for requested data");
		Map<String, EPADSession> sessions = EPADSessionOperations.getCurrentSessions();
		return sessions.values();
	}

	@Override
	public EPADUsageList getUsageSummary(String username) throws Exception {
		String sql = " createdtime =(select max(createdtime) from epadstatistics b where b.host = a.host) group by host order by host";
		List<EpadStatistics> stats = new EpadStatistics().getObjects(sql);
		EPADUsageList eul = new EPADUsageList();
		for (EpadStatistics stat: stats)
		{
			eul.addUsage(new EPADUsage(stat.getHost(), stat.getNumOfUsers(), stat.getNumOfProjects(),
					stat.getNumOfPatients(), stat.getNumOfStudies(), stat.getNumOfSeries(),
					stat.getNumOfAims(), stat.getNumOfDSOs(), stat.getNumOfPacs(), stat.getNumOfAutoQueries(),
					stat.getNumOfWorkLists(), stat.getNumOfFiles(), stat.getNumOfTemplates(), stat.getNumOfPlugins(), dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	
	@Override
	public EPADUsageList getMonthlyUsageSummary() throws Exception {
		List<EpadStatisticsMonthly> stats = new EpadStatisticsMonthly().getObjects("");
		EPADUsageList eul = new EPADUsageList();
		for (EpadStatisticsMonthly stat: stats)
		{
			eul.addUsage(new EPADUsage("", stat.getNumOfUsers(), stat.getNumOfProjects(),
					stat.getNumOfPatients(), stat.getNumOfStudies(), stat.getNumOfSeries(),
					stat.getNumOfAims(), stat.getNumOfDSOs(), stat.getNumOfPacs(), stat.getNumOfAutoQueries(),
					stat.getNumOfWorkLists(), stat.getNumOfFiles(), stat.getNumOfTemplates(), stat.getNumOfPlugins(), dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	@Override
	public EPADUsageList getMonthlyUsageSummaryForMonth(int month) throws Exception {
		String sql = " month(createdtime)="+month;
		List<EpadStatisticsMonthly> stats = new EpadStatisticsMonthly().getObjects(sql);
		EPADUsageList eul = new EPADUsageList();
		for (EpadStatisticsMonthly stat: stats)
		{
			eul.addUsage(new EPADUsage("", stat.getNumOfUsers(), stat.getNumOfProjects(),
					stat.getNumOfPatients(), stat.getNumOfStudies(), stat.getNumOfSeries(),
					stat.getNumOfAims(), stat.getNumOfDSOs(), stat.getNumOfPacs(), stat.getNumOfAutoQueries(),
					stat.getNumOfWorkLists(), stat.getNumOfFiles(), stat.getNumOfTemplates(), stat.getNumOfPlugins(), dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	
	@Override
	public EPADTemplateUsageList getTemplateStatSummary() throws Exception {
		String sql = "createdtime >(((select max(createdtime) from epadstatistics_template b where b.host = a.host)- INTERVAL 1 HOUR))  group by host,templatecode order by host,templatecode";
		log.info("template stat query: "+sql);
		List<EpadStatisticsTemplate> stats = new EpadStatisticsTemplate().getObjects(sql);
		EPADTemplateUsageList eul = new EPADTemplateUsageList();
		for (EpadStatisticsTemplate stat: stats)
		{
			eul.addTemplateUsage(new EPADTemplateUsage(stat.getHost(),stat.getTemplateLevelType(),stat.getTemplateName(),
					stat.getAuthors(),stat.getVersion(),stat.getTemplateDescription(),stat.getTemplateType(),
					stat.getTemplateCode(),stat.getNumOfAims(),null, dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	
	
	
	@Override
	public EPADTemplateUsageList getTemplateStatSummaryWithXML() throws Exception {
		String sql = "createdtime >(((select max(createdtime) from epadstatistics_template b where b.host = a.host)- INTERVAL 1 HOUR))  group by host,templatecode order by host,templatecode";
		log.info("template stat query: "+sql);
		List<EpadStatisticsTemplate> stats = new EpadStatisticsTemplate().getObjects(sql);
		EPADTemplateUsageList eul = new EPADTemplateUsageList();
		for (EpadStatisticsTemplate stat: stats)
		{
			eul.addTemplateUsage(new EPADTemplateUsage(stat.getHost(),stat.getTemplateLevelType(),stat.getTemplateName(),
					stat.getAuthors(),stat.getVersion(),stat.getTemplateDescription(),stat.getTemplateType(),
					stat.getTemplateCode(),stat.getNumOfAims(),stat.getTemplateText(), dateformat.format(stat.getCreatedTime())));
		}
		return eul;
	}
	
	@Override
	public int getActiveCount(int days) throws Exception {
		String sql = "createdtime >(( NOW( ) - INTERVAL "+ days+" DAY)) and createdtime =(select max(createdtime) from epadstatistics b where b.host = a.host) group by host order by host";
		log.info("active count query: "+sql);
		List<EpadStatistics> stats = new EpadStatistics().getObjects(sql);
		return stats.size();
	}

	@Override
	public EPADUsageList getUsage(String username, String hostname, boolean byMonth, boolean byYear, boolean all) throws Exception {
		String sql = "host like '" + hostname.replace('*', '%') + "%' order by host, createdtime desc";
		if (!all && !byMonth && !byYear) sql = "host like '" + hostname.replace('*', '%') + "%' and createdtime =(select max(createdtime) from epadstatistics b where b.host = a.host)";
		List<EpadStatistics> stats = new EpadStatistics().getObjects(sql);
		EpadStatistics total = new EpadStatistics();
		EPADUsageList eul = new EPADUsageList();
		int prevDay = -1 ;
		Date lastDate = null;
		Set<String> counted = new HashSet<String>();
		for (EpadStatistics stat: stats)
		{
			if (byMonth) {
				Date date = stat.getCreatedTime();
				if (!isLastDayOfMonth(date) || counted.contains(stat.getHost()+"_" + getDayOfYear(stat.getCreatedTime())))
					continue;
			}	
			if (byYear) {
				Date date = stat.getCreatedTime();
				if (!isLastDayOfYear(date) || counted.contains(stat.getHost()+"_" + getDayOfYear(stat.getCreatedTime())))
					continue;
			}
			counted.add(stat.getHost()+"_" + getDayOfYear(stat.getCreatedTime()));
			if (prevDay != -1 && prevDay != getDayOfYear(stat.getCreatedTime()) && (byMonth || byYear) && hostname.equals("*"))
			{
				eul.addUsage(new EPADUsage("Total", total.getNumOfUsers(), total.getNumOfProjects(),
						total.getNumOfPatients(), total.getNumOfStudies(), total.getNumOfSeries(),
						total.getNumOfAims(), total.getNumOfDSOs(), total.getNumOfPacs(), total.getNumOfAutoQueries(),
						total.getNumOfWorkLists(), dateformat.format(stat.getCreatedTime())));
				total = new EpadStatistics();
			}
			eul.addUsage(new EPADUsage(stat.getHost(), stat.getNumOfUsers(), stat.getNumOfProjects(),
					stat.getNumOfPatients(), stat.getNumOfStudies(), stat.getNumOfSeries(),
					stat.getNumOfAims(), stat.getNumOfDSOs(), stat.getNumOfPacs(), stat.getNumOfAutoQueries(),
					stat.getNumOfWorkLists(), dateformat.format(stat.getCreatedTime())));
			total.addStatistics(stat);
			prevDay = getDayOfYear(stat.getCreatedTime());
			lastDate = stat.getCreatedTime();
		}
		if ((!all || byMonth || byYear) && hostname.contains("*"))
		{
			eul.addUsage(new EPADUsage("Total", total.getNumOfUsers(), total.getNumOfProjects(),
					total.getNumOfPatients(), total.getNumOfStudies(), total.getNumOfSeries(),
					total.getNumOfAims(), total.getNumOfDSOs(), total.getNumOfPacs(), total.getNumOfAutoQueries(),
					total.getNumOfWorkLists(), dateformat.format(lastDate)));
		}
		return eul;
	}

	boolean isLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		cal.add(Calendar.DATE, 1);
		if (cal.get(Calendar.MONTH) != month)
			return true;
		else
			return false;
	}

	boolean isLastDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		cal.add(Calendar.DATE, 1);
		if (cal.get(Calendar.YEAR) != year)
			return true;
		else
			return false;
	}

	int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	@Override
	public EPADEventLogList getEventLogs(String loggedInUserName, String username, int start, int count) throws Exception {
		User loggedInUser = projectOperations.getUser(loggedInUserName);
		if (!loggedInUser.isAdmin() && !loggedInUserName.equals(username))
			throw new Exception("No permissions for requested data");
		EPADEventLogList elist = new EPADEventLogList();
		List<EventLog> elogs = projectOperations.getUseEventLogs(username, start, count);
		//		if (count > 0 && elogs.size() > (start+count))
		//		{
		//			elogs = elogs.subList(start, start+count);
		//		}
		for(EventLog elog: elogs)
		{
			EPADEventLog log = new EPADEventLog(this.formatDateTime(elog.getCreatedTime()), 
					elog.getUsername(), elog.getProjectID(),
					elog.getSubjectUID(), elog.getStudyUID(), elog.getSeriesUID(),
					elog.getImageUID(), elog.getAimID(), elog.getFunction(), elog.getParams());
			if (elog.getProjectID() != null && elog.getProjectID().length() > 0)
			{
				Project project = projectOperations.getProject(elog.getProjectID());
				if (project != null) log.projectName = project.getName();
			}
			if (elog.getSubjectUID() != null && elog.getSubjectUID().length() > 0)
			{
				Subject subject = projectOperations.getSubject(elog.getSubjectUID());
				if (subject != null) log.subjectName = subject.getName();
			}
			if (elog.getAimID() != null && elog.getAimID().length() > 0)
			{
				EPADAIM aim = epadDatabaseOperations.getAIM(elog.getAimID());
				if (aim != null) log.aimName = aim.name;
			}
			if (elog.isError())
				log.errorMessage = elog.getParams();
			elist.addEPADEventLog(log);
		}
		return elist;
	}

	@Override
	public EPADObjectList getTaskStatuses(String loggedInUserName, String username)
			throws Exception {
		User loggedInUser = projectOperations.getUser(loggedInUserName);
		if (!loggedInUser.isAdmin() && !loggedInUserName.equals(username))
			throw new Exception("No permissions for requested data");
		EPADObjectList list = new EPADObjectList();
		Collection<TaskStatus> tasks = projectOperations.getUser(username).getCurrentTasks().values();
		List<TaskStatus> tss = new ArrayList<TaskStatus>();
		tss.addAll(tasks);
		Collections.sort(tss, new TSComparator());
		for(TaskStatus task: tss)
		{
			list.addObject(task);
		}
		return list;
	}

	public static Date getDate(String dateStr)
	{
		if (dateStr == null || dateStr.length() == 0)
			return new Date();
		try
		{
			return dateTimeFormat.parse(dateStr);
		}
		catch (Exception x) {}
		return null;
	}

	public class TSComparator implements Comparator<TaskStatus> {
		public int compare(TaskStatus o1, TaskStatus o2) {
			try
			{
				//				if (o2.completetime != null)
				//					return getDate(o2.completetime).compareTo(getDate(o1.completetime));
				//				else
				return getDate(o2.starttime).compareTo(getDate(o1.starttime));
			} catch (Exception x) {}
			return 0;
		}
	}

	@Override
	public EPADWorklistList getWorkListsForUser(String loggedInUserName, String username) throws Exception {
		List<WorkList> worklists = null;
		if (projectOperations.isAdmin(loggedInUserName) && username.equals("*"))
		{
			worklists = workListOperations.getAllWorkLists();
		}
		else
		{
			worklists = workListOperations.getWorkListsForUser(username);
			List<WorkList> worklist2 = workListOperations.getWorkListsByUser(username);
			for (WorkList wl: worklist2)
			{
				if (!worklists.contains(wl))
					worklists.add(wl);
			}
		}
		EPADWorklistList wllist = new EPADWorklistList();
		for (WorkList wl: worklists)
		{
			List<Subject> subjects = workListOperations.getSubjectsForWorkList(wl.getWorkListID());
			List<Study> studies = workListOperations.getStudiesForWorkList(wl.getWorkListID());
			List<String> subjectIDs = new ArrayList<String>();
			List<String> studyUIDs = new ArrayList<String>();
			List<String> statuses = new ArrayList<String>();
			List<String> projectIDs = new ArrayList<String>();
			for (Subject subject: subjects)
			{
				subjectIDs.add(subject.getSubjectUID());
				projectIDs.add(subject.getProjectID());
				statuses.add(subject.getStatus());
			}
			for (Study study: studies)
			{
				studyUIDs.add(study.getStudyUID());
				projectIDs.add(study.getProjectID());
				statuses.add(study.getStatus());
			}
			User user = (User) projectOperations.getDBObject(User.class, wl.getUserId());
			wllist.addEPADWorklist(new EPADWorklist(wl.getWorkListID(), user.getUsername(), wl.getName(),
					wl.getDescription(), wl.getStatus(),formatDate(wl.getStartDate()),
					formatDate(wl.getCompleteDate()), formatDate(wl.getDueDate()), projectIDs, studyUIDs, statuses));
		}
		return wllist;
	}

	@Override
	public EPADWorklistStudyList getWorkListStudies(String loggedInUserName, String username,
			String workListID) throws Exception {
		WorkList wl = workListOperations.getWorkList(workListID);
		if (wl == null)
			throw new Exception("Worklist " + workListID + " not found");
		User user = (User) projectOperations.getDBObject(User.class, wl.getUserId());
		//		Set<Subject> subjects = workListOperations.getSubjectsForWorkListWithStatus(wl.getWorkListID());
		//		List<String> subjectIDs = new ArrayList<String>();
		//		List<String> studyUIDs = new ArrayList<String>();
		//		List<String> subjectStatus = new ArrayList<String>();
		//		List<String> studyStatus = new ArrayList<String>();
		//		for (Subject subject: subjects)
		//		{
		//			subjectIDs.add(subject.getSubjectUID());
		//			subjectStatus.add(subject.getSubjectUID() + ":" + subject.getStatus());
		//		}
		List<WorkListToStudy> wstudies = workListOperations.getWorkListStudies(wl.getWorkListID());
		EPADWorklistStudyList wlsl = new EPADWorklistStudyList();
		for (WorkListToStudy wstudy: wstudies)
		{
			Study study = (Study) projectOperations.getDBObject(Study.class, wstudy.getStudyId());
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, study.getSubjectId());
			Project project = (Project) projectOperations.getDBObject(Project.class, wstudy.getProjectId());
			EPADWorklistStudy wls = new EPADWorklistStudy(workListID, username, project.getProjectId(),
					subject.getSubjectUID(), study.getStudyUID(), wstudy.getStatus(), formatDate(wstudy.getStartDate()),
					formatDate(wstudy.getCompleteDate()));
			wls.workListName = wl.getName();
			wls.sortOrder = wstudy.getSortOrder();
			wlsl.addEPADWorklistStudy(wls);
		}
		return wlsl;
	}

	@Override
	public EPADWorklistSubjectList getWorkListSubjects(String loggedInUserName, String username, String workListID) throws Exception {
		WorkList wl = workListOperations.getWorkList(workListID);
		if (wl == null)
			throw new Exception("Worklist " + workListID + " not found");
		User user = (User) projectOperations.getDBObject(User.class, wl.getUserId());
		List<WorkListToSubject> wsubjects = workListOperations.getWorkListSubjects(wl.getWorkListID());
		EPADWorklistSubjectList wlsl = new EPADWorklistSubjectList();
		for (WorkListToSubject wsubject: wsubjects)
		{
			Subject subject = (Subject) projectOperations.getDBObject(Subject.class, wsubject.getSubjectId());
			Project project = (Project) projectOperations.getDBObject(Project.class, wsubject.getProjectId());
			EPADWorklistSubject wls = new EPADWorklistSubject(workListID, username, project.getProjectId(),
					subject.getSubjectUID(), subject.getName(), wsubject.getStatus(), formatDate(wsubject.getStartDate()),
					formatDate(wsubject.getCompleteDate()));
			SubjectReference reference = new SubjectReference(null, subject.getSubjectUID());
			int count = epadDatabaseOperations.getNumberOfAIMs(username, reference);
			wls.workListName = wl.getName();
			wls.sortOrder = wsubject.getSortOrder();
			wls.numberOfAnnotations = count;
			wlsl.addEPADWorklistSubject(wls);
		}
		return wlsl;
	}

	@Override
	public EPADWorklist getWorkListByID(String loggedInUserName, String username, String workListID) throws Exception {
		WorkList wl = workListOperations.getWorkList(workListID);
		User user = (User) projectOperations.getDBObject(User.class, wl.getUserId());
		if (username != null && !username.equals(user.getUsername()))
			throw new Exception("Username for worklist " + workListID + " does not match " + username);
		List<Subject> subjects = workListOperations.getSubjectsForWorkList(wl.getWorkListID());
		List<Study> studies = workListOperations.getStudiesForWorkList(wl.getWorkListID());
		List<String> subjectIDs = new ArrayList<String>();
		List<String> studyUIDs = new ArrayList<String>();
		List<String> statuses = new ArrayList<String>();
		List<String> projectIDs = new ArrayList<String>();
		for (Subject subject: subjects)
		{
			subjectIDs.add(subject.getSubjectUID());
			projectIDs.add(subject.getProjectID());
			statuses.add(subject.getStatus());
		}
		for (Study study: studies)
		{
			studyUIDs.add(study.getStudyUID());
			projectIDs.add(study.getProjectID());
			statuses.add(study.getStatus());
		}

		return new EPADWorklist(wl.getWorkListID(), user.getUsername(), wl.getName(),
				wl.getDescription(), wl.getStatus(),formatDate(wl.getStartDate()),
				formatDate(wl.getCompleteDate()), formatDate(wl.getDueDate()), projectIDs, studyUIDs, statuses);
	}

	private EPADStudy dcm4cheeStudy2EpadStudy(String sessionID, String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEEStudy dcm4CheeStudy, String username)
	{
		return dcm4cheeStudy2EpadStudy(sessionID, suppliedProjectID, suppliedSubjectID, dcm4CheeStudy, username, false);
	}
	private EPADStudy dcm4cheeStudy2EpadStudy(String sessionID, String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEEStudy dcm4CheeStudy, String username, boolean includeAnnotationStatus)
	{
		String projectID = suppliedProjectID == null || suppliedProjectID.equals("") ? EPADConfig.xnatUploadProjectID : suppliedProjectID;
		String patientName = dcm4CheeStudy.patientName;
		String xnatPatientID = XNATUtil.subjectID2XNATSubjectLabel(dcm4CheeStudy.patientID);
		String subjectID = suppliedSubjectID.equals("") ? xnatPatientID : suppliedSubjectID;
		String studyUID = dcm4CheeStudy.studyUID;
		String insertDate = dcm4CheeStudy.dateAcquired; // studyDate
		String firstSeriesUID = dcm4CheeStudy.firstSeriesUID;
		String firstSeriesDateAcquired = dcm4CheeStudy.firstSeriesDateAcquired;
		String physicianName = dcm4CheeStudy.physicianName;
		String birthdate = dcm4CheeStudy.birthdate;
		String sex = dcm4CheeStudy.sex;
		String studyDescription = dcm4CheeStudy.studyDescription;
		String studyAccessionNumber = dcm4CheeStudy.studyAccessionNumber;
		String createdTime = dcm4CheeStudy.createdTime;
		Set<String> examTypes = getExamTypesForStudy(studyUID);
		int numberOfSeries = dcm4CheeStudy.seriesCount;
		int numberOfImages = dcm4CheeStudy.imagesCount;
		//not being used, performance issue fix
//		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);
		StudyProcessingStatus studyProcessingStatus = getStudyProcessingStatus(studyUID);
		//		//int numberOfAnnotations = (seriesUIDs.size() <= 0) ? 0 : AIMQueries.getNumberOfAIMAnnotationsForSeriesSet(seriesUIDs, username);
		//		EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(suppliedProjectID, null, studyUID), null, sessionID);
		//		//log.info("Number of study aims:" + aims.ResultSet.totalRecords + " insertDate:" + insertDate + " createdTime:" + createdTime);
		//		int	numberOfAnnotations = getNumberOfAccessibleAims(sessionID, suppliedProjectID, aims, username);
		//ml for speeding up annotation count
		int numberOfAnnotations = getStudyAimCount(sessionID,studyUID,suppliedProjectID,username);

		AnnotationStatus annotationStatus = null;
		Map<String, AnnotationStatus> userStatusList = null;

		if (includeAnnotationStatus) {
			userStatusList = new HashMap<>();
			annotationStatus = getAnnotationStatusStudy(projectID, subjectID, studyUID, username, userStatusList,numberOfSeries);
			//set userStatusList to null so it doesn't display
			if (userStatusList.size()==0)
				userStatusList = null;
		}

		return new EPADStudy(projectID, subjectID, patientName, studyUID, insertDate, firstSeriesUID,
				firstSeriesDateAcquired, physicianName, birthdate, sex, studyProcessingStatus, examTypes, studyDescription,
				studyAccessionNumber, numberOfSeries, numberOfImages, numberOfAnnotations, createdTime, annotationStatus, userStatusList);
	}


	private EPADSeries dcm4cheeSeries2EpadSeries(String sessionID, String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEESeries dcm4CheeSeries, String username)
	{
		return dcm4cheeSeries2EpadSeries(sessionID, suppliedProjectID, suppliedSubjectID, dcm4CheeSeries, username, false);

	}

	private EPADSeries dcm4cheeSeries2EpadSeries(String sessionID, String suppliedProjectID, String suppliedSubjectID,
			DCM4CHEESeries dcm4CheeSeries, String username, boolean includeAnnotationStatus)
	{
		String projectID = suppliedProjectID == null || suppliedProjectID.equals("") ? EPADConfig.xnatUploadProjectID : suppliedProjectID;
		String patientName = trimTrailing(dcm4CheeSeries.patientName);
		String xnatPatientID = XNATUtil.subjectID2XNATSubjectLabel(dcm4CheeSeries.patientID);
		String subjectID = suppliedSubjectID.equals("") ? xnatPatientID : suppliedSubjectID;
		String studyUID = dcm4CheeSeries.studyUID;
		String seriesUID = dcm4CheeSeries.seriesUID;
		String seriesDate = dcm4CheeSeries.seriesDate;
		String seriesDescription = dcm4CheeSeries.seriesDescription;
		String examType = dcm4CheeSeries.examType;
		String bodyPart = dcm4CheeSeries.bodyPart;
		String accessionNumber = dcm4CheeSeries.accessionNumber;
		String institution = dcm4CheeSeries.institution;
		String stationName = dcm4CheeSeries.stationName;
		String department = dcm4CheeSeries.department;
		int numberOfImages = dcm4CheeSeries.imagesInSeries;
		int numberOfSeriesRelatedInstances = dcm4CheeSeries.numberOfSeriesRelatedInstances;
		String firstImageUIDInSeries = (numberOfSeriesRelatedInstances != 1) ? "" : dcm4CheeDatabaseOperations
				.getFirstImageUIDInSeries(seriesUID);

		//int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForSeries(seriesUID, username);
		EPADAIMList aims = getSeriesAIMDescriptions(new SeriesReference(null, null, null, seriesUID), username, sessionID);
		int numberOfAnnotations = getNumberOfAccessibleAims(sessionID, suppliedProjectID, aims, username);
		List<EPADAIM> sharedAims = epadDatabaseOperations.getSharedAIMs(projectID, null, seriesUID);
		numberOfAnnotations = numberOfAnnotations + sharedAims.size();
		SeriesProcessingStatus seriesProcessingStatus = epadDatabaseOperations.getSeriesProcessingStatus(seriesUID);
		String createdTime = dcm4CheeSeries.createdTime != null ? dcm4CheeSeries.createdTime.toString() : "";


		AnnotationStatus annotationStatus = null;
		Map<String, AnnotationStatus> userStatusList = null;

		if (includeAnnotationStatus) {
			userStatusList = new HashMap<>();
			annotationStatus = getAnnotationStatusSeries(projectID, subjectID, studyUID, seriesUID, username, userStatusList);
			//set userStatusList to null so it doesn't display
			if (userStatusList.size()==0)
				userStatusList = null;
		}
		return new EPADSeries(projectID, subjectID, patientName, studyUID, seriesUID, seriesDate, seriesDescription,
				examType, bodyPart, accessionNumber, numberOfImages, numberOfSeriesRelatedInstances, numberOfAnnotations,
				institution, stationName, department, seriesProcessingStatus, createdTime, firstImageUIDInSeries, dcm4CheeSeries.isDSO, annotationStatus, userStatusList);
	}

	public AnnotationStatus getAnnotationStatusProject(String projectUID,
			String username, Map<String, AnnotationStatus> userStatusList, String sessionID, EPADSearchFilter searchFilter)
	{
		//check if the project has its own done status
		//if not check for each subject
		boolean isUserPrivileged = isUserPrivileged(projectUID, username);
		try {


			EPADSubjectList subjects= getSubjectDescriptions(projectUID, username, sessionID, searchFilter,1,5000, null,false,true);
			log.info("Number of subjects "+ subjects.ResultSet.totalRecords);
			//artem's fix for empty project
			if (subjects.ResultSet.totalRecords==0) 
				return AnnotationStatus.NOT_STARTED;
			int doneCount=0;
			int inProgressCount=0;
			//to calculate the cumulative user list,  we need a map of user to an array containing [donecount and in_progresscount]
			Map<String,int[]> statsMap= new HashMap<>();
			for (EPADSubject su: subjects.ResultSet.Result) {
				log.info("subject "+ su.subjectID);
				log.info("annotation status "+ su.annotationStatus);
				if (su.annotationStatus.equals(AnnotationStatus.DONE)) {
					doneCount++;
				}
				if (su.annotationStatus.equals(AnnotationStatus.IN_PROGRESS)) {
					inProgressCount++;
				}
				if (isUserPrivileged) {
					//cumulate the users' status over studies
					if (su.userStatusList!=null) {
						for (Entry<String, AnnotationStatus> e : su.userStatusList.entrySet()) {
							int[] value = statsMap.get(e.getKey());
							if (value==null) {
								value=new int[]{0,0};
							}

							if (e.getValue().equals(AnnotationStatus.DONE)) {
								value[0]++;

							}else if (e.getValue().equals(AnnotationStatus.IN_PROGRESS)) {
								value[1]++;

							}
							statsMap.put(e.getKey(), value);

						}
					}
					//fix for subjects with no studies
					else if (su.annotationStatus.equals(AnnotationStatus.DONE)) {
						for (Entry<String, int[]> e : statsMap.entrySet()) {
							int[] value = e.getValue();
							if (value==null) {
								value=new int[]{0,0};
							}

							value[0]++;
							statsMap.put(e.getKey(), value);

						}

					}
				}

			}
			//fill the user status list if priviliged
			if (isUserPrivileged) {
				for (Entry<String, int[]> e : statsMap.entrySet()) {
					int[] statForUser=e.getValue();
					//if done count equals to study count, user is done
					if (statForUser[0]==subjects.ResultSet.Result.size()) 
						userStatusList.put(e.getKey(), AnnotationStatus.DONE);
					else if (statForUser[0]+statForUser[1] >0)
						userStatusList.put(e.getKey(), AnnotationStatus.IN_PROGRESS);
					else userStatusList.put(e.getKey(), AnnotationStatus.NOT_STARTED);
				}

			}
			log.info("Subjects Done =" + doneCount + " in progress="+inProgressCount);
			if (subjects.ResultSet.Result.size()==doneCount) 
				return AnnotationStatus.DONE;
			else if (doneCount+inProgressCount >0 )
				return AnnotationStatus.IN_PROGRESS;



		} catch (Exception e) {
			log.info("Could not retrieve subjects for project "+ projectUID + " " + e.getMessage());
			return AnnotationStatus.ERROR;
		}
		return AnnotationStatus.NOT_STARTED;


	}

	public AnnotationStatus getAnnotationStatusSubject(String projectUID, String subjectUID,  
			String username, Map<String, AnnotationStatus> userStatusList, String sessionID, EPADSearchFilter searchFilter)
	{
		boolean isUserPrivileged = isUserPrivileged(projectUID, username);
		try {
			//to calculate the cumulative user list,  we need a map of user to an array containing [donecount and in_progresscount]
			Map<String,int[]> statsMap= new HashMap<>();

			if (isUserPrivileged) {
				//if the user is priviliged fill the user list
				EPADUserList users = getUserDescriptions(username, new ProjectReference(projectUID), sessionID);

				for (EPADUser u : users.ResultSet.Result) {
					int[] value=new int[]{0,0};
					statsMap.put(u.username, value);

				}

			}
			//			else {
			//				AnnotationStatus as=projectOperations.getAnnotationStatusForUserBySubject(projectUID, subjectUID, username);
			//				if (as!=AnnotationStatus.ERROR)	 
			//					return as;
			//			}

			EPADStudyList studies= getStudyDescriptions(new SubjectReference(projectUID, subjectUID), username, sessionID, searchFilter,true);
			int doneCount=0;
			int inProgressCount=0;
			if (studies.ResultSet.totalRecords==0)
				return AnnotationStatus.NOT_STARTED;
			for (EPADStudy st: studies.ResultSet.Result) {
				log.info("study "+ st.studyUID);
				log.info("annotation status "+ st.annotationStatus);

				if (isUserPrivileged) {
					//cumulate the users' status over studies
					if (st.userStatusList!=null) {
						for (Entry<String, AnnotationStatus> e : st.userStatusList.entrySet()) {
							int[] value = statsMap.get(e.getKey());
							if (value==null) {
								value=new int[]{0,0};
							}
							if (e.getValue().equals(AnnotationStatus.DONE)) {
								value[0]++;

							}else if (e.getValue().equals(AnnotationStatus.IN_PROGRESS)) {
								value[1]++;

							}
							statsMap.put(e.getKey(), value);

						}
					}

					//fix for studies with no series
					else if (st.annotationStatus.equals(AnnotationStatus.NOT_STARTED)) {
						for (Entry<String, int[]> e : statsMap.entrySet()) {
							int[] value = e.getValue();
							if (value==null) {
								value=new int[]{0,0};
							}

							value[0]++;
							statsMap.put(e.getKey(), value);

						}

					}
				} else {
					if (st.annotationStatus.equals(AnnotationStatus.DONE)) {
						doneCount++;
					}
					if (st.annotationStatus.equals(AnnotationStatus.IN_PROGRESS)) {
						inProgressCount++;
					}
				}

			}


			//cumulate studies		
			if (isUserPrivileged) {
				for (Entry<String, int[]> e : statsMap.entrySet()) {
					int[] statForUser=e.getValue();
					//if done count equals to study count, user is done
					if (statForUser[0]==studies.ResultSet.Result.size()) 
						userStatusList.put(e.getKey(), AnnotationStatus.DONE);
					else if (statForUser[0]+statForUser[1] >0)
						userStatusList.put(e.getKey(), AnnotationStatus.IN_PROGRESS);
					else userStatusList.put(e.getKey(), AnnotationStatus.NOT_STARTED);
				}
				//				//check subject status table and override the cumulated from studies
				//				for (Entry<String, AnnotationStatus> e : userStatusList.entrySet()) {
				//					AnnotationStatus as=projectOperations.getAnnotationStatusForUserBySubject(projectUID, subjectUID, e.getKey());
				//					if (as!=AnnotationStatus.ERROR)	 
				//						e.setValue(as);
				//					log.info("subject set "+ subjectUID + "  "+ as);
				//				}
				//				
				//check the user list to cumulate
				for (Entry<String, AnnotationStatus> e : userStatusList.entrySet()) {
					if (e.getValue().equals(AnnotationStatus.DONE)) 
						doneCount++;
					if (e.getValue().equals(AnnotationStatus.IN_PROGRESS))
						inProgressCount++;
				}
				if (userStatusList.size() == 0)
					return AnnotationStatus.NOT_STARTED;
				else if (userStatusList.size()==doneCount) 
					return AnnotationStatus.DONE;
				else if (doneCount+inProgressCount >0 )
					return AnnotationStatus.IN_PROGRESS;

			}else {

				if (studies.ResultSet.Result.size()==doneCount) 
					return AnnotationStatus.DONE;
				else if (doneCount+inProgressCount >0 )
					return AnnotationStatus.IN_PROGRESS;
			}


		} catch (Exception e) {
			log.info("Could not retrieve studies for subject "+ subjectUID + " " + e.getMessage());
			return AnnotationStatus.ERROR;
		}
		return AnnotationStatus.NOT_STARTED;
	}

	public AnnotationStatus getAnnotationStatusStudy(String projectUID, String subjectUID, String studyUID, 
			String username, Map<String, AnnotationStatus> userStatusList, int numberOfSeries)
	{
		//check if the study has its own done status
		//if not check series
		return getAnnotationStatus(projectUID, subjectUID, studyUID, null, username, userStatusList, numberOfSeries, isUserPrivileged(projectUID,username));

	}

	public AnnotationStatus getAnnotationStatusSeries(String projectUID, String subjectUID, String studyUID, String series_uid,
			String username, Map<String, AnnotationStatus> userStatusList) {
		return getAnnotationStatus(projectUID, subjectUID, studyUID, series_uid, username, userStatusList, 1, isUserPrivileged(projectUID,username));
	}

	public boolean isUserPrivileged(String projectUID, String username) {
		User user=null;
		try {
			user=projectOperations.getUser(username);
		} catch (Exception e) {
			log.info("User couldn't be retrieved for username "+username+ " " +e.getMessage());
			return false;
		}
		boolean isOwner=false;
		try {
			isOwner=projectOperations.isOwner(username, projectUID);
		} catch (Exception e) {
			log.info("Is owner status couldn't be checked for username "+username + " and project "+projectUID + " " +e.getMessage());
			return false;
		}
		//if the user os admin or the owner of the project, get cumulative. 
		if (username.equals("admin") || (user!=null && (user.isAdmin() || isOwner))) {
			return true;
		}
		return false;
	}


	public AnnotationStatus getAnnotationStatus(String projectUID, String subjectUID, String studyUID, String series_uid,
			String username, Map<String, AnnotationStatus> userStatusList, int numberOfSeries, boolean isUserPrivileged)
	{
		//if the user is admin or the owner of the project, get cumulative. 
		if (isUserPrivileged) {
			fillAnnotationStatusList(projectUID, subjectUID, studyUID, series_uid, username, userStatusList, numberOfSeries);
			//no series fix, do not bother calculating
			if (numberOfSeries==0) 
				return AnnotationStatus.NOT_STARTED;

			//go through the user list to cumulate
			int doneCount=0;
			int inProgressCount=0;
			for (Entry<String, AnnotationStatus> e : userStatusList.entrySet()) {
				if (e.getValue().equals(AnnotationStatus.DONE)) 
					doneCount++;
				if (e.getValue().equals(AnnotationStatus.IN_PROGRESS))
					inProgressCount++;
			}
			if (userStatusList.size() == 0) 
				return AnnotationStatus.NOT_STARTED;
			else if(userStatusList.size()==doneCount) 
				return AnnotationStatus.DONE;
			else if (doneCount+inProgressCount >0 )
				return AnnotationStatus.IN_PROGRESS;

			//use annotation and user counts to decide
			//			long userCount=0;
			//			int annotationDoneUserCount;

			//			try {
			//				userCount=projectOperations.getUserCountProject(projectUID);
			//				annotationDoneUserCount = projectOperations.getAnnotationStatusUserCount(projectUID, subjectUID, studyUID, series_uid,AnnotationStatus.DONE);
			//			}catch (Exception e) {
			//				log.info("User count couldn't be retrieved for project "+projectUID+ " " +e.getMessage());
			//				return AnnotationStatus.NOT_STARTED;
			//			}
			//			if (annotationDoneUserCount == 0) {
			//				int inProgressCount = projectOperations.getAnnotationStatusUserCount(projectUID, subjectUID, studyUID, series_uid,AnnotationStatus.IN_PROGRESS);
			//				if (inProgressCount == 0)
			//					return AnnotationStatus.NOT_STARTED;
			//				else
			//					return AnnotationStatus.IN_PROGRESS;
			//			}
			//			else if (userCount*numberOfSeries == annotationDoneUserCount) 
			//				return AnnotationStatus.DONE;
			//			else if (userCount*numberOfSeries > annotationDoneUserCount) 
			//				return AnnotationStatus.IN_PROGRESS;

			return AnnotationStatus.NOT_STARTED;
		}

		//Else get his own status
		return projectOperations.getAnnotationStatusForUser(projectUID, subjectUID, studyUID, series_uid, username, numberOfSeries);


	}


	public void fillAnnotationStatusList(String projectUID, String subjectUID, String studyUID, String series_uid,
			String username, Map<String, AnnotationStatus> userStatusList, int numberOfSeries)
	{
		try {
			List<User> users = projectOperations.getUsersForProject(projectUID);
			for (User u: users) {
				log.info("putting user "+u.getUsername() + " status "+ projectOperations.getAnnotationStatusForUser(projectUID, subjectUID, studyUID, series_uid, u.getUsername(), numberOfSeries));

				userStatusList.put(u.getUsername(), projectOperations.getAnnotationStatusForUser(projectUID, subjectUID, studyUID, series_uid, u.getUsername(), numberOfSeries));
			}
		} catch (Exception e1) {
			log.info("Couldn't get users for the project "+projectUID + e1.getMessage());
		}


	}


	private static String trimTrailing(String xnatName)
	{
		while (xnatName.endsWith("^"))
			xnatName = xnatName.substring(0, xnatName.length()-1);
		String name = xnatName.trim();
		return name;
	}

	private StudyProcessingStatus getStudyProcessingStatus(String studyUID)
	{
		boolean seriesNotStarted = false;
		boolean seriesWithNoDICOM = false;
		boolean seriesInPipeline = false;
		boolean seriesWithError = false;

		Set<String> seriesUIDs = dcm4CheeDatabaseOperations.getAllSeriesUIDsInStudy(studyUID);

		for (String seriesUID : seriesUIDs) {
			SeriesProcessingStatus seriesProcessingStatus = epadDatabaseOperations.getSeriesProcessingStatus(seriesUID);
			if (seriesProcessingStatus == null)
				seriesNotStarted = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.NO_DICOM)
				seriesWithNoDICOM = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.ERROR)
				seriesWithError = true;
			if (seriesProcessingStatus == SeriesProcessingStatus.IN_PIPELINE)
				seriesInPipeline = true;
		}

		if (seriesNotStarted)
			return StudyProcessingStatus.STUDY_STATUS_NOT_STARTED;
		if (seriesWithError)
			return StudyProcessingStatus.STUDY_STATUS_ERROR_MISSING_PNG;
		else if (seriesWithNoDICOM)
			return StudyProcessingStatus.STUDY_STATUS_ERROR_MISSING_DICOM;
		else if (seriesInPipeline)
			return StudyProcessingStatus.STUDY_STATUS_PROCESSING;
		else
			return StudyProcessingStatus.STUDY_STATUS_COMPLETED;
	}

	private EPADProject xnatProject2EPADProject(String sessionID, String username, XNATProject xnatProject,
			EPADSearchFilter searchFilter, boolean annotationCount)
	{
		String projectName = xnatProject.name;
		if (!searchFilter.shouldFilterProject(projectName)) {
			String projectID = xnatProject.ID;

			String secondaryID = xnatProject.secondary_ID;
			String piLastName = xnatProject.pi_lastname;
			String description = xnatProject.description;
			String piFirstName = xnatProject.pi_firstname;
			String uri = xnatProject.URI;
			Set<String> patientIDs = XNATQueries.getSubjectIDsForProject(sessionID, projectID);
			int numberOfPatients = patientIDs.size();
			//			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatients(sessionID, username, patientIDs);
			Set<String> studyUIDs =XNATQueries.getAllStudyUIDsForProject(projectID, sessionID);
			int numberOfAnnotations = 0;
			if (annotationCount)
			{
				for  (String studyUID: studyUIDs)
				{
					//ml for speeding up annotation count
					numberOfAnnotations = getStudyAimCount(sessionID, studyUID.replace('_', '.'),projectID,username);
					//					EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(null, null, studyUID.replace('_', '.')), username, sessionID);
					//					numberOfAnnotations = numberOfAnnotations + getNumberOfAccessibleAims(sessionID, projectID, aims, username);
				}
			}
			if (!searchFilter.shouldFilterProject(projectName, numberOfAnnotations)) {
				int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatients(patientIDs);
				XNATUserList xnatUsers = XNATQueries.getUsersForProject(projectID);
				Set<String> usernames = xnatUsers.getLoginNames();
				Map<String,String> userRoles = xnatUsers.getRoles();
				if (!userRoles.keySet().contains(username))
					userRoles.put(username, "Collaborator");
				return new EPADProject(secondaryID, piLastName, description, projectName, projectID, piFirstName, uri,
						numberOfPatients, numberOfStudies, numberOfAnnotations, patientIDs, usernames, userRoles);
			} else
				return null;
		} else
			return null;
	}

	private EPADProject project2EPADProject(String sessionID, String username, Project project,
			EPADSearchFilter searchFilter, boolean annotationCount) throws Exception
	{
		return project2EPADProject(sessionID, username, project, searchFilter, annotationCount, false);
	}
	private EPADProject project2EPADProject(String sessionID, String username, Project project,
			EPADSearchFilter searchFilter, boolean annotationCount, boolean includeAnnotationStatus) throws Exception
	{
		if (project == null) return null;
		String projectName = project.getName();
		if (!searchFilter.shouldFilterProject(projectName)) {
			String projectID = project.getProjectId();
			String description = project.getDescription();
			Set<String> patientIDs = new HashSet<String>();
			long starttime = System.currentTimeMillis();
			List<Subject> subjects = projectOperations.getSubjectsForProject(projectID);
			for (Subject subject: subjects)
				patientIDs.add(subject.getSubjectUID());
			long subjecttime = System.currentTimeMillis();
			int numberOfPatients = patientIDs.size();
			if (project.getProjectId().equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned"))) {
				numberOfPatients = projectOperations.getUnassignSubjects().size();
			}
			int numberOfStudies = 0;
			int numberOfAnnotations = 0;
			long studytime = System.currentTimeMillis();
			if (annotationCount && subjects.size() < 300)
			{
				Set<String> studyUIDs = new HashSet<String>();
				List<Study> studies = projectOperations.getAllStudiesForProject(projectID);
				for (Study study: studies)
					studyUIDs.add(study.getStudyUID());
				studytime = System.currentTimeMillis();
				numberOfStudies = studies.size();
				for  (String studyUID: studyUIDs)
				{
					//ml for speeding up annotation count
					numberOfAnnotations += getStudyAimCount(sessionID,studyUID,projectID,username);
					//					EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(null, null, studyUID), username, sessionID);
					//					numberOfAnnotations = numberOfAnnotations + getNumberOfAccessibleAims(sessionID, projectID, aims, username);
				}
				List<EPADAIM> sharedAims = epadDatabaseOperations.getSharedAIMs(projectID, null, null);
				numberOfAnnotations = numberOfAnnotations + sharedAims.size();
			}
			long aimtime = System.currentTimeMillis();
			if (!searchFilter.shouldFilterProject(projectName, numberOfAnnotations)) {
				List<User> users = projectOperations.getUsersForProject(projectID);
				Set<String> usernames = new HashSet<String>();
				for (User user: users)
					usernames.add(user.getUsername());
				long usertime = System.currentTimeMillis();
				Map<String,String> userRoles = new HashMap<String, String>(); // TODO
				//Map<String,String> userRoles = xnatUsers.getRoles();
				//if (!userRoles.keySet().contains(username))
				//	userRoles.put(username, "Collaborator");

				//log.info("Time for conv, subj:" + (subjecttime-starttime) + ", study:" + (studytime-subjecttime) + " aim:" + (aimtime-studytime) + " user:" + (usertime-aimtime) + " msecs");

				AnnotationStatus annotationStatus = null;
				Map<String, AnnotationStatus> userStatusList = null;
				if (includeAnnotationStatus) {
					userStatusList = new HashMap<>();
					annotationStatus = getAnnotationStatusProject(projectID, username, userStatusList, sessionID, searchFilter);
					//set userStatusList to null so it doesn't display
					if (userStatusList.size()==0)
						userStatusList = null;
				}

				EPADProject ep = new EPADProject("", "", description, projectName, projectID, "", "",
						numberOfPatients, numberOfStudies, numberOfAnnotations, patientIDs, usernames, userRoles, annotationStatus, userStatusList, project.getType());
				ep.defaultTemplate = project.getDefaultTemplate();
				return ep;
			} else
				return null;
		} else
			return null;
	}

	public int getStudyAimCount(String sessionID,String studyUID,String projectID,String username) {
		//		EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(null, null, studyUID), username, sessionID);
		//		return getNumberOfAccessibleAims(sessionID, projectID, aims, username);
		return epadDatabaseOperations.getAIMCount(projectID, studyUID, username);

	}

	private int getNumberOfAccessibleAims(String sessionID, String suppliedProjectID, EPADAIMList aimlist, String username)
	{
		Set<String> projectIDs = aimlist.getProjectIds();
		int count = 0;
		for (String projectID: projectIDs)
		{
			//if (!suppliedProjectID.equals(projectID) && !projectID.equals(EPADConfig.xnatUploadProjectID) && !projectID.equals("")) continue;
			if (suppliedProjectID == null || !suppliedProjectID.equals(projectID)) continue;
			try
			{
				
				boolean isCollaborator = UserProjectService.isCollaborator(sessionID, username, projectID);
				Set<EPADAIM> aims = aimlist.getAIMsForProject(projectID);
				for (EPADAIM aim: aims)
				{
					if (!isCollaborator || aim.userName.equalsIgnoreCase(username) || aim.userName.equalsIgnoreCase("shared"))
					{
						count++;
					}
				}
			}
			catch (Exception x) {}
		}

		return count;
	}

	private EPADSubject xnatSubject2EPADSubject(String sessionID, String username, XNATSubject xnatSubject,
			EPADSearchFilter searchFilter)
	{
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();

		String patientID = xnatSubject.label;
		String patientName = xnatSubject.src;
		if (!searchFilter.shouldFilterSubject(patientID, patientName)) {
			String projectID = xnatSubject.project;
			String xnatSubjectID = xnatSubject.ID;
			String uri = xnatSubject.URI;
			String insertUser = xnatSubject.insert_user;
			String insertDate = xnatSubject.insert_date;
			//			int numberOfAnnotations = AIMQueries.getNumberOfAIMAnnotationsForPatient(patientID, username);
			//Set<String> studyUIDs = XNATQueries.getStudyUIDsForSubject(sessionID, projectID, xnatSubjectID);
			int numberOfAnnotations = 0;
			//for  (String studyUID: studyUIDs)
			{
				// Skip this, cause it is too slow and not that important
				//EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(null, null, studyUID), username, sessionID);
				//numberOfAnnotations = numberOfAnnotations + getNumberOfAccessibleAims(sessionID, aims, username);
			}
			if (!searchFilter.shouldFilterSubject(patientID, patientName, numberOfAnnotations)) {
				Set<String> examTypes = epadQueries.getExamTypesForSubject(patientID);
				if (!searchFilter.shouldFilterSubject(patientID, patientName, examTypes, numberOfAnnotations)) {
					int numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatient(patientID);

					return new EPADSubject(projectID, patientID, patientName, insertUser, xnatSubjectID, insertDate, uri,
							numberOfStudies, numberOfAnnotations, examTypes);
				} else
					return null;
			} else
				return null;
		} else
			return null;
	}

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private String formatDate(Date date)
	{
		if (date == null)
			return "";
		else
			return dateFormat.format(date);
	}

	static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	private String formatDateTime(Date date)
	{
		if (date == null)
			return "";
		else
			return dateTimeFormat.format(date);
	}

	private EPADSubject subject2EPADSubject(String sessionID, String username, Subject subject, String projectID,
			EPADSearchFilter searchFilter, boolean annotationCount) throws Exception
	{
		return subject2EPADSubject(sessionID, username, subject, projectID, searchFilter, annotationCount, false);
	}
	private EPADSubject subject2EPADSubject(String sessionID, String username, Subject subject, String projectID,
			EPADSearchFilter searchFilter, boolean annotationCount, boolean includeAnnotationStatus) throws Exception
	{
		EpadOperations epadQueries = DefaultEpadOperations.getInstance();

		String patientID = subject.getSubjectUID();
		String displayID = subject.getDisplayUID();
		String patientName = subject.getName();
		int numberOfStudies = 0;
		if (!searchFilter.shouldFilterSubject(patientID, patientName)) {
			String xnatSubjectID = "";
			String uri = "";
			String insertUser = subject.getCreator();
			String insertDate = dateFormat.format(subject.getCreatedTime());

			int numberOfAnnotations = 0;
			if (annotationCount && !"true".equalsIgnoreCase(EPADConfig.getParamValue("SkipPatientAnnotationCount", "false"))) {
				List<Study> studies = null;
				//ml fix for downloading multiple subjects
				if (projectID==null) {
					studies=projectOperations.getStudiesForSubject(patientID);
				} else {
					studies=projectOperations.getStudiesForProjectAndSubject(projectID, patientID);
				}
				numberOfStudies = studies.size();
				for  (Study study: studies)
				{
					//ml
					numberOfAnnotations += getStudyAimCount(sessionID,study.getStudyUID(),projectID,username);
					// Skip this, cause it is too slow and not that important
					//					EPADAIMList aims = getStudyAIMDescriptions(new StudyReference(null, null, study.getStudyUID()), username, sessionID);
					//					numberOfAnnotations = numberOfAnnotations + getNumberOfAccessibleAims(sessionID, projectID, aims, username);
				}
				List<EPADAIM> sharedAims = epadDatabaseOperations.getSharedAIMs(projectID, patientID, null);
				numberOfAnnotations = numberOfAnnotations + sharedAims.size();
			}
			if (!searchFilter.shouldFilterSubject(patientID, patientName, numberOfAnnotations)) {
				Set<String> examTypes = epadQueries.getExamTypesForSubject(patientID);
				if (!searchFilter.shouldFilterSubject(patientID, patientName, examTypes, numberOfAnnotations)) {
					if (numberOfStudies == 0)
						numberOfStudies = Dcm4CheeQueries.getNumberOfStudiesForPatient(patientID);

					AnnotationStatus annotationStatus = null;
					Map<String, AnnotationStatus> userStatusList = null;
					if (includeAnnotationStatus) {
						userStatusList = new HashMap<>();
						annotationStatus = getAnnotationStatusSubject(projectID, patientID, username, userStatusList, sessionID, searchFilter);
						//set userStatusList to null so it doesn't display
						if (userStatusList.size()==0)
							userStatusList = null;
					}
					return new EPADSubject(projectID, patientID, patientName, insertUser, xnatSubjectID, insertDate, uri,
							numberOfStudies, numberOfAnnotations, examTypes, annotationStatus, userStatusList, displayID);
				} else
					return null;
			} else
				return null;
		} else
			return null;
	}

	private String getPNGPath(String studyUID, String seriesUID, String imageUID)
	{ // TODO Look at this. Not very robust.
		String pngLocation = epadDatabaseOperations.getPNGLocation(studyUID, seriesUID, imageUID);
		if (pngLocation == null)
			return null;
		String pngPath = pngLocation.substring(EPADConfig.getEPADWebServerPNGDir().length());

		return pngPath;
	}

	private String getPNGMaskPath(String studyUID, String seriesUID, String imageUID, int frameNumber)
	{
		return "studies/" + studyUID + "/series/" + seriesUID + "/images/" + imageUID + "/masks/" + frameNumber + ".png";
	}

	private String getPNGMaskPath(String studyUID, String seriesUID, String imageUID, int frameNumber, String segmentNumber)
	{
		return "studies/" + studyUID + "/series/" + seriesUID + "/images/" + imageUID + "/masks/" + frameNumber + "_" + segmentNumber + ".png";
	}

	private String getPNGContourPath(String studyUID, String seriesUID, String imageUID, int frameNumber)
	{
		return "studies/" + studyUID + "/series/" + seriesUID + "/images/" + imageUID + "/contours/" + frameNumber + ".png";
	}

	private String getWADOPath(String studyUID, String seriesUID, String imageUID)
	{
		return "?requestType=WADO&studyUID=" + studyUID + "&seriesUID=" + seriesUID + "&objectUID=" + imageUID;
	}

	private EPADImage createEPADImage(SeriesReference seriesReference, DCM4CHEEImageDescription dcm4cheeImageDescription,
			DICOMElementList dicomElements, DICOMElementList defaultDICOMElements)
	{
		return createEPADImage(seriesReference.projectID, seriesReference.subjectID, seriesReference.studyUID,
				seriesReference.seriesUID, dcm4cheeImageDescription.imageUID, dcm4cheeImageDescription, dicomElements,
				defaultDICOMElements);
	}

	private EPADImage createEPADImage(ImageReference imageReference, DCM4CHEEImageDescription dcm4CheeImageDescription,
			DICOMElementList dicomElements, DICOMElementList defaultDICOMElements)
	{
		return createEPADImage(imageReference.projectID, imageReference.subjectID, imageReference.studyUID,
				imageReference.seriesUID, imageReference.imageUID, dcm4CheeImageDescription, dicomElements,
				defaultDICOMElements);
	}

	private EPADImage createEPADImage(String projectID, String subjectID, String studyUID, String seriesUID,
			String imageUID, DCM4CHEEImageDescription dcm4cheeImageDescription, DICOMElementList dicomElements,
			DICOMElementList defaultDICOMElements)
	{
		String classUID = dcm4cheeImageDescription.classUID;
		int instanceNumber = dcm4cheeImageDescription.instanceNumber;
		String sliceLocation = dcm4cheeImageDescription.sliceLocation;
		String imageDate = dcm4cheeImageDescription.contentTime;
		String insertDate = dcm4cheeImageDescription.createdTime;
		String rescaleIntercept = dcm4cheeImageDescription.rescaleIntercept;
		String rescaleSlope = dcm4cheeImageDescription.rescaleSlope;
		int numberOfFrames = getNumberOfFrames(imageUID, defaultDICOMElements);
		boolean isDSO = isDSO(dcm4cheeImageDescription);
		String losslessImage = getPNGPath(studyUID, seriesUID, imageUID);
		if (losslessImage == null)
		{
			//String dicomFilePath = getDICOMFilePath(dcm4cheeImageDescription.);
			//File inputDICOMFile = new File(dicomFilePath);
			//if (!isDSO)
		}
		String lossyImage = getWADOPath(studyUID, seriesUID, imageUID);
		//		log.info("rescale slope:"+rescaleSlope+" and intercept:"+rescaleIntercept);
		if (rescaleIntercept==null && rescaleSlope==null) {
			log.info("rescale slope and intercept empty!");
			if (dicomElements!=null){ //the first image, try dicom elements
				log.info("using dicomelements");
				for (int i=0;i< dicomElements.ResultSet.totalRecords; i++) {
					if (dicomElements.ResultSet.Result.get(i).tagCode.equals("(0028,1052)")) {
						rescaleIntercept = dicomElements.ResultSet.Result.get(i).value.trim();
					}
					if (dicomElements.ResultSet.Result.get(i).tagCode.equals("(0028,1053)")) {
						rescaleSlope = dicomElements.ResultSet.Result.get(i).value.trim();
					}
				}
				log.info("rescale slope:"+rescaleSlope+" and intercept:"+rescaleIntercept);
			}
			if (rescaleIntercept==null && rescaleSlope==null) { //still empty, query
				log.info("rescale slope and intercept still empty!");
				DICOMElementList dicomTags= getDICOMElements(studyUID, seriesUID, imageUID);
				for (int i=0;i< dicomTags.ResultSet.totalRecords; i++) {
					if (dicomTags.ResultSet.Result.get(i).tagCode.equals("(0028,1052)")) {
						rescaleIntercept = dicomTags.ResultSet.Result.get(i).value.trim();
					}
					if (dicomTags.ResultSet.Result.get(i).tagCode.equals("(0028,1053)")) {
						rescaleSlope = dicomTags.ResultSet.Result.get(i).value.trim();
					}
				}
				log.info("rescale slope:"+rescaleSlope+" and intercept:"+rescaleIntercept);
			}

		}
		
		

		//log.debug("losslessimage:" + losslessImage);
		return new EPADImage(projectID, subjectID, studyUID, seriesUID, imageUID, classUID, insertDate, imageDate,
				sliceLocation, instanceNumber, losslessImage, lossyImage, dicomElements, defaultDICOMElements, numberOfFrames,
				isDSO, rescaleIntercept, rescaleSlope);
	}

	private int getNumberOfFrames(String imageUID, DICOMElementList dicomElements)
	{
		for (DICOMElement dicomElement : dicomElements.ResultSet.Result) {
			if (dicomElement.tagCode.equalsIgnoreCase(PixelMedUtils.NumberOfFramesCode)) {
				try {
					return Integer.parseInt(dicomElement.value);
				} catch (NumberFormatException e) {
					log.warning("Invalid number of frames value " + dicomElement.value + " for image " + imageUID);
					return 0;
				}
			}
		}
		//log.warning("Could not find number of frames value  in DICOM headers for image " + imageUID);
		return 0;
	}

	private int getNumberOfSegments(DICOMElementList dicomElements)
	{
		int segments = 0;
		for (DICOMElement dicomElement : dicomElements.ResultSet.Result) {
			if (dicomElement.tagCode.equalsIgnoreCase(PixelMedUtils.SegmentNumberCode)) {
				segments++;
			}
		}
		return segments;
	}

	private DICOMElementList getDICOMElements(ImageReference imageReference)
	{
		return getDICOMElements(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID);
	}
	private DICOMElementList getDICOMElements(ImageReference imageReference, SegmentedProperty catTypeProp)
	{
		return getDICOMElements(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID, catTypeProp);
	}

	@Override
	public DICOMElementList getDICOMElements(String studyUID, String seriesUID, String imageUID)
	{
		DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID);

		if (dicomElementList == null)
			log.warning("Could not get DICOM header for image " + imageUID + " in series " + seriesUID);

		return dicomElementList;
	}
	
	private DICOMElementList getDICOMElements(String studyUID, String seriesUID, String imageUID, SegmentedProperty catTypeProp)
	{
		DICOMElementList dicomElementList = Dcm4CheeQueries.getDICOMElementsFromWADO(studyUID, seriesUID, imageUID, catTypeProp);

		if (dicomElementList == null)
			log.warning("Could not get DICOM header for image " + imageUID + " in series " + seriesUID);

		return dicomElementList;
	}

	private DICOMElementList getDefaultDICOMElements(ImageReference imageReference, DICOMElementList suppliedDICOMElements)
	{
		return getDefaultDICOMElements(imageReference.studyUID, imageReference.seriesUID, imageReference.imageUID,
				suppliedDICOMElements, 0);
	}

	@Override
	public String getDICOMElement(DICOMElementList dicomElements, String tagName)
	{
		List<DICOMElement> defaultDicomElements = new ArrayList<>();
		Map<String, List<DICOMElement>> dicomElementMap = generateDICOMElementMap(dicomElements);
		if (dicomElementMap.containsKey(tagName))
			return dicomElementMap.get(tagName).get(0).value;
		else
			return null;
	}

	private DICOMElementList getDefaultDICOMElements(String studyUID, String seriesUID, String imageUID,
			DICOMElementList suppliedDicomElements, int instanceNo)
	{
		return getDefaultDICOMElements(studyUID, seriesUID, imageUID, suppliedDicomElements, instanceNo, false);
	}

	private DICOMElementList getDefaultDICOMElements(String studyUID, String seriesUID, String imageUID,
			DICOMElementList suppliedDicomElements, int instanceNo, boolean useMax)
	{
		String override = epadDatabaseOperations.getSeriesDefaultTags(seriesUID);
		if (override == null) override = "";
		String[] tags = override.split(";");
		String modality = "";
		String bodyPart = "";
		Map<String, String> overriddenTags = new HashMap<String, String>();
		for (String tag: tags)
		{
			String[] tagValue = tag.split("=");
			if (tagValue.length != 2) continue;
			String insNo = null;
			if (tagValue[0].contains(","))
			{
				insNo = tagValue[0].substring(tagValue[0].indexOf(",")+1);
				tagValue[0] = tagValue[0].substring(0, tagValue[0].indexOf(","));
			}
			if (insNo == null || insNo.trim().equals(String.valueOf(instanceNo)))
				overriddenTags.put(tagValue[0].trim(), tagValue[1].trim());
		}
		List<DICOMElement> defaultDicomElements = new ArrayList<>();
		Map<String, List<DICOMElement>> suppliedDICOMElementMap = generateDICOMElementMap(suppliedDicomElements);

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientNameCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientNameCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientNameCode, PixelMedUtils.PatientNameTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ModalityCode))
		{
			modality = suppliedDICOMElementMap.get(PixelMedUtils.ModalityCode).get(0).value;
			if (modality.equals("US"))
				useMax = true;
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ModalityCode).get(0));
		}
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ModalityCode, PixelMedUtils.ModalityTagName, ""));
		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.BodyPartExamined))
		{
			bodyPart = suppliedDICOMElementMap.get(PixelMedUtils.BodyPartExamined).get(0).value;
		}

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SeriesDescriptionCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SeriesDescriptionCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SeriesDescriptionCode,
					PixelMedUtils.SeriesDescriptionTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientBirthDateCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientBirthDateCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientBirthDateCode,
					PixelMedUtils.PatientBirthDateTagName, "1900-01-01T00:00:00"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SliceThicknessCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SliceThicknessCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceThicknessCode, PixelMedUtils.SliceThicknessTagName,
					"0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SliceLocationCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SliceLocationCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceLocationCode, PixelMedUtils.SliceLocationTagName,
					"0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PatientSexCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PatientSexCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PatientSexCode, PixelMedUtils.PatientSexTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ManufacturerCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ManufacturerCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ManufacturerCode, PixelMedUtils.ManufacturerTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ModelNameCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ModelNameCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ModelNameCode, PixelMedUtils.ModelNameTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.SoftwareVersionCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.SoftwareVersionCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.SoftwareVersionCode,
					PixelMedUtils.SoftwareVersionTagName, ""));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PixelSpacingCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PixelSpacingCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelSpacingCode, PixelMedUtils.PixelSpacingTagName,
					"1\\1"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RescaleInterceptCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RescaleInterceptCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.RescaleInterceptCode,
					PixelMedUtils.RescaleInterceptTagName, "0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RescaleSlopeCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RescaleSlopeCode).get(0));
		else
			defaultDicomElements
			.add(new DICOMElement(PixelMedUtils.RescaleSlopeCode, PixelMedUtils.RescaleSlopeTagName, "1"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.StudyDateCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.StudyDateCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.StudyDateCode, PixelMedUtils.StudyDateTagName,
					"1900-01-01T00:00:00"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.StudyTimeCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.StudyTimeCode).get(0));
		else
			defaultDicomElements
			.add(new DICOMElement(PixelMedUtils.StudyTimeCode, PixelMedUtils.StudyTimeTagName, "00:00:00"));

		String rows = "512";
		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.RowsCode))
		{
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RowsCode).get(0));
			//if (suppliedDICOMElementMap.get(PixelMedUtils.RowsCode).size() > 1)
			//	defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.RowsCode).get(1));
			rows = suppliedDICOMElementMap.get(PixelMedUtils.RowsCode).get(0).value;
		}
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.RowsCode, PixelMedUtils.RowsTagName, "512"));

		String cols = "512";
		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.ColumnsCode))
		{
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ColumnsCode).get(0));
			//if (suppliedDICOMElementMap.get(PixelMedUtils.ColumnsCode).size() > 1)
			//	defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.ColumnsCode).get(1));
			cols = suppliedDICOMElementMap.get(PixelMedUtils.ColumnsCode).get(0).value;
		}
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.ColumnsCode, PixelMedUtils.ColumnsTagName, "512"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.BitsStoredCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.BitsStoredCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.BitsStoredCode, PixelMedUtils.BitsStoredTagName, "16"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.PixelRepresentationCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.PixelRepresentationCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelRepresentationCode,
					PixelMedUtils.PixelRepresentationTagName, "0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.NumberOfFramesCode))
			defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.NumberOfFramesCode).get(0));
		else
			defaultDicomElements.add(new DICOMElement(PixelMedUtils.NumberOfFramesCode, PixelMedUtils.NumberOfFramesTagName,
					"0"));

		if (suppliedDICOMElementMap.containsKey(PixelMedUtils.WindowWidthCode)
				&& suppliedDICOMElementMap.containsKey(PixelMedUtils.WindowCenterCode)) {
			if ("0".equals(suppliedDICOMElementMap.get(PixelMedUtils.WindowWidthCode).get(0).value))
			{
				if (overriddenTags.containsKey(PixelMedUtils.WindowWidthTagName))
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowWidthCode, PixelMedUtils.WindowWidthTagName, overriddenTags.get(PixelMedUtils.WindowWidthTagName)));
				if (overriddenTags.containsKey(PixelMedUtils.WindowCenterTagName))
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowCenterCode, PixelMedUtils.WindowCenterTagName, overriddenTags.get(PixelMedUtils.WindowCenterTagName)));
				else
					defaultDicomElements.addAll(getCalculatedWindowingDICOMElements(studyUID, seriesUID, imageUID, useMax, modality, bodyPart));
			}
			else
			{
				defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.WindowWidthCode).get(0));
				defaultDicomElements.add(suppliedDICOMElementMap.get(PixelMedUtils.WindowCenterCode).get(0));
			}
		} else {
			if (overriddenTags.containsKey(PixelMedUtils.WindowWidthTagName))
				defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowWidthCode, PixelMedUtils.WindowWidthTagName, overriddenTags.get(PixelMedUtils.WindowWidthTagName)));
			if (overriddenTags.containsKey(PixelMedUtils.WindowCenterTagName))
				defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowCenterCode, PixelMedUtils.WindowCenterTagName, overriddenTags.get(PixelMedUtils.WindowCenterTagName)));
			else
				defaultDicomElements.addAll(getCalculatedWindowingDICOMElements(studyUID, seriesUID, imageUID, useMax, modality, bodyPart));
		}
		return new DICOMElementList(defaultDicomElements);
	}

	private DICOMElementList replaceSliceSpecificElements(String studyUID, String seriesUID, String imageUID,
			DICOMElementList suppliedDicomElements)
	{
		List<DICOMElement> defaultDicomElements = new ArrayList<>();
		File tagFile = new File(EPADConfig.getEPADWebServerDicomTagDir() + getPNGPath(studyUID, seriesUID, imageUID).replace(".png",".tag"));
		if (!tagFile.exists()) {
			log.info("No tag file found:" + tagFile.getAbsolutePath());
			return suppliedDicomElements;
		}
		try {
			String contents = EPADFileUtils.readFileAsString(tagFile);
			String[] tags = contents.split("\n");
			Map<String, String> tagMap = new HashMap<String, String>();
			for (String tag: tags) {
				int paren1 = tag.indexOf("(");
				if (paren1 == -1) continue;
				int paren2 = tag.indexOf(")");
				if (paren2 == -1) continue;
				int square1 = tag.indexOf("[");
				if (square1 == -1) continue;
				int square2 = tag.indexOf("]");
				if (square2 == -1) continue;
				String tagCode = tag.substring(paren1, paren2+1);
				String tagValue = tag.substring(square1+1, square2);
				log.debug("tagCode:" + tagCode + " tagValue:" + tagValue);
				tagMap.put(tagCode, tagValue);
			}
			for (int i = 0; i < suppliedDicomElements.ResultSet.totalRecords; i++) {
				DICOMElement dicomElement = suppliedDicomElements.ResultSet.Result.get(i);
				if (dicomElement.tagCode.equals(PixelMedUtils.SliceThicknessCode) && tagMap.containsKey(PixelMedUtils.SliceThicknessCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceThicknessCode, PixelMedUtils.SliceThicknessTagName,
							tagMap.get(PixelMedUtils.SliceThicknessCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.SliceLocationCode) && tagMap.containsKey(PixelMedUtils.SliceLocationCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.SliceLocationCode, PixelMedUtils.SliceLocationTagName,
							tagMap.get(PixelMedUtils.SliceLocationCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.PixelSpacingCode) && tagMap.containsKey(PixelMedUtils.PixelSpacingCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelSpacingCode, PixelMedUtils.PixelSpacingTagName,
							tagMap.get(PixelMedUtils.SliceLocationCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.RescaleInterceptCode) && tagMap.containsKey(PixelMedUtils.RescaleInterceptCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.RescaleInterceptCode, PixelMedUtils.RescaleInterceptTagName,
							tagMap.get(PixelMedUtils.RescaleInterceptCode)));
					log.info("rescale-int "+ tagMap.get(PixelMedUtils.RescaleInterceptCode)); //ml
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.RescaleSlopeCode) && tagMap.containsKey(PixelMedUtils.RescaleSlopeCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.RescaleSlopeCode, PixelMedUtils.RescaleSlopeTagName,
							tagMap.get(PixelMedUtils.RescaleSlopeCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.RescaleSlopeCode) && tagMap.containsKey(PixelMedUtils.RescaleSlopeCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.RescaleSlopeCode, PixelMedUtils.RescaleSlopeTagName,
							tagMap.get(PixelMedUtils.RescaleSlopeCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.RowsCode) && tagMap.containsKey(PixelMedUtils.RowsCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.RowsCode, PixelMedUtils.RowsTagName,
							tagMap.get(PixelMedUtils.RowsCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.ColumnsCode) && tagMap.containsKey(PixelMedUtils.ColumnsCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.ColumnsCode, PixelMedUtils.ColumnsTagName,
							tagMap.get(PixelMedUtils.ColumnsCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.BitsStoredCode) && tagMap.containsKey(PixelMedUtils.BitsStoredCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.BitsStoredCode, PixelMedUtils.BitsStoredTagName,
							tagMap.get(PixelMedUtils.BitsStoredCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.PixelRepresentationCode) && tagMap.containsKey(PixelMedUtils.PixelRepresentationCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.PixelRepresentationCode, PixelMedUtils.PixelRepresentationTagName,
							tagMap.get(PixelMedUtils.PixelRepresentationCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.NumberOfFramesCode) && tagMap.containsKey(PixelMedUtils.NumberOfFramesCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.NumberOfFramesCode, PixelMedUtils.NumberOfFramesTagName,
							tagMap.get(PixelMedUtils.NumberOfFramesCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.WindowWidthCode) && tagMap.containsKey(PixelMedUtils.WindowWidthCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowWidthCode, PixelMedUtils.WindowWidthTagName,
							tagMap.get(PixelMedUtils.WindowWidthCode)));
				}
				else if (dicomElement.tagCode.equals(PixelMedUtils.WindowCenterCode) && tagMap.containsKey(PixelMedUtils.WindowCenterCode))
				{
					defaultDicomElements.add(new DICOMElement(PixelMedUtils.WindowCenterCode, PixelMedUtils.WindowCenterTagName,
							tagMap.get(PixelMedUtils.WindowCenterCode)));
				}
				else
					defaultDicomElements.add(dicomElement);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DICOMElementList(defaultDicomElements);
	}


	private Map<String, List<DICOMElement>> generateDICOMElementMap(DICOMElementList dicomElementList)
	{
		Map<String, List<DICOMElement>> result = new HashMap<>();

		for (DICOMElement dicomElement : dicomElementList.ResultSet.Result) {
			if (result.get(dicomElement.tagCode) != null)
				result.get(dicomElement.tagCode).add(dicomElement);
			else {
				List<DICOMElement> dicomElements = new ArrayList<>();
				dicomElements.add(dicomElement);
				result.put(dicomElement.tagCode, dicomElements);
			}
		}
		return result;
	}

	static Map<String, String> defaultWindow = new HashMap<String, String>();
	static { 
		defaultWindow.put("CT-ABDOMEN", "350,40");
		defaultWindow.put("CT-PELVIS", "350,40");
		defaultWindow.put("CT-OVARY", "350,40");
		defaultWindow.put("CT-BONE", "2500,480");
		defaultWindow.put("CT-BRAIN", "80,40");
		defaultWindow.put("CT-CTA (3D-MIP)", "650,50");
		defaultWindow.put("CT-KIDNEY", "700,50");
		defaultWindow.put("CT-LIVER", "120,70");
		defaultWindow.put("CT-LUNG", "1500,-500");
		defaultWindow.put("CT-MEDIASTINUM,CHEST", "4000,40");
		defaultWindow.put("CT-MYELOGRAM", "880,110");
		defaultWindow.put("CT-NECK", "350,20");
		defaultWindow.put("CT-SINUS", "2000,100");
		defaultWindow.put("CT-STROKE", "50,40");
		defaultWindow.put("CT-SUBDURAL", "350,90");
		defaultWindow.put("MR-BRAIN", "1200,800");
		defaultWindow.put("MR-T1", "480,225");
		defaultWindow.put("MR-T2", "350,300");
		defaultWindow.put("MR-FLAIR", "800,170");
		defaultWindow.put("MR-PD", "1900,950");
		defaultWindow.put("US-LOW CONTRAST", "190,80");
		defaultWindow.put("US-MED CONTRAST", "160,70");
		defaultWindow.put("US-HIGH CONTRAST", "120,60");
		defaultWindow.put("XA-CARDIAC (10 BIT)", "1024,512");
		defaultWindow.put("XA-CARDIAC (12 BIT)", "4096,2048");
		defaultWindow.put("XA-CARDIAC (8 BIT)", "255,127");
	};

	private List<DICOMElement> getCalculatedWindowingDICOMElements(String studyUID, String seriesUID, String imageUID, boolean useMax, String modality, String bodyPart)
	{
		List<DICOMElement> dicomElements = new ArrayList<>();
		long windowWidth = 400;
		long windowCenter = 0;
		String dicomImageFilePath = null;

		try {
			File temporaryDicomFile = File.createTempFile(imageUID, ".dcm");
			DCM4CHEEUtil.downloadDICOMFileFromWADO(studyUID, seriesUID, imageUID, temporaryDicomFile);

			dicomImageFilePath = temporaryDicomFile.getAbsolutePath();
			Opener opener = new Opener();
			ImagePlus image = null;
			try {
				image = opener.openImage(dicomImageFilePath);
			} catch (Error x) {
				log.warning("ImageJ error opening image");
			} catch (Throwable x) {
				log.warning("ImageJ error opening image");
			}


			if (image != null) {
				// This method to get Window parameters in overriden below (need to test which one is correct)
				double min = image.getDisplayRangeMin();
				double max = image.getDisplayRangeMax();
				Calibration cal = image.getCalibration();
				double minValue = cal.getCValue(min);
				double maxValue = cal.getCValue(max);
				windowWidth = Math.round(maxValue - minValue);
				if (windowWidth == 0)
				{
					windowWidth = 400;
				}
				windowCenter = Math.round(minValue + windowWidth / 2.0);
				ImageProcessor ip = image.getProcessor();
				log.info("Image, min:" + minValue + " max:" + maxValue + " width:" + windowWidth + " center:" + windowCenter);
				if (ip != null)
					log.info("Processor min:" + ip.getMinThreshold() + " minh:"+ ip.getHistogramMin() + " max:" + ip.getMax() + " calmin:" + cal.getCValue(ip.getMin()) + " calmx:"+ cal.getCValue(ip.getMax()));
				// New method to get window parameters
				ImageStatistics is = image.getStatistics();
				if (is != null)
				{
					min = is.min;
					max = is.max;
					log.info("Statistics, min:" + min + " max:" + max + " all:" + is);
					long width = Math.round(max - min);
					long center = Math.round(min + width/2.0);
					if (width > 0)
					{
						windowWidth = width;
						windowCenter = center;
					}
				}
				if (cal.isSigned16Bit() && max < 5000) // Signed values can be negative/positive
					windowCenter = 0;
				log.info("Calculated, windowWidth:" + windowWidth + " windowCenter:" + windowCenter);
				if (useMax && windowWidth != 255 && windowCenter !=128) { 	//temporary test
					windowCenter = 16384;
					windowWidth = 32768;
				}
				String key = modality + "-" + bodyPart;
				if (defaultWindow.get(key.toUpperCase()) != null)
				{
					String[] win = defaultWindow.get(key.toUpperCase()).split(",");
					windowCenter = getInt(win[1]);
					windowWidth = getInt(win[0]);
				}

				log.info("Image " + imageUID + " in series " + seriesUID + " has a calculated window width of " + windowWidth
						+ " and window center of " + windowCenter + " signed:" + cal.isSigned16Bit());
				temporaryDicomFile.delete();
			} else {
				log.warning("ImageJ failed to load DICOM file for image " + imageUID + " in series " + seriesUID + " path: " + dicomImageFilePath
						+ " to calculate windowing");
				if (useMax) {
					windowCenter = 16384;
					windowWidth = 32768;
				}
			}
		} catch (Exception e) {
			log.warning("Error getting DICOM file from dcm4chee for image " + imageUID + " in series " + seriesUID, e);
		}

		dicomElements.add(new DICOMElement(PixelMedUtils.WindowWidthCode, PixelMedUtils.WindowWidthTagName, ""
				+ windowWidth));
		dicomElements.add(new DICOMElement(PixelMedUtils.WindowCenterCode, PixelMedUtils.WindowCenterTagName, ""
				+ windowCenter));

		return dicomElements;
	}

	@Override
	public EPADUserList getUserDescriptions(String username, String sessionID, boolean returnUsage)
			throws Exception {
		EPADUserList userlist = new EPADUserList();
		List<User> users = projectOperations.getAllUsers();
		for (User user: users) {
			Set<String> permissions = new HashSet<String>();
			String[] perms = user.getPermissions().split(",");
			for (String perm: perms)
				permissions.add(perm);
			Set<String> projects = null;
			List<String> projectToRole = null;
			if (user.getProjectToRole() != null)
			{
				projects = user.getProjectToRole().keySet();
				projectToRole = new ArrayList<String>();
				if (projects != null)
				{
					for (String project: projects)
					{
						projectToRole.add(project + ":" + user.getProjectToRole().get(project));
					}
				}
			}
			if (projectOperations.isAdmin(username) || username.equals(user.getUsername()) || username.equals(user.getCreator()))
			{
				EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
						user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", permissions, projects, projectToRole);
				epadUser.colorpreference = user.getColorpreference();
				epadUser.creator = user.getCreator();
				if (returnUsage)
				{
					EpadStatistics userStats = projectOperations.getUserStatistics(username, user.getUsername(), false);
					if (userStats != null)
						epadUser.usage = new EPADUsage(userStats.getHost(), userStats.getNumOfUsers(), userStats.getNumOfProjects(),
								userStats.getNumOfPatients(), userStats.getNumOfStudies(), userStats.getNumOfSeries(),
								userStats.getNumOfAims(), userStats.getNumOfDSOs(), userStats.getNumOfPacs(), userStats.getNumOfAutoQueries(),
								userStats.getNumOfWorkLists(), userStats.getNumOfFiles(), userStats.getNumOfTemplates(), userStats.getNumOfPlugins(), dateformat.format(new Date()));
				}
				userlist.addEPADUser(epadUser);
			}
			else
			{
				EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
						user.getFirstName(), user.getLastName(), "******", user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", permissions, projects, projectToRole);
				epadUser.colorpreference = user.getColorpreference();
				if (returnUsage)
				{
					EpadStatistics userStats = projectOperations.getUserStatistics(username, user.getUsername(), false);
					if (userStats != null)
						epadUser.usage = new EPADUsage(userStats.getHost(), userStats.getNumOfUsers(), userStats.getNumOfProjects(),
								userStats.getNumOfPatients(), userStats.getNumOfStudies(), userStats.getNumOfSeries(),
								userStats.getNumOfAims(), userStats.getNumOfDSOs(), userStats.getNumOfPacs(), userStats.getNumOfAutoQueries(),
								userStats.getNumOfWorkLists(), userStats.getNumOfFiles(), userStats.getNumOfTemplates(), userStats.getNumOfPlugins(), dateformat.format(new Date()));
				}
				userlist.addEPADUser(epadUser);
			}
		}
		return userlist;
	}

	@Override
	public EPADUser getUserDescription(String loggedInusername,
			String username, String sessionID, boolean returnUsage) throws Exception {
		User user = projectOperations.getUser(username);
		if (user == null) {
			user = projectOperations.getUserByEmail(username);
			if (user == null) return null;
		}
		Set<String> permissions = new HashSet<String>();
		String[] perms = user.getPermissions().split(",");
		for (String perm: perms)
			permissions.add(perm);
		Set<String> projects = null;
		List<String> projectToRole = null;
		if (user.getProjectToRole() != null)
		{
			projects = user.getProjectToRole().keySet();
			projectToRole = new ArrayList<String>();
			if (projects != null)
			{
				for (String project: projects)
				{
					projectToRole.add(project + ":" + user.getProjectToRole().get(project));
				}
			}
		}
		EPADUser epadUser = null;
		if (projectOperations.isAdmin(loggedInusername) || loggedInusername.equals(user.getUsername()) || loggedInusername.equals(user.getCreator()))
		{
			epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
					user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", permissions, projects, projectToRole);
			epadUser.colorpreference = user.getColorpreference();
			epadUser.creator = user.getCreator();
			if (returnUsage)
			{
				EpadStatistics userStats = projectOperations.getUserStatistics(username, user.getUsername(), false);
				if (userStats != null)
					epadUser.usage = new EPADUsage(userStats.getHost(), userStats.getNumOfUsers(), userStats.getNumOfProjects(),
							userStats.getNumOfPatients(), userStats.getNumOfStudies(), userStats.getNumOfSeries(),
							userStats.getNumOfAims(), userStats.getNumOfDSOs(), userStats.getNumOfPacs(), userStats.getNumOfAutoQueries(),
							userStats.getNumOfWorkLists(), userStats.getNumOfFiles(), userStats.getNumOfTemplates(), userStats.getNumOfPlugins(), dateformat.format(new Date()));
			}
		}
		else
		{
			epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
					user.getFirstName(), user.getLastName(), "******", user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", permissions, projects, projectToRole);
			epadUser.colorpreference = user.getColorpreference();
			if (returnUsage)
			{
				EpadStatistics userStats = projectOperations.getUserStatistics(username, user.getUsername(), false);
				if (userStats != null)
					epadUser.usage = new EPADUsage(userStats.getHost(), userStats.getNumOfUsers(), userStats.getNumOfProjects(),
							userStats.getNumOfPatients(), userStats.getNumOfStudies(), userStats.getNumOfSeries(),
							userStats.getNumOfAims(), userStats.getNumOfDSOs(), userStats.getNumOfPacs(), userStats.getNumOfAutoQueries(),
							userStats.getNumOfWorkLists(), userStats.getNumOfFiles(), userStats.getNumOfTemplates(), userStats.getNumOfPlugins(), dateformat.format(new Date()));
			}
		}
		return epadUser;

	}

	@Override
	public void createOrModifyUser(String loggedInUserName, String username,
			String firstname, String lastname, String email, String password,String oldpassword, String colorpreference,
			String[] addPermissions, String[] removePermissions) throws Exception {
		User user = projectOperations.getUser(username);
		User loggedInUser = projectOperations.getUser(loggedInUserName);
		if (!loggedInUser.isAdmin() && (user == null || !loggedInUserName.equals(username)) && !loggedInUser.hasPermission(User.CreateUserPermission))
			throw new Exception("User " + loggedInUserName + " does not have privilege to create/modify users");

		List<String> addPerms = new ArrayList<String>();
		List<String> removePerms = new ArrayList<String>();
		if (addPermissions != null)
		{
			for (String perm: addPermissions)
			{
				perm = perm.trim();
				if (perm.indexOf(",") != -1)
				{
					String[] perms = perm.split(",");
					for (String p: perms)
					{
						p = p.trim();
						if (p.length() > 0)
							addPerms.add(p);
					}
				}
				else
				{
					if (perm.length() > 0)
						addPerms.add(perm);
				}
			}
		}
		if (removePermissions != null)
		{
			for (String perm: removePermissions)
			{
				perm = perm.trim();
				if (perm.indexOf(",") != -1)
				{
					String[] perms = perm.split(",");
					for (String p: perms)
					{
						p = p.trim();
						if (p.length() > 0)
							removePerms.add(p);
					}
				}
				else
				{
					if (perm.length() > 0)
						removePerms.add(perm);
				}
			}
		}
		if (user == null)
		{
			projectOperations.createUser(loggedInUserName, username, firstname, lastname, email, password, colorpreference, addPerms, removePerms);
		}
		else
		{
			projectOperations.updateUser(loggedInUserName, username, firstname, lastname, email, password, oldpassword, colorpreference, addPerms, removePerms);
		}

	}

	@Override
	public void setAdmin(String loggedInUser, String username) throws Exception {
		projectOperations.setAdmin(loggedInUser, username);
	}

	@Override
	public void resetAdmin(String loggedInUser, String username) throws Exception {
		projectOperations.resetAdmin(loggedInUser, username);
	}

	@Override
	public void enableUser(String loggedInUser, String username) throws Exception {
		projectOperations.enableUser(loggedInUser, username);
	}

	@Override
	public void disableUser(String loggedInUser, String username) throws Exception {
		projectOperations.disableUser(loggedInUser, username);
	}

	@Override
	public void deleteUser(String loggedInUser, String username) throws Exception {
		try
		{
			projectOperations.deleteUser(loggedInUser, username);
		}
		catch (Exception x)
		{
			log.warning("Error deleting user:" + username, x);
			throw new Exception(x.getMessage());
		}
	}

	@Override
	public EPADUserList getUserDescriptions(String username,
			ProjectReference projectReference, String sessionID)
					throws Exception {
		EPADUserList userlist = new EPADUserList();
		List<User> users = projectOperations.getUsersWithRoleForProject(projectReference.projectID);
		for (User user: users) {
			Set<String> permissions = new HashSet<String>();
			String[] perms = user.getPermissions().split(",");
			for (String perm: perms)
				permissions.add(perm);
			if (projectOperations.isAdmin(username) || username.equals(user.getUsername()) || username.equals(user.getCreator()))
			{
				EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
						user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), user.getRole(), permissions);
				epadUser.colorpreference = user.getColorpreference();
				epadUser.creator = user.getCreator();
				userlist.addEPADUser(epadUser);
			}
			else
			{
				EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
						user.getFirstName(), user.getLastName(), "******", user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), user.getRole(), permissions);
				epadUser.colorpreference = user.getColorpreference();
				userlist.addEPADUser(epadUser);
			}
		}
		return userlist;
	}

	@Override
	public void addUserToProject(String loggedInusername,
			ProjectReference projectReference, String username, String roleName, String defaultTemplate, String sessionID)
					throws Exception {
		User user = projectOperations.getUser(username);
		if (!projectOperations.isOwner(loggedInusername, projectReference.projectID) && !user.isAdmin() && !projectOperations.hasAccessToProject(username, projectReference.projectID))
			throw new Exception("User " + loggedInusername + " is not the owner of " + projectReference.projectID);
		if (roleName != null && !projectOperations.isOwner(loggedInusername, projectReference.projectID) && !user.isAdmin())
			throw new Exception("User " + loggedInusername + " is not the owner of " + projectReference.projectID);
		UserRole role = UserRole.getRole(roleName);
		projectOperations.addUserToProject(loggedInusername, projectReference.projectID, username, role, defaultTemplate);

	}

	@Override
	public void removeUserFromProject(String loggedInusername,
			ProjectReference projectReference, String username, String sessionID)
					throws Exception {
		User user = projectOperations.getUser(username);
		if (!projectOperations.isOwner(loggedInusername, projectReference.projectID) && !user.isAdmin())
			throw new Exception("User " + loggedInusername + " is not the owner of " + projectReference.projectID);
		projectOperations.removeUserFromProject(loggedInusername, projectReference.projectID, username);
	}

	@Override
	public EPADUserList getReviewers(String loggedInusername, String username,
			String sessionID) throws Exception {
		EPADUserList userlist = new EPADUserList();
		List<User> users = projectOperations.getReviewers(username);
		for (User user: users) {
			EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
					user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", null);
			epadUser.colorpreference = user.getColorpreference();
			userlist.addEPADUser(epadUser);
		}
		return userlist;
	}

	@Override
	public EPADUserList getReviewees(String loggedInusername, String username,
			String sessionID) throws Exception {
		EPADUserList userlist = new EPADUserList();
		List<User> users = projectOperations.getReviewees(username);
		for (User user: users) {
			EPADUser epadUser = new EPADUser(user.getFullName(), user.getUsername(), 
					user.getFirstName(), user.getLastName(), user.getEmail(), user.isEnabled(), user.isAdmin(), user.isPasswordExpired(), "", null);
			epadUser.colorpreference = user.getColorpreference();
			userlist.addEPADUser(epadUser);
		}
		return userlist;
	}

	@Override
	public void addReviewer(String loggedInUser, String username,
			String reviewer) throws Exception {
		projectOperations.addReviewer(loggedInUser, username, reviewer);
	}

	@Override
	public void addReviewee(String loggedInUser, String username,
			String reviewee) throws Exception {
		projectOperations.addReviewee(loggedInUser, username, reviewee);
	}

	@Override
	public void removeReviewer(String loggedInUser, String username,
			String reviewer) throws Exception {
		projectOperations.removeReviewer(loggedInUser, username, reviewer);
	}

	@Override
	public void removeReviewee(String loggedInUser, String username,
			String reviewee) throws Exception {
		projectOperations.removeReviewee(loggedInUser, username, reviewee);
	}

	/**
	 * @param username
	 * @param imageReference - Image from old study where old annotation created
	 * @param followupStudyUID - StudyID of new study
	 * @param sessionID
	 * @return matching image from new study (same location)
	 * @throws Exception
	 */
	@Override
	public EPADImage getSameSliceFromNewStudy(String username, ImageReference imageReference, String followupStudyUID, String sessionID) throws Exception
	{
		SeriesReference oldSeriesReference = new SeriesReference(imageReference.projectID, imageReference.subjectID, imageReference.studyUID, imageReference.seriesUID); 
		EPADSeries oldSeries = this.getSeriesDescription(oldSeriesReference, username, sessionID);
		EPADImage oldImage = this.getImageDescription(imageReference, sessionID);
		StudyReference studyReference = new StudyReference(imageReference.projectID, imageReference.subjectID, followupStudyUID); 
		EPADSeriesList serieses = getSeriesDescriptions(studyReference, username, sessionID, new EPADSearchFilter(), true);
		for (EPADSeries newSeries: serieses.ResultSet.Result) {
			if (oldSeries.seriesDescription.equals(newSeries.seriesDescription) || serieses.ResultSet.totalRecords == 1) {
				SeriesReference newSeriesReference = new SeriesReference(imageReference.projectID, imageReference.subjectID, newSeries.studyUID, newSeries.seriesUID);
				EPADImageList imageList = this.getImageDescriptions(newSeriesReference, sessionID, new EPADSearchFilter());
				EPADImage closest = imageList.ResultSet.Result.get(0);
				double diff = Math.abs(getDouble(oldImage.sliceLocation) - getDouble(closest.sliceLocation));
				for (EPADImage image: imageList.ResultSet.Result) {
					double diff2 = Math.abs(getDouble(oldImage.sliceLocation) - getDouble(image.sliceLocation));
					if (diff2 < diff) {
						closest = image;
						diff = diff2;
					}
				}
				return closest;
			}
		}
		return null;
	}

	double getDouble(String doubleStr)
	{
		try
		{
			return Double.valueOf(doubleStr);
		}
		catch (Exception x) {}
		return 0.0;
	}
	// This is Pixelmed variant (though does not seem to be correct).
	// SourceImage srcDicomImage = new SourceImage(temporaryDicomFile.getAbsolutePath());
	// ImageEnhancer imageEnhancer = new ImageEnhancer(srcDicomImage);
	// imageEnhancer.findVisuParametersImage();
	// windowWidth = Math.round(imageEnhancer.getWindowWidth());
	// windowCenter = Math.round(imageEnhancer.getWindowCenter());

	//ml
	@Override
	public EPADProjectList getProjectsForStudy(String username, String sessionID,EPADSearchFilter searchFilter, boolean annotationCount, String studyUID) throws Exception
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		long starttime = System.currentTimeMillis();
		List<Project> projects = new ArrayList<Project>();
		List projectList =	projectOperations.getProjectsForStudy(studyUID);
		projectList = projectOperations.sort(projectList, "name", true);
		projects.addAll(projectList);
		long gettime = System.currentTimeMillis();
		log.info("get projects for study " + studyUID + " returned " + projectList.size() +" items");
		for (Project project : projects) {
			if ((project.getProjectId().equals(EPADConfig.xnatUploadProjectID) || project.getProjectId().equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned"))))
				continue;
			EPADProject epadProject = project2EPADProject(sessionID, username, project, searchFilter, false);

			if (epadProject != null)
			{
				//log.info("project " + epadProject.id + " aim count:" + epadProject.numberOfAnnotations);
				epadProjectList.addEPADProject(epadProject);
			}
		}
		long convtime = System.currentTimeMillis();
		log.info("Time to get " + epadProjectList.ResultSet.totalRecords + " projects:" + (gettime-starttime) + " msecs, to convert:" + (convtime-gettime) + " msecs");
		return epadProjectList;
	}
	//ml
	@Override
	public EPADProjectList getProjectsForSubject(String username, String sessionID,EPADSearchFilter searchFilter, boolean annotationCount, String subjectUID) throws Exception
	{
		EPADProjectList epadProjectList = new EPADProjectList();
		long starttime = System.currentTimeMillis();
		List<Project> projects = new ArrayList<Project>();
		List projectList =	projectOperations.getProjectsForSubject(subjectUID);
		projectList = projectOperations.sort(projectList, "name", true);
		projects.addAll(projectList);
		long gettime = System.currentTimeMillis();
		log.info("get projects for subject " + subjectUID + " returned " + projectList.size() +" items");
		for (Project project : projects) {
			if ((project.getProjectId().equals(EPADConfig.xnatUploadProjectID) || project.getProjectId().equals(EPADConfig.getParamValue("UnassignedProjectID", "nonassigned"))))
				continue;
			EPADProject epadProject = project2EPADProject(sessionID, username, project, searchFilter, false);

			if (epadProject != null)
			{
				//log.info("project " + epadProject.id + " aim count:" + epadProject.numberOfAnnotations);
				epadProjectList.addEPADProject(epadProject);
			}
		}
		long convtime = System.currentTimeMillis();
		log.info("Time to get " + epadProjectList.ResultSet.totalRecords + " projects:" + (gettime-starttime) + " msecs, to convert:" + (convtime-gettime) + " msecs");
		return epadProjectList;
	}

}
