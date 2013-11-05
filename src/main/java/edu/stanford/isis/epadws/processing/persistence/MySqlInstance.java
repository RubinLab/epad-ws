package edu.stanford.isis.epadws.processing.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import edu.stanford.isis.epad.common.util.EPADFileUtils;
import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epad.common.util.FileKey;
import edu.stanford.isis.epad.common.util.ResourceUtils;
import edu.stanford.isis.epadws.processing.model.DatabaseState;

/**
 * @author amsnyder
 */
public class MySqlInstance
{
	private static EPADLogger logger = EPADLogger.getInstance();

	private static final String USER = "pacs";
	private static final String PWD = "pacs";

	private static final MySqlInstance ourInstance = new MySqlInstance();

	private MySqlConnectionPool connectionPool;
	private MySqlQueries mySqlQueries;

	private final AtomicReference<DatabaseState> dbState = new AtomicReference<DatabaseState>(DatabaseState.INIT);

	private long startupTime = -1;

	public static MySqlInstance getInstance()
	{
		return ourInstance;
	}

	private MySqlInstance()
	{
		initConnectionPool();
	}

	private void initConnectionPool()
	{
		try {
			logger.info("starting database.");
			// create the connection pool to the local mysql database.
			createConnectionPool();

			mySqlQueries = new MySqlQueriesImpl(connectionPool);
		} catch (Exception e) {
			logger.sever("Failed to init connection pool to database database", e);
			dbState.set(DatabaseState.ERROR);
		}
	}

	public void startup()
	{
		try {
			dbState.set(DatabaseState.STARTING);
			long time = System.currentTimeMillis();

			if (!tablesUpToDate()) {
				logger.info("IMPORTANT: NEED to add the epaddb tables using MySQL command-line!!");
				// updateMySqlTables();
			} else {
				logger.info("ePad's extra MySQL tables appear to be up to date.");
			}
			startupTime = System.currentTimeMillis() - time;

			dbState.set(DatabaseState.READY);
			logger.info("Database took " + startupTime + " ms to start.");
		} catch (Exception e) {
			logger.sever("Failed to start-up database", e);
			dbState.set(DatabaseState.ERROR);
		}
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	@SuppressWarnings("unused")
	private void updateMySqlTables()
	{
		try { // For now we are just creating the loading the first version.
			String mySQLScriptDir = ResourceUtils.getEPADWebServerMySQLScriptDir();
			String sqlFilePath = FileKey.getCanonicalPath(new File(mySQLScriptDir + "dcm4chee-extensions.sql"));
			File createDbTablesFile = new File(sqlFilePath);
			logger.info("Reading sql file: " + sqlFilePath);
			String content = EPADFileUtils.read(createDbTablesFile);

			List<String> createCommands = DatabaseUtils.parseCreateTable(content);

			for (String currCreateTableCmd : createCommands) {
				logger.info("executing: " + currCreateTableCmd);
				Connection conn = null;
				String sqlCmd = currCreateTableCmd.trim();
				if (sqlCmd.length() == 0) {
					continue;
				}
				if (sqlCmd.equals(";")) {
					continue;
				}
				if (sqlCmd.startsWith("//")) {
					continue;
				}
				if (sqlCmd.startsWith("--")) {
					continue;
				}
				Statement s = null;
				try {
					MySqlInstance mySqlInstance = MySqlInstance.getInstance();
					conn = mySqlInstance.getConnection();
					s = conn.createStatement();

					if (sqlCmd.indexOf("CREATE DATABASE") > -1) {
						s.executeUpdate(sqlCmd);
					} else {
						s.execute(sqlCmd);
					}
				} catch (SQLException sqle) {
					if (sqlCmd.toUpperCase().startsWith("DROP")) {
						logger.info("Not dropping table: " + currCreateTableCmd);
					} else {
						logger.warning("Failed command: " + currCreateTableCmd, sqle);
					}
				} catch (Exception e) {
					logger.warning("Failed to create database.", e);
				} finally {
					DatabaseUtils.close(s);
					DatabaseUtils.close(conn);
				}
			}
		} catch (Exception e) {
			logger.warning("Failed to create extra MySQL tables", e);
		}
	}

	private boolean tablesUpToDate()
	{
		boolean result = false;
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("Checking MySQL database. ");
			conn = connectionPool.getConnection();
			s = conn.createStatement();

			try {
				rs = s.executeQuery("SELECT version from epaddb.dbversion");
			} catch (Exception e) {
				logger.info("select version from epaddb.dbversion had: " + e.getMessage());
				// assume we don't have the table in this case and add them.
				return false;
			}
			if (rs.next()) {
				result = true;
				sb.append("DB Version is: ").append(rs.getString("version")).append(" ");
			}
		} catch (SQLException sqle) {
			logger.warning("SQL Error when checking for db version.", sqle);
			result = false;
		} finally {
			logger.info(sb.toString());
			DatabaseUtils.close(rs);
			DatabaseUtils.close(s);
			DatabaseUtils.close(conn);
		}
		return result;
	}

	public void shutdown()
	{
		dbState.set(DatabaseState.SHUTDOWN);
		long time = System.currentTimeMillis();
		logger.info("Shutting down database.");

		closeConnectionPool();
		logger.info("The database took " + (System.currentTimeMillis() - time) + " ms, to shutdown.");
	}

	public MySqlQueries getMysqlQueries()
	{
		return mySqlQueries;
	}

	private void createConnectionPool() throws SQLException
	{
		String localHostConnStr = "jdbc:mysql://localhost:3306?autoReconnect=true";

		logger.info("MySql using connection string: " + localHostConnStr);

		connectionPool = new MySqlConnectionPool(localHostConnStr, USER, PWD);
	}

	private void closeConnectionPool()
	{
		connectionPool.dispose();
	}

	public int getConnectionPoolAvailCount()
	{
		return connectionPool.availableConnectionCount();
	}

	public int getConnectionPoolUsedCount()
	{
		return connectionPool.usedConnectionCount();
	}

	public Connection getConnection() throws SQLException
	{
		if (dbState.get() == DatabaseState.READY || dbState.get() == DatabaseState.STARTING) {
			Connection newConnection = connectionPool.getConnection();
			if (newConnection.isClosed()) {
				logger.info("MySqlInstance.getConnection returning closed connection");
			}
			return newConnection;
		}
		throw new IllegalStateException("Database Not Ready. It is in: " + dbState.get().name() + " state");
	}
}