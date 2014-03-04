package edu.stanford.isis.tools;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.epaddb.EpadDatabase;
import edu.stanford.isis.epadws.queries.EpadQueries;

/**
 * A connection test for DCM4CHEE MySQL database.
 * 
 * @author alansnyder Date: 9/14/12
 */
public class DcmDbTester
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	static final String mysqlConn = "jdbc:mysql://epad-devu.stanford.edu:3306/pacsdb";
	static final String mysqlConnR = "jdbc:mysql://rufus.stanford.edu:3306/pacsdb";
	static final String user = "pacs";
	static final String pw = "pacs";

	public static void main(String[] args)
	{
		try {
			EpadDatabase mySqlInstance = EpadDatabase.getInstance();
			mySqlInstance.startup();

			logConnectionStatus();

			doSqlTest2();
			doSqlTest3();
			doSqlTest4();
			doSqlTest5();
			logConnectionStatus();
			doSqlTest6();
			doSqlTest7();
			logConnectionStatus();
			doSqlTest8();

		} catch (Exception e) {
			logger.warning("  ", e);
			e.printStackTrace();
		} finally {
			EpadDatabase.getInstance().shutdown();
		}
	}

	/**
	 * Try to get a list of all the studies using the MySqlInstance facilities.
	 */
	private static void doSqlTest2()
	{
		try {
			logger.info("######## Start test #2 - basic search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations.dicomStudySearch("patientName", "*");

			String[] keys = { "study_iuid", "pat_id", "modality", "study_datetime", "pat_name" };
			StringBuilder sb = new StringBuilder("Study Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {
				sb.append("#").append(resultIndex).append(" ");
				sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
				sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
				sb.append("modality=").append(currResult.get(keys[2])).append(",");
				sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
				sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

				resultIndex++;
				// to get keys from a database call.
				// printKeys(currResult.keySet());
			}
			logger.info(sb.toString());
			logger.info("######## End test #2 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest2 had: ", e);
		}
	}

	/**
	 * Do wild-card searches work?
	 * 
	 * @param con
	 */
	private static void doSqlTest3()
	{
		try {
			logger.info("######## Start test #3 - wildcard search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations.dicomStudySearch("patientName", "A*");

			String[] keys = { "study_iuid", "pat_id", "modality", "study_datetime", "pat_name" };
			StringBuilder sb = new StringBuilder("Study Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {

				sb.append("#").append(resultIndex).append(" ");
				sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
				sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
				sb.append("modality=").append(currResult.get(keys[2])).append(",");
				sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
				sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

				resultIndex++;
			}
			logger.info(sb.toString());
			logger.info("######## End test #3 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest3", e);
		}
	}

	/**
	 * Are searches case-insensitive?
	 * 
	 * @param con
	 */
	private static void doSqlTest4()
	{
		try {
			logger.info("######## Start test #4 - case insensitive search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> resultsUpperCase = databaseOperations.dicomStudySearch("patientName", "A*");
			List<Map<String, String>> resultsLowerCase = databaseOperations.dicomStudySearch("patientName", "a*");

			if (resultsUpperCase.size() != resultsLowerCase.size()) {
				logger.info("FAILED: Case insensitive search test." + " upper-case=" + resultsUpperCase.size() + " lower-case="
						+ resultsLowerCase.size());
			} else {
				logger.info("PASSES (likely) upper-case and lower-case search had same result. #" + resultsLowerCase.size());
			}
			logger.info("######## End test #4 - case insensitive search ########");
		} catch (Exception e) {
			logger.warning("doSqlTest4", e);
		}
	}

	/**
	 * Test patientId search.
	 */
	private static void doSqlTest5()
	{
		try {
			logger.info("######## Start test #5 - patient id search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations.dicomStudySearch("patientId", "2228*");
			String[] keys = { "study_iuid", "pat_id", "modality", "study_datetime", "pat_name" };
			StringBuilder sb = new StringBuilder("Study Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {
				sb.append("#").append(resultIndex).append(" ");
				// sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
				sb.append("studyId=").append(currResult.get(keys[0])).append(",");
				sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
				sb.append("modality=").append(currResult.get(keys[2])).append(",");
				sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
				sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

				resultIndex++;
			}
			logger.info(sb.toString());
			logger.info("######## End test #5 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest5", e);
		}
	}

	private static void doSqlTest6()
	{
		try {
			logger.info("######## Start test #6 - exam-type search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations.dicomStudySearch("examType", "DX");

			String[] keys = { "study_iuid", "pat_id", "modality", "study_datetime", "pat_name" };
			StringBuilder sb = new StringBuilder("Study Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {

				sb.append("#").append(resultIndex).append(" ");
				sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
				sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
				sb.append("modality=").append(currResult.get(keys[2])).append(",");
				sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
				sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

				resultIndex++;
			}
			logger.info(sb.toString());
			logger.info("######## End test #6 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest6", e);
		}
	}

	private static void doSqlTest7()
	{
		try {
			logger.info("######## Start test #7 - study-time search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations.dicomStudySearch("studyDate", "2002");

			String[] keys = { "study_iuid", "pat_id", "modality", "study_datetime", "pat_name" };
			StringBuilder sb = new StringBuilder("Study Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {

				sb.append("#").append(resultIndex).append(" ");
				sb.append("studyId=").append(truncate(currResult.get(keys[0]))).append(",");
				sb.append("patId=").append(truncate(currResult.get(keys[1]))).append(",");
				sb.append("modality=").append(currResult.get(keys[2])).append(",");
				sb.append("study_datetime=").append(currResult.get(keys[3])).append(",");
				sb.append("pat_name=").append(currResult.get(keys[4])).append("\n");

				resultIndex++;
			}// for
			logger.info(sb.toString());
			logger.info("######## End test #7 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest7", e);
		}
	}

	/**
	 * Test a get"series call".
	 */
	private static void doSqlTest8()
	{
		try {
			logger.info("######## Start test #8 - study-time search ########");

			EpadQueries databaseOperations = EpadDatabase.getInstance().getDatabaseOperations();
			List<Map<String, String>> results = databaseOperations
					.findAllDicomSeriesInStudy("1.2.826.0.1.3680043.8.420.30757817405477639080180001130587461759");

			// String[] keys = {"study_iuid", "pat_id", "modality", "study_datetime", "pat_name"};
			StringBuilder sb = new StringBuilder("Series Results \n");
			int resultIndex = 1;
			for (Map<String, String> currResult : results) {

				sb.append("#").append(resultIndex).append(" ");
				sb.append(printKeys(currResult.keySet()));
				resultIndex++;

			}// for
			logger.info(sb.toString());
			logger.info("######## End test #8 ########");
		} catch (Exception e) {
			logger.warning("doSqlTest8", e);
		}
	}

	private static String printKeys(Set<String> keySet)
	{
		StringBuilder sb = new StringBuilder();
		for (String currKey : keySet) {
			sb.append(currKey).append(", ");
		}
		return sb.toString();
	}

	/**
	 * Make a long string a short string so it displays better.
	 * 
	 * @param longString
	 * @return
	 */
	private static String truncate(String longString, int maxLen)
	{

		if (longString.length() < maxLen) {
			return longString;
		}

		// Shorten a long string.
		StringBuilder sb = new StringBuilder();
		sb.append(longString.substring(0, ((maxLen / 2) - 3)));
		sb.append("...");
		int endIndex = longString.length() - (maxLen / 2) + 3;
		sb.append(longString.substring(endIndex));

		return sb.toString();
	}

	private static String truncate(String longString)
	{
		return truncate(longString, 20);
	}

	private static void logConnectionStatus()
	{
		StringBuilder sb = new StringBuilder("Connection status - ");

		sb.append(" #avail: ").append(EpadDatabase.getInstance().getConnectionPoolAvailCount());
		sb.append(" #used: ").append(EpadDatabase.getInstance().getConnectionPoolUsedCount());

		logger.info(sb.toString());
	}

}// class
