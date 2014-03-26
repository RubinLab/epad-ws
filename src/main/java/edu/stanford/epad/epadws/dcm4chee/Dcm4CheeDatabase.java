package edu.stanford.epad.epadws.dcm4chee;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.common.util.EPADTools;
import edu.stanford.epad.epadws.epaddb.ConnectionPool;
import edu.stanford.epad.epadws.epaddb.DatabaseState;

public class Dcm4CheeDatabase
{
	private static EPADLogger logger = EPADLogger.getInstance();

	private static final Dcm4CheeDatabase ourInstance = new Dcm4CheeDatabase();

	private ConnectionPool connectionPool;
	private Dcm4CheeDatabaseOperations dcm4CheeDatabaseOperations;

	private final AtomicReference<DatabaseState> dbState = new AtomicReference<DatabaseState>(DatabaseState.INIT);

	private long startupTime = -1;

	public static Dcm4CheeDatabase getInstance()
	{
		return ourInstance;
	}

	private Dcm4CheeDatabase()
	{
		initConnectionPool();
	}

	public void startup()
	{
		try {
			dbState.set(DatabaseState.STARTING);
			long time = System.currentTimeMillis();
			startupTime = System.currentTimeMillis() - time;
			dbState.set(DatabaseState.READY);
			logger.info("DCM4CHEE database took " + startupTime + " ms to start.");
		} catch (Exception e) {
			logger.severe("Failed to start DCM4CHEE database", e);
			dbState.set(DatabaseState.ERROR);
		}
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	public void shutdown()
	{
		dbState.set(DatabaseState.SHUTDOWN);
		long time = System.currentTimeMillis();
		logger.info("Shutting down database.");

		closeConnectionPool();
		logger.info("The database took " + (System.currentTimeMillis() - time) + " ms, to shutdown.");
	}

	public Dcm4CheeDatabaseOperations getDcm4CheeDatabaseOperations()
	{
		return this.dcm4CheeDatabaseOperations;
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
			logger.info("Creating connection pool");
			createConnectionPool();
			this.dcm4CheeDatabaseOperations = new DefaultDcm4CheeDatabaseOperations(connectionPool);
		} catch (Exception e) {
			logger.severe("Failed to create connection pool", e);
			dbState.set(DatabaseState.ERROR);
		}
	}

	private void createConnectionPool() throws SQLException
	{
		String username = EPADTools.dcm4CheeDatabaseUsername;
		String password = EPADTools.dcm4CheeDatabasePassword;
		String dcm4CheeDatabaseURL = EPADTools.dcm4CheeDatabaseURL;

		logger.info("MySql using connection string for DCM4CHEE database: " + dcm4CheeDatabaseURL);

		connectionPool = new ConnectionPool(dcm4CheeDatabaseURL, username, password);
	}

	private void closeConnectionPool()
	{
		connectionPool.dispose();
	}
}
