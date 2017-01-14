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
package edu.stanford.epad.epadws.epaddb;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.epad.dtos.AnnotationStatus;
import edu.stanford.epad.dtos.EPADAIM;
import edu.stanford.epad.dtos.EPADDataList;
import edu.stanford.epad.dtos.PNGFileProcessingStatus;
import edu.stanford.epad.dtos.SeriesProcessingStatus;
import edu.stanford.epad.epadws.aim.AIMSearchType;
import edu.stanford.epad.epadws.handlers.coordination.CoordinationHandler;
import edu.stanford.epad.epadws.handlers.coordination.Term;
import edu.stanford.epad.epadws.handlers.core.FrameReference;
import edu.stanford.epad.epadws.handlers.core.ImageReference;
import edu.stanford.epad.epadws.handlers.core.ProjectReference;
import edu.stanford.epad.epadws.handlers.core.SeriesReference;
import edu.stanford.epad.epadws.handlers.core.StudyReference;
import edu.stanford.epad.epadws.handlers.core.SubjectReference;
import edu.stanford.epad.epadws.models.EpadStatisticsTemplate;

/**
 * Defines all operations on ePAD's database
 * 
 * 
 * @author martin
 */

public interface EpadDatabaseOperations
{
	int getFreeConnections();
	
	int getUsedConnections();
	
	String getPNGLocation(String studyUID, String seriesUID, String imageUID);

	String getPNGLocation(ImageReference imageReference);

	String getPNGLocation(FrameReference frameReference);

	String getJPGLocation(FrameReference frameReference);
	
	List<String> getAllPNGLocations(String imageUID);

	Set<String> getAllSeriesUIDsFromEPadDatabase();

	Set<String> getImageUIDsInSeries(String seriesUID);

	void deleteStudy(String studyUID);

	void deleteSeries(String seriesUID);

	void deleteSeriesOnly(String seriesUID);

	SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID);

	void insertEpadEvent(String sessionID, String eventStatus, String aimUID, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName);

	void insertEpadEvent(String sessionID, String eventStatus, String aimUID, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName, 
			String projectID, String projectName, String studyUID, String seriesUID, boolean error);
	public void insertEpadEvent(String sessionID, String message, String name, String target);
	
	int deleteOldEvents();

	Timestamp getSeriesProcessingDate(String seriesUID);

	void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus);

	void updateSeriesDefaultTags(String seriesUID, String defaultTags) throws Exception;

	String getSeriesDefaultTags(String seriesUID);
	
	boolean hasSeriesInEPadDatabase(String seriesIUID);
	
	boolean hasStudyInDCM4CHE(String studyIUID);

	List<Map<String, String>> getEpadEventsForSessionID(String sessionID, boolean delete);

	List<Map<String, String>> getEpadEventsForAimID(String sessionID);

	void forceDICOMReprocessing();

	// Database recording of PNG files generated from DICOM images

	void insertEpadFileRow(Map<String, String> fileRecord);

	void updateEpadFileRow(String filePath, PNGFileProcessingStatus newStatus, long fileSize, String errorMsg);

	boolean hasEpadFileRow(String filePath);

	List<String> getAllEPadFilePathsWithErrors();

	List<String> getAllEPadInPipelineFilePaths();
	
	void deleteObsoleteEpadFileEntries();
	
	void checkAndRefreshAnnotationsTable();
	
	EPADAIM getAIM(String aimID);

	EPADAIM getAIM(ProjectReference projectReference, String aimID);

	EPADAIM getAIM(SubjectReference subjectReference, String aimID);

	EPADAIM getAIM(StudyReference studyReference, String aimID);

	EPADAIM getAIM(SeriesReference seriesReference, String aimID);

	EPADAIM getAIM(ImageReference imageReference, String aimID);

	EPADAIM getAIM(FrameReference frameReference, String aimID);

	List<EPADAIM> getAIMs(ProjectReference projectReference);

	List<EPADAIM> getAIMs(SubjectReference subjectReference);

	List<EPADAIM> getAIMs(StudyReference studyReference);

	List<EPADAIM> getAIMs(SeriesReference seriesReference);

	List<EPADAIM> getAIMs(ImageReference imageReference);

	List<EPADAIM> getAIMs(FrameReference frameReference);

	List<EPADAIM> getAIMs(String projectID, String subjectID, String studyUID, String seriesUID);
	
	List<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value, int start, int count);

	List<EPADAIM> getAIMsByDSOSeries(String dsoSeriesUID);

	List<EPADAIM> getAIMsByDSOSeries(String projectID, String dsoSeriesUID);

	List<EPADAIM> getAIMsByDSOSeries(String projectID, String patientID, String dsoSeriesUID);

	List<EPADAIM> getAIMsByQuery(String sqlQuery);

	void addProjectToAIM(String projectID, String aimID);

	void removeProjectFromAIM(String projectID, String aimID);
	
	List<EPADAIM> getSharedAIMs(String projectID, String patientID, String seriesUID);

	int getNumberOfAIMs(String userName, ProjectReference reference);

	int getNumberOfAIMs(String userName, SubjectReference reference);

	int getNumberOfAIMs(String userName, StudyReference reference);

	int getNumberOfAIMs(String userName, SeriesReference reference);

	int getNumberOfAIMs(String userName, ImageReference reference);

	int getNumberOfAIMs(String userName, FrameReference reference);

	int getNumberOfAIMsForPatients(String projectID, Set<String> patientIDs, String userName);

	int getNumberOfAIMsForSeriesSet(String projectID, Set<String> seriesIDs, String username);

	int getNumberOfAIMsForSeries(String projectID, String seriesID, String username);

	int getNumberOfAIMs(String criteria);

	EPADAIM addAIM(String userName, ProjectReference reference, String aimID);

	EPADAIM addAIM(String userName, SubjectReference reference, String aimID);

	EPADAIM addAIM(String userName, StudyReference reference, String aimID);

	EPADAIM addAIM(String userName, SeriesReference reference, String aimID);

	EPADAIM addAIM(String userName, ImageReference reference, String aimID);

	EPADAIM addAIM(String userName, FrameReference reference, String aimID);

	EPADAIM addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID);

	EPADAIM addAIM(String userName, FrameReference reference, String aimID, String aimXML, String aimName);

	EPADAIM addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID, String aimXML, String name);
	
	EPADAIM updateAIM(String aimID, String projectID, String username);
	
	EPADAIM updateAIMDSOSeries(String aimID, String seriesUID, String username);
	
	EPADAIM updateAIMXml(String aimID, String xml);
	
	EPADAIM updateAIMName(String aimID, String name);
	
	EPADAIM updateAIMDSOFrameNo(String aimID, int frameNo);
	
	EPADAIM updateAIMColor(String aimID, String color);
	
	EPADAIM updateAIMTemplateCode(String aimID, String templateCode);
	
	void deleteAIM(String userName, ProjectReference reference, String aimID);

	void deleteAIM(String userName, SubjectReference reference, String aimID);

	void deleteAIM(String userName, StudyReference reference, String aimID);

	void deleteAIM(String userName, SeriesReference reference, String aimID);

	void deleteAIM(String userName, ImageReference reference, String aimID);

	void deleteAIM(String userName, FrameReference reference, String aimID);

	void deleteAIM(String userName, String aimID);

	// Coordination methods; will disappear with AIM 4

	/**
	 * Return the key of a {@link Term}.
	 * 
	 * @param term
	 * @return The key of the term or -1 if it is not recorded
	 * @see CoordinationHandler
	 */
	int getKeyForTerm(Term term) throws SQLException;

	/**
	 * 
	 * @param termKeys
	 * @return The {@link Term} representing the coordination or null if it does not exist.
	 * @see CoordinationHandler
	 */
	Term getCoordinationTerm(List<Integer> termKeys) throws SQLException;

	/**
	 * Insert a term and return its new ID.
	 * 
	 * @param term
	 * @return The new key of the term
	 * @see CoordinationHandler
	 */
	int insertTerm(Term term) throws SQLException;

	/**
	 * Record a coordination term.
	 * 
	 * @param coordinationTermID
	 * @param schemaName
	 * @param schemaVersion
	 * @param description
	 * @param termIDs
	 * @return A {@link Term} representing the new coordination
	 * @see CoordinationHandler
	 */
	Term insertCoordinationTerm(String termIDPrefix, String schemaName, String schemaVersion, String description,
			List<Integer> termKeys) throws SQLException;
	
	/**
	 * Get all coordination data for coordinationID (eg: for "EPAD-prod-1" or "EPAD-prod-7" etc)
	 * @param coordinationID
	 * @return
	 */
	List<Map<String, String>> getCoordinationData(String coordinationID) throws Exception;
	

	EPADDataList getEpadHostNames();

	int getNumberOfSeries();
	
	/* 
	 * New DB Operations methods for schema to replace XNAT
	 * 
	 * Structure of dbColumns array in all methods below:
	 * fieldName,fieldType,columnName,columnType
	 * eg:
	 * 	{"name","String","name","varchar"},
	 *	{"numOfErrors","int","num_of_errors","integer"},
	 */
	Object insertDBObject(Object dbObject, String dbTable, String[][] dbColumns) throws Exception;
	Object updateDBObject(Object dbObject, String dbTable, String[][] dbColumns) throws Exception;
	int deleteDBObject(String dbTable, long id) throws Exception;
	int deleteDBObjects(String dbTable, String criteria) throws Exception;
	List getDBObjects(Class dbClass, String dbTable, String[][] dbColumns, String criteria, int startRecords, int maxRecords, boolean distinct) throws Exception;
	List<Long> getDBIds(String dbTable, String criteria, int startRecords, int maxRecords) throws Exception;
	int getDBCount(String dbTable, String criteria) throws Exception;	
	Object retrieveObjectById(Object dbObject, long id, String dbTable, String[][] dbColumns) throws Exception;
	/**
	 * @param script
	 * @return true on success
	 */
	boolean runSQLScript(String script);

	String getDBVersion();

	/**
	 * Faster annotation count using database join
	 * @param projectID
	 * @param studyUID
	 * @param username
	 * @return aim count
	 * @author emelalkim
	 */
	int getAIMCount(String projectID, String studyUID, String username);

	/**
	 * Gets all the parameters and returns the annotation status for that specific user and series
	 * @param projectuid
	 * @param subjectuid
	 * @param studyuid
	 * @param seriesuid
	 * @param username
	 * @return annotation status see AnnotationStatus class for values or null
	 * @author emelalkim
	 */
	AnnotationStatus getAnnotationStatusForUser(String projectUID, String subjectUID, String studyUID,
			String series_uid, String username);

	/**
	 * Gets the number of users that are done annotating the specific series for the project
	 * @param projectUID
	 * @param subjectUID
	 * @param studyUID
	 * @param series_uid
	 * @return count or 0
	 * @author emelalkim
	 */
	int getAnnotationDoneUserCount(String projectUID, String subjectUID, String studyUID, String series_uid);

	/**
	 * hold pixel values for paramteric maps
	 * @param filePath
	 * @param frameNum
	 * @param pixelValues
	 */
	void insertPixelValues(String filePath, int frameNum, String pixelValues, String imageUID);

	/**
	 * get pixel values for the image
	 * implemented for parametric maps where the pixel values can be float
	 * @param imageUID
	 * @return
	 */
	Map<String, String> getPixelValues(String imageUID);

	/**
	 * get pixel values for a specific png
	 * @param pngPath
	 * @return
	 */
	String getPixelValuesForPng(String pngPath);

	/**
	 * method for populating template statistics from db
	 * @return
	 */
	List<EpadStatisticsTemplate> getTemplateStats();

	/**
	 * adding a dicomsr aim
	 * @param userName
	 * @param reference
	 * @param aimID
	 * @param aimXML
	 * @param aimName
	 * @param isDicomSR
	 * @return
	 */
	EPADAIM addAIM(String userName, FrameReference reference, String aimID, String aimXML, String aimName,
			boolean isDicomSR);

	/**
	 * calculates the monthly cumulatives from epadstatistics table getting just the latest and puts it to epadstatistics_monthly
	 */
	void calcMonthlyCumulatives();

	

}
