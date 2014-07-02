package edu.stanford.epad.epadws.epaddb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicReference;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

public class EpadDatabase
{
	private static EPADLogger log = EPADLogger.getInstance();

	private static final EpadDatabase ourInstance = new EpadDatabase();

	private ConnectionPool connectionPool;
	private EpadDatabaseOperations epadDatabaseOperations;

	private final AtomicReference<DatabaseState> databaseState = new AtomicReference<DatabaseState>(DatabaseState.INIT);

	private long startupTime = -1;

	public static EpadDatabase getInstance()
	{
		return ourInstance;
	}

	private EpadDatabase()
	{
		initConnectionPool();
	}

	public void startup()
	{
		try {
			databaseState.set(DatabaseState.STARTING);
			long time = System.currentTimeMillis();

			if (!tablesUpToDate()) {
				log.info("IMPORTANT: NEED to add the epaddb tables using MySQL command-line!!");
			} else {
				log.info("ePad's extra MySQL tables appear to be up to date.");
			}
			startupTime = System.currentTimeMillis() - time;
			databaseState.set(DatabaseState.READY);
			log.info("Database took " + startupTime + " ms to start.");
		} catch (Exception e) {
			log.severe("Failed to start-up database", e);
			databaseState.set(DatabaseState.ERROR);
		}
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	public void shutdown()
	{
		databaseState.set(DatabaseState.SHUTDOWN);
		long time = System.currentTimeMillis();
		log.info("Shutting down database.");

		closeConnectionPool();
		log.info("The database took " + (System.currentTimeMillis() - time) + " ms, to shutdown.");
	}

	public EpadDatabaseOperations getEPADDatabaseOperations()
	{
		return epadDatabaseOperations;
	}

	public int getConnectionPoolAvailCount()
	{
		return connectionPool.availableConnectionCount();
	}

	public int getConnectionPoolUsedCount()
	{
		return connectionPool.usedConnectionCount();
	}

	private void initConnectionPool()
	{
		try {
			log.info("Creating connection pool.");
			createConnectionPool();
			epadDatabaseOperations = new DefaultEpadDatabaseOperations(connectionPool);
		} catch (Exception e) {
			log.severe("Failed to create connection pool", e);
			databaseState.set(DatabaseState.ERROR);
		}
	}

	private void createConnectionPool() throws SQLException
	{
		String username = EPADConfig.epadDatabaseUsername;
		String password = EPADConfig.epadDatabasePassword;
		String epadDatabaseURL = EPADConfig.epadDatabaseURL;

		log.info("MySql using connection string for ePAD database: " + epadDatabaseURL);

		connectionPool = new ConnectionPool(epadDatabaseURL, username, password);
	}

	private void closeConnectionPool()
	{
		connectionPool.dispose();
	}

	private boolean tablesUpToDate()
	{
		boolean result = false;
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("Checking MySQL database.");
			conn = connectionPool.getConnection();
			s = conn.createStatement();

			try {
				rs = s.executeQuery("SELECT version FROM epaddb.dbversion");
			} catch (Exception e) {
				log.info("SELECT version FROM epaddb.dbversion had error: " + e.getMessage());
				return false;
			}
			if (rs.next()) {
				result = true;
				sb.append("Database version is: ").append(rs.getString("version")).append(" ");
			}
		} catch (SQLException sqle) {
			log.warning("SQL error when checking for database version", sqle);
			result = false;
		} finally {
			log.info(sb.toString());
			DatabaseUtils.close(rs);
			DatabaseUtils.close(s);
			DatabaseUtils.close(conn);
		}
		return result;
	}
}
