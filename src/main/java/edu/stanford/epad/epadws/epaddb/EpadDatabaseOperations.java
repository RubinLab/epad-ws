//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
package edu.stanford.epad.epadws.epaddb;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * Defines all operations on ePAD's database
 * 
 * 
 * @author martin
 */

public interface EpadDatabaseOperations
{
	String getPNGLocation(String studyUID, String seriesUID, String imageUID);

	String getPNGLocation(ImageReference imageReference);

	String getPNGLocation(FrameReference frameReference);

	String getJPGLocation(FrameReference frameReference);

	Set<String> getAllSeriesUIDsFromEPadDatabase();

	Set<String> getImageUIDsInSeries(String seriesUID);

	void deleteStudy(String studyUID);

	void deleteSeries(String seriesUID);

	void deleteSeriesOnly(String seriesUID);

	SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID);

	void insertEpadEvent(String sessionID, String eventStatus, String aimUID, String aimName, String patientID,
			String patientName, String templateID, String templateName, String pluginName);
	
	int deleteOldEvents();

	Timestamp getSeriesProcessingDate(String seriesUID);

	void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus);
	
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

	EPADAIM addAIM(String userName, FrameReference reference, String aimID, String aimXML);

	EPADAIM addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID, String aimXML);
	
	EPADAIM updateAIM(String aimID, String projectID, String username);
	
	EPADAIM updateAIMXml(String aimID, String xml);
	
	EPADAIM updateAIMDSOFrameNo(String aimID, int frameNo);
	
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

}
