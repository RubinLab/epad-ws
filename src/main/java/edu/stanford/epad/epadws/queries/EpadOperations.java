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

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.stanford.epad.common.dicom.DICOMFileDescription;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADAIMList;
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
import edu.stanford.epad.dtos.EPADSeries;
import edu.stanford.epad.dtos.EPADSeriesList;
import edu.stanford.epad.dtos.EPADStudy;
import edu.stanford.epad.dtos.EPADStudyList;
import edu.stanford.epad.dtos.EPADSubject;
import edu.stanford.epad.dtos.EPADSubjectList;
import edu.stanford.epad.dtos.EPADTemplateContainer;
import edu.stanford.epad.dtos.EPADTemplateContainerList;
import edu.stanford.epad.dtos.EPADTemplateUsageList;
import edu.stanford.epad.dtos.EPADUsageList;
import edu.stanford.epad.dtos.EPADUser;
import edu.stanford.epad.dtos.EPADUserList;
import edu.stanford.epad.dtos.EPADWorklist;
import edu.stanford.epad.dtos.EPADWorklistList;
import edu.stanford.epad.dtos.EPADWorklistStudyList;
import edu.stanford.epad.dtos.EPADWorklistSubjectList;
import edu.stanford.epad.dtos.internal.DCM4CHEESeries;
import edu.stanford.epad.dtos.internal.DICOMElementList;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.handlers.core.EPADSearchFilter;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.models.ProjectType;
import edu.stanford.epad.epadws.models.Template;
import edu.stanford.epad.epadws.security.EPADSession;

/**
 * 
 * @author martin
 */
public interface EpadOperations
{
	/**
	 * Get list of projects accessible to user
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param annotationCount
	 * @return
	 * @throws Exception
	 */
	EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount, boolean ignoreSystem) throws Exception;

	/**
	 * Get project description
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @param annotationCount
	 * @return
	 * @throws Exception
	 */
	EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID, boolean annotationCount) throws Exception;

	/**
	 * Get subject descriptions for a project 
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param start
	 * @param count
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter, int start, int count, String sortField, boolean annotationCount) throws Exception;
	
	EPADSubjectList getWorklistSubjectDescriptions(String projectID, String username, String worklistID, EPADSearchFilter searchFilter, String sessionID, String sortField) throws Exception;

	/**
	 * Get Unassigned subject descriptions
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param start
	 * @param count
	 * @param sortField
	 * @return
	 * @throws Exception
	 */
	EPADSubjectList getUnassignedSubjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get subject description
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID) throws Exception;

	/**
	 * Get study descriptions for a subject 
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;
	
	/**
	 * Get study description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID) throws Exception;
	
	/**
	 * Get series descriptions for a study
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param filterDSOs
	 * @return
	 * @throws Exception
	 */
	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs) throws Exception;

	/**
	 * Get series descriptions for a study
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param filterDSOs
	 * @param includeAnnotationStatus
	 * @return
	 * @throws Exception
	 */
	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs, boolean includeAnnotationStatus) throws Exception;

	/**
	 * Get series description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID);
	
	/**
	 * Get image descriptions for a series
	 * @param seriesReference
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 */
	EPADImageList getImageDescriptions(SeriesReference seriesReference, String sessionID, EPADSearchFilter searchFilter);
	
	/**
	 * Get image descriptions for a series with username to get the information about the flags.
	 * @param username
	 * @param seriesReference
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 */
	EPADImageList getImageDescriptions(String username, SeriesReference seriesReference, String sessionID, EPADSearchFilter searchFilter);

	/**
	 * Get image description
	 * @param imageReference
	 * @param sessionID
	 * @return
	 */
	EPADImage getImageDescription(ImageReference imageReference, String sessionID);
	EPADImage getSameSliceFromNewStudy(String username, ImageReference imageReference, String followupStudyUID, String sessionID) throws Exception;

	/**
	 * Get frame descriptions for an image
	 * @param imageReference
	 * @return
	 */
	EPADFrameList getFrameDescriptions(ImageReference imageReference);

	/**
	 * Get frame description
	 * @param frameReference
	 * @param sessionID
	 * @return
	 */
	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID);

	/**
	 * Get descriptions for users in a project
	 * @param username
	 * @param projectReference
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getUserDescriptions(String username, ProjectReference projectReference, String sessionID) throws Exception;

	/**
	 * Add a user to a project or set default template for a user
	 * @param loggedInusername
	 * @param projectReference
	 * @param username
	 * @param role
	 * @param defaultTemplate
	 * @param sessionID
	 * @throws Exception
	 */
	void addUserToProject(String loggedInusername, ProjectReference projectReference, String username, String role, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * Remove a user from a project
	 * @param loggedInusername
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @throws Exception
	 */
	void removeUserFromProject(String loggedInusername, ProjectReference projectReference, String username, String sessionID) throws Exception;

	/**
	 * Get description for all users
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getUserDescriptions(String username, String sessionID, boolean usage) throws Exception;

	/**
	 * Get description for a user
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUser getUserDescription(String loggedInusername, String username, String sessionID, boolean usage) throws Exception;

	/**
	 * Get users who can review annotations for a reader
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getReviewers(String loggedInusername, String username, String sessionID) throws Exception;

	/**
	 * Get readers whose annotations can be reviewed by this user
	 * @param loggedInusername
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADUserList getReviewees(String loggedInusername, String username, String sessionID) throws Exception;
	
	/**
	 * Create a subject and study (without a dicom)
	 * @param username
	 * @param projectID
	 * @param subjectID
	 * @param subjectName
	 * @param studyUID
	 * @param sessionID
	 * @throws Exception
	 */
	void createSubjectAndStudy(String username, String projectID, String subjectID, String subjectName, String studyUID, String sessionID) throws Exception;

	/**
	 * Create a project
	 * @param username
	 * @param projectReference
	 * @param projectName
	 * @param projectDescription
	 * @param defaultTemplate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * update a project
	 * @param username
	 * @param projectReference
	 * @param projectName
	 * @param projectDescription
	 * @param defaultTemplate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int updateProject(String username, ProjectReference projectReference, String projectName, String projectDescription, String defaultTemplate, String sessionID) throws Exception;

	/**
	 * create a subject
	 * @param username
	 * @param subjectRefernece
	 * @param subjectName
	 * @param dob
	 * @param gender
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	/**
	 * create a study
	 * @param username
	 * @param studyReference
	 * @param description
	 * @param studyDate
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createStudy(String username, StudyReference studyReference, String description, Date studyDate, String sessionID) throws Exception;

	/**
	 * update a subject
	 * @param username
	 * @param subjectRefernece
	 * @param subjectName
	 * @param dob
	 * @param gender
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int updateSubject(String username, SubjectReference subjectRefernece, String subjectName, Date dob, String gender, String sessionID) throws Exception;

	/**
	 * Upload a project file
	 * @param username
	 * @param projectReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, ProjectReference projectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a subject file
	 * @param username
	 * @param subjectReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SubjectReference subjectReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a study file
	 * @param username
	 * @param studyReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, StudyReference studyReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a series file
	 * @param username
	 * @param seriesReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a series file and convert to dicom (does not quite work)
	 * @param username
	 * @param seriesReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @param convertToDICOM
	 * @param modality
	 * @param instanceNumber
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description, String fileType, String sessionID, boolean convertToDICOM, String modality, String instanceNumber) throws Exception;

	/**
	 * Upload a image file
	 * @param username
	 * @param imageReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, ImageReference imageReference, File uploadedFile, String description, String fileType, String sessionID) throws Exception;

	/**
	 * Upload a file
	 * @param username
	 * @param projectID
	 * @param subjectID
	 * @param studyID
	 * @param seriesID
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @throws Exception
	 */
	void createFile(String username, String projectID, String subjectID, String studyID, String seriesID,
			File uploadedFile, String description, String fileType, String sessionID) throws Exception;
	
	/**
	 * Upload a dicom
	 * @param username
	 * @param projectID
	 * @param dicomFile
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createImage(String username, String projectID, File dicomFile, String sessionID) throws Exception;
	
	/**
	 * @param username
	 * @param templateFile
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	int createSystemTemplate(String username, File templateFile, String sessionID) throws Exception;	
	
	/**
	 * Get project files description
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(ProjectReference projectReference, String username, String sessionID, EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get project files description
	 * @param projectReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(ProjectReference projectReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get subject files description
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get subject files description
	 * @param subjectReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(SubjectReference subjectReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get study file description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;
	/**
	 * Get study files description
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param toplevelOnly
	 * @return
	 * @throws Exception
	 */
	List<EPADFile> getEPADFiles(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean toplevelOnly) throws Exception;

	/**
	 * Get study file description
	 * @param studyReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(StudyReference studyReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * Get series files description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	EPADFileList getFileDescriptions(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get series files description
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	List<EPADFile> getEPADFiles(SeriesReference seriesReference, String username, String sessionID,
			EPADSearchFilter searchFilter) throws Exception;

	/**
	 * Get templates
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getTemplateDescriptions(String username, String sessionID) throws Exception;

	/**
	 * Get templates with templateleveltypefilter
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getTemplateDescriptions(String username, String sessionID, String templateLevelFilter) throws Exception;

	/**
	 * Get system-wide templates
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getSystemTemplateDescriptions(String username, String sessionID) throws Exception;

	/**
	 * Get project templates
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getProjectTemplateDescriptions(String projectID, String username, String sessionID) throws Exception;
	
	/**
	 * @param seriesReference
	 * @param filename
	 * @param username
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADFile getFileDescription(SeriesReference seriesReference, String filename, String username, String sessionID) throws Exception;

	/**
	 * create Subject AIM
	 * @param username
	 * @param subjectRefernece
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createSubjectAIM(String username, SubjectReference subjectRefernece, String aimID, File aimFile, String sessionID);
	
	/**
	 * create Study AIM
	 * @param username
	 * @param studyReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createStudyAIM(String username, StudyReference studyReference, String aimID, File aimFile, String sessionID);

	/**
	 * create a non-dicom Series
	 * @param username
	 * @param seriesReference
	 * @param description
	 * @param seriesDate
	 * @param modality
	 * @param referencedSeries
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	EPADSeries createSeries(String username, SeriesReference seriesReference, String description, Date seriesDate, String modality, String referencedSeries, String sessionID) throws Exception;

	/**
	 * Update default tags for a dicom series
	 * @param username
	 * @param seriesReference
	 * @param defaultTags
	 * @param sessionID
	 * @throws Exception
	 */
	void updateSeriesTags(String username, SeriesReference seriesReference, String defaultTags, String sessionID) throws Exception;
	
	/**
	 * create Project AIM (does not make senss, just for completeness)
	 * @param username
	 * @param projectReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createProjectAIM(String username, ProjectReference projectReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Series AIM
	 * @param username
	 * @param seriesReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createSeriesAIM(String username, SeriesReference seriesReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Image AIM
	 * @param username
	 * @param imageReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createImageAIM(String username, ImageReference imageReference, String aimID, File aimFile, String sessionID);

	/**
	 * create Frame AIM
	 * @param username
	 * @param frameReference
	 * @param aimID
	 * @param aimFile
	 * @param sessionID
	 * @return
	 */
	String createFrameAIM(String username, FrameReference frameReference, String aimID, File aimFile, String sessionID);

	/**
	 * Set Subject Status per user
	 * @param subjectReference
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	String setSubjectStatus(SubjectReference subjectReference, String sessionID, String username) throws Exception;
	
	/**
	 * create/modify user
	 * @param loggedInUser
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param password
	 * @param oldpassword
	 * @param addPermission
	 * @param removePermissions
	 * @throws Exception
	 */
	void createOrModifyUser(String loggedInUser, String username, String firstname, String lastname, String email, 
			String password, String oldpassword, String colorpreference, String[]addPermission, String[] removePermissions) throws Exception;
	
	/**
	 * set user as admin
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void setAdmin(String loggedInUser, String username) throws Exception;
	
	/**
	 * set user as not admin
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void resetAdmin(String loggedInUser, String username) throws Exception;
	
	/**
	 * enable user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void enableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * disable user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void disableUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * delete user
	 * @param loggedInUser
	 * @param username
	 * @throws Exception
	 */
	void deleteUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Add reviewer for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void addReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * Add reviewee for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void addReviewee(String loggedInUser, String username, String reviewee) throws Exception;
	
	/**
	 * Remove reviewer for a user
	 * @param loggedInUser
	 * @param username
	 * @param reviewer
	 * @throws Exception
	 */
	void removeReviewer(String loggedInUser, String username, String reviewer) throws Exception;
	
	/**
	 * @param loggedInUser
	 * @param username
	 * @param reviewee
	 * @throws Exception
	 */
	void removeReviewee(String loggedInUser, String username, String reviewee) throws Exception;
	
	/**
	 * @param projectID
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int projectDelete(String projectID, String sessionID, String username) throws Exception;

	/**
	 * @param subjectReference
	 * @param sessionID
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int subjectDelete(SubjectReference subjectReference, String sessionID, String username) throws Exception;

	/**
	 * @param subjectReference
	 * @param sessionID
	 * @param username
	 * @param all
	 * @return
	 * @throws Exception
	 */
	int subjectDelete(SubjectReference subjectReference, String sessionID, String username, boolean all) throws Exception;

	/**
	 * @param studyReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @return
	 * @throws Exception
	 */
	String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username) throws Exception;

	/**
	 * @param studyReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @param all
	 * @return
	 * @throws Exception
	 */
	String studyDelete(StudyReference studyReference, String sessionID, boolean deleteAims, String username, boolean all) throws Exception;

	/**
	 * Delete series
	 * @param seriesReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @return
	 */
	String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username) throws Exception;
	
	/**
	 * Delete series
	 * @param seriesReference
	 * @param sessionID
	 * @param deleteAims
	 * @param username
	 * @param all
	 * @return
	 */
	String seriesDelete(SeriesReference seriesReference, String sessionID, boolean deleteAims, String username, boolean all) throws Exception;
	
	/**
	 * Delete series
	 * @param seriesReference
	 * @param deleteAims
	 * @return
	 */
	String deleteSeries(SeriesReference seriesReference, boolean deleteAims);
	void deleteSeriesPNGs(SeriesReference seriesReference);

	/**
	 * Delete project aim
	 * @param projectReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int projectAIMDelete(ProjectReference projectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete subject aim
	 * @param subjectReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int subjectAIMDelete(SubjectReference subjectReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete study aim
	 * @param studyReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int studyAIMDelete(StudyReference studyReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete series aim
	 * @param seriesReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int seriesAIMDelete(SeriesReference seriesReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete image aim
	 * @param imageReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int imageAIMDelete(ImageReference imageReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete frame aim
	 * @param frameReference
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int frameAIMDelete(FrameReference frameReference, String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;

	/**
	 * Delete aim
	 * @param aimID
	 * @param sessionID
	 * @param deleteDSO
	 * @param username
	 * @return
	 * @throws Exception
	 */
	int aimDelete(String aimID, String sessionID, boolean deleteDSO, String username) throws Exception;
	
	/**
	 * Delete all series aim
	 * @param seriedUID
	 * @param deleteDSOs
	 */
	void deleteAllSeriesAims(String seriedUID, boolean deleteDSOs);
	
	/**
	 * Delete all study aim
	 * @param studyUID
	 * @param deleteDSOs
	 */
	void deleteAllStudyAims(String studyUID, boolean deleteDSOs);

	/**
	 * Delete all project aim
	 * @param projectID
	 * @param subjectID
	 * @param studyUID
	 * @param seriesUID
	 * @param deleteDSOs
	 */
	void deleteAllAims(String projectID, String subjectID, String studyUID, String seriesUID, boolean deleteDSOs);

	/**
	 * Delete subject file
	 * @param username
	 * @param subjectReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, SubjectReference subjectReference, String fileName) throws Exception;

	/**
	 * Delete project file
	 * @param username
	 * @param projectReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, ProjectReference projectReference, String fileName) throws Exception;

	/**
	 * Delete study file
	 * @param username
	 * @param studyReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, StudyReference studyReference, String fileName) throws Exception;

	/**
	 * Delete series file
	 * @param username
	 * @param seriesReference
	 * @param fileName
	 * @throws Exception
	 */
	void deleteFile(String username, SeriesReference seriesReference, String fileName) throws Exception;

	/**
	 * Delete file
	 * @param username
	 * @param fileName
	 * @throws Exception
	 */
	public void deleteFile(String username, String fileName) throws Exception;
	
	/**
	 * Get project aims
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getProjectAIMDescriptions(ProjectReference projectReference, String username, String sessionID);

	/**
	 * @param projectReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getProjectAIMDescription(ProjectReference projectReference, String aimID, String username, String sessionID);

	/**
	 * Get subject aims
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getSubjectAIMDescriptions(SubjectReference subjectReference, String username, String sessionID);

	/**
	 * @param subjectReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getSubjectAIMDescription(SubjectReference subjectReference, String aimID, String username, String sessionID);

	/**
	 * Get study aims
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getStudyAIMDescriptions(StudyReference studyReference, String username, String sessionID);

	/**
	 * @param studyReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getStudyAIMDescription(StudyReference studyReference, String aimID, String username, String sessionID);

	/**
	 * Get series aims
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID);

	/**
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param includeStudyAims
	 * @return
	 */
	EPADAIMList getSeriesAIMDescriptions(SeriesReference seriesReference, String username, String sessionID, boolean includeStudyAims);

	/**
	 * @param seriesReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getSeriesAIMDescription(SeriesReference seriesReference, String aimID, String username, String sessionID);

	/**
	 * Get image aims
	 * @param imageReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getImageAIMDescriptions(ImageReference imageReference, String username, String sessionID);

	/**
	 * @param imageReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getImageAIMDescription(ImageReference imageReference, String aimID, String username, String sessionID);

	/**
	 * Get frame aims
	 * @param frameReference
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getFrameAIMDescriptions(FrameReference frameReference, String username, String sessionID);

	/**
	 * @param frameReference
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getFrameAIMDescription(FrameReference frameReference, String aimID, String username, String sessionID);

	/**
	 * Search for aims
	 * @param projectID
	 * @param aimSearchType
	 * @param searchValue
	 * @param username
	 * @param sessionID
	 * @param start
	 * @param count
	 * @return
	 */
	EPADAIMList getAIMDescriptions(String projectID, AIMSearchType aimSearchType, String searchValue, String username, String sessionID, int start, int count);
	
	/**
	 * Get aim by user
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIMList getAIMDescriptionsForUser(String username, String sessionID);
	
	/**
	 * Get aim by aimID
	 * @param aimID
	 * @param username
	 * @param sessionID
	 * @return
	 */
	EPADAIM getAIMDescription(String aimID, String username, String sessionID);
	
	/**
	 * Get worklists for user
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADWorklistList getWorkListsForUser(String loggedInUser, String username) throws Exception;
	
	/**
	 * Get studies for worklist
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklistStudyList getWorkListStudies(String loggedInUser, String username, String workListID) throws Exception;
	
	/**
	 * Get subjects for worklist
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklistSubjectList getWorkListSubjects(String loggedInUser, String username, String workListID) throws Exception;
	
	//EPADWorklistStudyList getWorkListSubjectStudies(ProjectReference projectReference, String username, String subjectID, String workListID) throws Exception;
	
	//EPADWorklist getWorkList(ProjectReference projectReference, String username) throws Exception;
	
	/**
	 * Get Worklist by ID
	 * @param username
	 * @param workListID
	 * @return
	 * @throws Exception
	 */
	EPADWorklist getWorkListByID(String loggedInUser, String username, String workListID) throws Exception;

	/**
	 * See if new series have been uploaded to DCM4CHEE that ePAD does not know about.
	 */
	List<DCM4CHEESeries> getNewDcm4CheeSeries();
	/**
	 * See if any series have been deleted from DCM4CHEE that are still in ePAD.
	 */
	Set<String> getDeletedDcm4CheeSeries();

	/**
	 * For the specified series, return a list of DICOM image file descriptions for instances that have no corresponding
	 * PNG file specified in the ePAD database.
	 * <p>
	 * Each description is a map with keys: study_iuid, series_iuid, sop_iuid, inst_no, filepath, file_size.
	 * @param seriesUID
	 * @return
	 */
	Set<DICOMFileDescription> getUnprocessedDICOMFilesInSeries(String seriesUID);

	/**
	 * @param seriesUID
	 * @param imagUID
	 * @return
	 */
	Set<DICOMFileDescription> getDICOMFilesInSeries(String seriesUID, String imagUID);

	/**
	 * @param studyUID
	 * @param deleteAims
	 */
	void deleteStudyFromEPadAndDcm4CheeDatabases(String studyUID, boolean deleteAims);

	/**
	 * @param studyUIDs
	 */
	void deleteStudiesFromEPadAndDcm4CheeDatabases(Set<String> studyUIDs);

	/**
	 * @param sessionID
	 * @param projectID
	 * @param subjectID
	 * @param searchFilter
	 * @return
	 * @throws Exception
	 */
	Set<String> getExamTypesForSubject(String sessionID, String projectID, String subjectID, EPADSearchFilter searchFilter) throws Exception;

	/**
	 * @param subjectID
	 * @return
	 */
	Set<String> getExamTypesForSubject(String subjectID);

	/**
	 * @param studyUID
	 * @return
	 */
	Set<String> getExamTypesForStudy(String studyUID);

	/**
	 * @param projectID
	 * @param subjectID
	 * @param sessionID
	 * @param searchFilter
	 * @return
	 */
	Set<String> getSeriesUIDsForSubject(String projectID, String subjectID, String sessionID,
			EPADSearchFilter searchFilter);
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADUsageList getUsage(String username, String hostname, boolean byMonth, boolean byYear, boolean all) throws Exception;
	
	/**
	 * get all the hosts' usage (distinct, latest)
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADUsageList getUsageSummary(String username) throws Exception;
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	Collection<EPADSession> getCurrentSessions(String username) throws Exception;
	
	/**
	 * @param username
	 * @return
	 * @throws Exception
	 */
	EPADEventLogList getEventLogs(String loggedInUser, String username, int start, int count) throws Exception;
	EPADObjectList getTaskStatuses(String loggedInUser, String username) throws Exception;
	
	
	EPADProjectList getProjectsForStudy(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount, String studyUID)  throws Exception; //ml
	EPADProjectList getProjectsForSubject(String username, String sessionID, EPADSearchFilter searchFilter, boolean annotationCount, String subjectUID)  throws Exception; //ml

	int createProject(String username, ProjectReference projectReference, String projectName, String projectDescription,
			String defaultTemplate, String sessionID, ProjectType type) throws Exception;

	int updateProject(String username, ProjectReference projectReference, String projectName, String projectDescription,
			String defaultTemplate, String sessionID, ProjectType type) throws Exception;

	/**
	 * get series description with or without annotation status
	 * @param seriesReference
	 * @param username
	 * @param sessionID
	 * @param includeAnnotationStatus
	 * @return series with annotation status (see AnnotationStatus class)
	 */
	EPADSeries getSeriesDescription(SeriesReference seriesReference, String username, String sessionID,
			boolean includeAnnotationStatus);

	/**
	 * get study description with or without annotation status
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param includeAnnotationStatus
	 * @return study with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADStudy getStudyDescription(StudyReference studyReference, String username, String sessionID,
			boolean includeAnnotationStatus) throws Exception;

	/**
	 * get study descriptions with or without annotation status
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param includeAnnotationStatus
	 * @return study with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADStudyList getStudyDescriptions(SubjectReference subjectReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean includeAnnotationStatus) throws Exception;

	/**
	 * get subject description with or without annotation status
	 * @param subjectReference
	 * @param username
	 * @param sessionID
	 * @param includeAnnotationStatus
	 * @return subject with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADSubject getSubjectDescription(SubjectReference subjectReference, String username, String sessionID,
			boolean includeAnnotationStatus) throws Exception;

	/**
	 * get subject descriptions with or without annotation status
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param start
	 * @param count
	 * @param sortField
	 * @param annotationCount
	 * @param includeAnnotationStatus
	 * @return subjects with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADSubjectList getSubjectDescriptions(String projectID, String username, String sessionID,
			EPADSearchFilter searchFilter, int start, int count, String sortField, boolean annotationCount,
			boolean includeAnnotationStatus) throws Exception;

	/**
	 * get project description with or without annotation status
	 * @param projectReference
	 * @param username
	 * @param sessionID
	 * @param annotationCount
	 * @param includeAnnotationStatus
	 * @return project with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADProject getProjectDescription(ProjectReference projectReference, String username, String sessionID,
			boolean annotationCount, boolean includeAnnotationStatus) throws Exception;

	/** 
	 * get project descriptions with or without annotation status
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param annotationCount
	 * @param ignoreSystem
	 * @param includeAnnotationStatus
	 * @return projects with annotation status (see AnnotationStatus class)
	 * @throws Exception
	 */
	EPADProjectList getProjectDescriptions(String username, String sessionID, EPADSearchFilter searchFilter,
			boolean annotationCount, boolean ignoreSystem, boolean includeAnnotationStatus) throws Exception;

	/**
	 * get templates with specific template description (filters with starts with)
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param templateLevelFilter Send null if you do not want to filter
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getProjectTemplateDescriptions(String projectID, String username, String sessionID,
			String templateLevelFilter) throws Exception;

	/**
	 * get system templates with specific template description (filters with starts with)
	 * @param projectID
	 * @param username
	 * @param sessionID
	 * @param templateLevelFilter Send null if you do not want to filter
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getSystemTemplateDescriptions(String username, String sessionID,
			String templateLevelFilter) throws Exception;

	
	/**
	 * get study descriptions that are not accessed than given number of days
	 * @param username
	 * @param sessionID
	 * @param days
	 * @return
	 * @throws Exception
	 */
	EPADStudyList getStudyDescriptions(String username, String sessionID, Integer days) throws Exception;

	/**
	 * get template descriptions filtering with templateLevelFilter and includeSystemTemplates
	 * @param username
	 * @param sessionID
	 * @param templateLevelFilter
	 * @param includeSystemTemplates
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainerList getTemplateDescriptions(String username, String sessionID, String templateLevelFilter,
			boolean includeSystemTemplates) throws Exception;

	
	/**
	 * create a template container using the db tuple for template
	 * @param t
	 * @return
	 * @throws Exception
	 */
	EPADTemplateContainer dbtemplate2Container(Template t) throws Exception;

	/**
	 * code to migrate the templates from files. called from main
	 * @throws Exception
	 */
	void migrateTemplates() throws Exception;

	/**
	 * extend getFrameDescriptions with booleans to trigger sending all the dicom tags and pixel data
	 * @param imageReference
	 * @param all
	 * @param pixelData
	 * @return
	 */
	EPADFrameList getFrameDescriptions(ImageReference imageReference, boolean all, boolean pixelData);

	/**
	 * extend getFrameDescriptions with booleans to trigger sending all the dicom tags and pixel data
	 * @param imageReference
	 * @param sessionID
	 * @param pixelData
	 * @return
	 */
	EPADFrame getFrameDescription(FrameReference frameReference, String sessionID, boolean pixelData);

	/**
	 * delete a specific file
	 * @param username
	 * @param fileId
	 * @throws Exception
	 */
	void deleteFile(String username, long fileId) throws Exception;

	/**
	 * delete template completely
	 * @param username
	 * @param templatecode
	 * @throws Exception
	 */
	void deleteTemplate(String username, String templatecode) throws Exception;

	/**
	 * get the number of active instances for the last ? number of days
	 * @param days
	 * @return
	 * @throws Exception
	 */
	int getActiveCount(int days) throws Exception;

	
	/**
	 * gets the summary for template stats
	 * @return
	 * @throws Exception
	 */
	EPADTemplateUsageList getTemplateStatSummary() throws Exception;

	
	/**
	 * gets the summary for template stats. contains also the xml
	 * @return
	 * @throws Exception
	 */
	EPADTemplateUsageList getTemplateStatSummaryWithXML() throws Exception;

	/**
	 * gets the monthly cumulative usage summary for the specified month
	 * @param month
	 * @return
	 * @throws Exception
	 */
	EPADUsageList getMonthlyUsageSummaryForMonth(int month) throws Exception;

	/**
	 * gets the monthly cumulative usage summary for all the months available
	 * @param month
	 * @return
	 * @throws Exception
	 */
	EPADUsageList getMonthlyUsageSummary() throws Exception;

	/**
	 * get dicom element for a specific tag code
	 * @param dicomElements
	 * @param tagName
	 * @return
	 */
	String getDICOMElement(DICOMElementList dicomElements, String tagName);

	/**
	 * get dicom elements for an image
	 * @param studyUID
	 * @param seriesUID
	 * @param imageUID
	 * @return
	 */
	DICOMElementList getDICOMElements(String studyUID, String seriesUID, String imageUID);

	/**
	 * get unique patient id for this name and id checking db 
	 * @param dicomPatientID
	 * @param dicomPatientName
	 * @return unique patient id (first 128 chars if longer)
	 */
	String getUniquePatientID(String dicomPatientID,String dicomPatientName);

	/**
	 * gets the image descriptions for flagged images only
	 * @param username
	 * @param studyReference
	 * @param sessionID
	 * @return
	 */
	EPADImageList getFlaggedImageDescriptions(String username, StudyReference studyReference, String sessionID);

	/**
	 * set the flagged property of an image
	 * @param username
	 * @param studyReference
	 * @param imageUID
	 * @param sessionID
	 * @param flag
	 */
	void setFlagged(String username, StudyReference studyReference, String imageUID, String sessionID,
			boolean flag);

	/**
	 * get image description using just projectid and image uid
	 * @param projectID
	 * @param imageUID
	 * @param sessionID
	 * @return
	 */
	EPADImage getImageDescription(String projectID, String imageUID, String sessionID);

	/**
	 * get series with flagged images
	 * @param studyReference
	 * @param username
	 * @param sessionID
	 * @param searchFilter
	 * @param filterDSOs
	 * @param includeAnnotationStatus
	 * @param getFlagged
	 * @return
	 */
	EPADSeriesList getSeriesDescriptions(StudyReference studyReference, String username, String sessionID,
			EPADSearchFilter searchFilter, boolean filterDSOs, boolean includeAnnotationStatus, boolean getFlagged);

	/**
	 * checks if there are old templates in the system
	 */
	void checkOldTemplates();

	/**
	 * created a file with study desc and patient name (used for tiffs to create the patient and study)
	 * @param username
	 * @param seriesReference
	 * @param uploadedFile
	 * @param description
	 * @param fileType
	 * @param sessionID
	 * @param convertToDICOM
	 * @param modality
	 * @param instanceNumber
	 * @param studyDescription
	 * @param patientName
	 * @param studyID
	 * @param seriesNumber
	 * @return
	 * @throws Exception
	 */
	int createFile(String username, SeriesReference seriesReference, File uploadedFile, String description,
			String fileType, String sessionID, boolean convertToDICOM, String modality, String instanceNumber,
			String studyDescription, String patientName, String studyID, String seriesNumber) throws Exception;
	
}
