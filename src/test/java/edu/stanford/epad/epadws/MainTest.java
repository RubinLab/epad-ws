package edu.stanford.epad.epadws;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.stanford.epad.epadws.epaddb.EpadDatabaseOperationsTest;
import edu.stanford.epad.epadws.service.EpadProjectOperationsTest;

@RunWith(Suite.class)
@SuiteClasses({ EpadDatabaseOperationsTest.class, EpadProjectOperationsTest.class })
public class MainTest {

}