package edu.stanford.epad.epadws.epaddb;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests all operations on ePAD's database
 * 
 */

public class EpadDatabaseOperationsTest
{
    @BeforeClass
    public static void oneTimeSetUp() {
       	if (EpadDatabase.getInstance().getStartupTime() == -1)
    		EpadDatabase.getInstance().startup("1");
     }
 
    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
    	System.out.println("@AfterClass - oneTimeTearDown");
    }
 
    @Before
    public void setUp() {
        System.out.println("@Before - setUp");
    }
 
    @After
    public void tearDown() {
         System.out.println("@After - tearDown");
    }
    
    @Test
    public void getAllSeriesUIDsFromEPadDatabaseTest() {
    	Set<String> uids = EpadDatabase.getInstance().getEPADDatabaseOperations().getAllSeriesUIDsFromEPadDatabase();
    	Assert.assertTrue(uids.size() > 0);
    }
//
//  Need to create Tests for these
//	Set<String> getImageUIDsInSeries(String seriesUID);
//
//	void deleteStudy(String studyUID);
//
//	void deleteSeries(String seriesUID);
//
//	void deleteSeriesOnly(String seriesUID);
//
//	SeriesProcessingStatus getSeriesProcessingStatus(String seriesUID);
//
//	void insertEpadEvent(String sessionID, String eventStatus, String aimUID, String aimName, String patientID,
//			String patientName, String templateID, String templateName, String pluginName);
//	
//	int deleteOldEvents();
//
//	Timestamp getSeriesProcessingDate(String seriesUID);
//
//	void updateOrInsertSeries(String seriesUID, SeriesProcessingStatus seriesProcessingStatus);
//
//	List<Map<String, String>> getEpadEventsForSessionID(String sessionID);
//
//	List<Map<String, String>> getEpadEventsForAimID(String sessionID);
//
//	void forceDICOMReprocessing();
//
//	// Database recording of PNG files generated from DICOM images
//
//	void insertEpadFileRow(Map<String, String> fileRecord);
//
//	void updateEpadFileRow(String filePath, PNGFileProcessingStatus newStatus, long fileSize, String errorMsg);
//
//	boolean hasEpadFileRow(String filePath);
//
//	List<String> getAllEPadFilePathsWithErrors();
//
//	List<String> getAllEPadInPipelineFilePaths();
//	
//	void deleteObsoleteEpadFileEntries();
//	
//	void checkAndRefreshAnnotationsTable();
//	
//	EPADAIM getAIM(String aimID);
//
//	EPADAIM getAIM(ProjectReference projectReference, String aimID);
//
//	EPADAIM getAIM(SubjectReference subjectReference, String aimID);
//
//	EPADAIM getAIM(StudyReference studyReference, String aimID);
//
//	EPADAIM getAIM(SeriesReference seriesReference, String aimID);
//
//	EPADAIM getAIM(ImageReference imageReference, String aimID);
//
//	EPADAIM getAIM(FrameReference frameReference, String aimID);
//
//	List<EPADAIM> getAIMs(ProjectReference projectReference);
//
//	List<EPADAIM> getAIMs(SubjectReference subjectReference);
//
//	List<EPADAIM> getAIMs(StudyReference studyReference);
//
//	List<EPADAIM> getAIMs(SeriesReference seriesReference);
//
//	List<EPADAIM> getAIMs(ImageReference imageReference);
//
//	List<EPADAIM> getAIMs(FrameReference frameReference);
//	
//	List<EPADAIM> getAIMs(String projectID, AIMSearchType aimSearchType, String value, int start, int count);
//
//	List<EPADAIM> getAIMs(String dsoSeriesUID);
//
//	int getNumberOfAIMs(String userName, ProjectReference reference);
//
//	int getNumberOfAIMs(String userName, SubjectReference reference);
//
//	int getNumberOfAIMs(String userName, StudyReference reference);
//
//	int getNumberOfAIMs(String userName, SeriesReference reference);
//
//	int getNumberOfAIMs(String userName, ImageReference reference);
//
//	int getNumberOfAIMs(String userName, FrameReference reference);
//
//	int getNumberOfAIMsForPatients(String projectID, Set<String> patientIDs, String userName);
//
//	int getNumberOfAIMsForSeriesSet(String projectID, Set<String> seriesIDs, String username);
//
//	int getNumberOfAIMsForSeries(String projectID, String seriesID, String username);
//
//	EPADAIM addAIM(String userName, ProjectReference reference, String aimID);
//
//	EPADAIM addAIM(String userName, SubjectReference reference, String aimID);
//
//	EPADAIM addAIM(String userName, StudyReference reference, String aimID);
//
//	EPADAIM addAIM(String userName, SeriesReference reference, String aimID);
//
//	EPADAIM addAIM(String userName, ImageReference reference, String aimID);
//
//	EPADAIM addAIM(String userName, FrameReference reference, String aimID);
//
//	EPADAIM addDSOAIM(String userName, ImageReference reference, String dsoSeriesUID, String aimID);
//	
//	EPADAIM updateAIM(String aimID, String projectID, String username);
//	
//	void deleteAIM(String userName, ProjectReference reference, String aimID);
//
//	void deleteAIM(String userName, SubjectReference reference, String aimID);
//
//	void deleteAIM(String userName, StudyReference reference, String aimID);
//
//	void deleteAIM(String userName, SeriesReference reference, String aimID);
//
//	void deleteAIM(String userName, ImageReference reference, String aimID);
//
//	void deleteAIM(String userName, FrameReference reference, String aimID);
//
//	void deleteAIM(String userName, String aimID);
//
//
//	Object insertDBObject(Object dbObject, String dbTable, String[][] dbColumns) throws Exception;
//	Object updateDBObject(Object dbObject, String dbTable, String[][] dbColumns) throws Exception;
//	int deleteDBObject(String dbTable, long id) throws Exception;
//	int deleteDBObjects(String dbTable, String criteria) throws Exception;
//	List getDBObjects(Class dbClass, String dbTable, String[][] dbColumns, String criteria, int startRecords, int maxRecords, boolean distinct) throws Exception;
//	List<Long> getDBIds(String dbTable, String criteria, int startRecords, int maxRecords) throws Exception;
//	int getDBCount(String dbTable, String criteria) throws Exception;	
//	boolean runSQLScript(String script);


}
