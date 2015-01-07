package edu.stanford.epad.epadws.service;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.stanford.epad.dtos.RemotePAC;
import edu.stanford.epad.epadws.epaddb.EpadDatabase;
import edu.stanford.epad.epadws.models.ProjectType;

public class RemotePACsServiceTest  {

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code   
    	System.out.println("@BeforeClass - oneTimeSetUp");
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
    public void getRemotePACsTest() {
		try {
	    	List<RemotePAC> pacs = RemotePACService.getInstance().getRemotePACs();
	    	Assert.assertTrue(pacs.size() > 1);
		} catch (Exception e) {
			Assert.fail();
		}
    }
//
//  Need to create Tests for these
//
//	public List<RemotePAC> getRemotePACs()
//
//	public RemotePAC getRemotePAC(String pacID)
//	
//	public void addRemotePAC(RemotePAC pac)
//	
//	public void modifyRemotePAC(RemotePAC pac)
//	
//	public void removeRemotePAC(RemotePAC pac)
//	
//	public synchronized List<RemotePACEntity> queryRemoteData(RemotePAC pac, String patientNameFilter, String patientIDFilter, String studyDateFilter)
//	
//	public synchronized void retrieveRemoteData(RemotePAC pac, String entityID, String projectID, String userName, String sessionID)
//	
}
