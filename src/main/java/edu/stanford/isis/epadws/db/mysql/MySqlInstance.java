package edu.stanford.isis.epadws.db.mysql;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import edu.stanford.isis.epad.common.FileKey;
import edu.stanford.isis.epad.common.ProxyFileUtils;
import edu.stanford.isis.epad.common.ProxyLogger;
import edu.stanford.isis.epadws.db.mysql.impl.DbState;
import edu.stanford.isis.epadws.db.mysql.impl.DbUtils;
import edu.stanford.isis.epadws.db.mysql.impl.MySqlQueriesImpl;

/**
 * @author amsnyder
 */
public class MySqlInstance
{

	private static ProxyLogger logger = ProxyLogger.getInstance();

	private static final String USER = "pacs";
	private static final String PWD = "pacs";

	private static MySqlInstance ourInstance = new MySqlInstance();

	private MySqlConnectionPool connectionPool;
	private MySqlQueries mySqlQueries;

	private final AtomicReference<DbState> dbState = new AtomicReference<DbState>(DbState.INIT);

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
			dbState.set(DbState.ERROR);
		}
	}

	public void startup()
	{
		try {
			dbState.set(DbState.STARTING);
			long time = System.currentTimeMillis();

			if (!tablesUpToDate()) {
				logger.info("IMPORTANT: NEED to add the epaddb tables using MySQL command-line!!");
				// updateMySqlTables();
			} else {
				logger.info("ePad's extra MySQL tables appear to be up to date.");
			}

			startupTime = System.currentTimeMillis() - time;

			dbState.set(DbState.READY);
			logger.info("Database took " + startupTime + " ms to start.");

		} catch (Exception e) {
			logger.sever("Failed to start-up database", e);
			dbState.set(DbState.ERROR);
		}
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	@SuppressWarnings("unused")
	private void updateMySqlTables()
	{
		try {

			// for now we are just creating the loading the first version.
			String sqlFilePath = FileKey.getCanonicalPath(new File("../etc/db/mysql/dcm4chee-extensions.sql"));
			File createDbTablesFile = new File(sqlFilePath);
			logger.info("Reading sql file: " + sqlFilePath);
			String content = ProxyFileUtils.read(createDbTablesFile);

			List<String> createCommands = DbUtils.parseCreateTable(content);

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
					DbUtils.close(s);
					DbUtils.close(conn);
				}
			}// for

		} catch (Exception e) {
			logger.warning("Failed to create extra my sql tables", e);
		}

	}

	private boolean tablesUpToDate()
	{
		// return true;

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
			DbUtils.close(rs);
			DbUtils.close(s);
			DbUtils.close(conn);
		}
		return result;

	}

	public void shutdown()
	{
		dbState.set(DbState.SHUTDOWN);
		long time = System.currentTimeMillis();
		logger.info("Shutting down database.");

		// flush data

		// close connection pool.
		closeConnectionPool();
		logger.info("The database took " + (System.currentTimeMillis() - time) + " ms, to shutdown.");
	}

	public MySqlQueries getMysqlQueries()
	{
		return mySqlQueries;
	}

	private void createConnectionPool() throws SQLException
	{
		// String localHostConnStr = "jdbc:mysql://localhost:3306/pacsdb"; //ToDo: old method.
		String localHostConnStr = "jdbc:mysql://localhost:3306?autoReconnect=true";
		// String localHostConnStr = "jdbc:mysql://localhost:3306";

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
		if (dbState.get() == DbState.READY || dbState.get() == DbState.STARTING) {
			Connection newConnection = connectionPool.getConnection();
			if (newConnection.isClosed()) {
				logger.info("MySqlInstance.getConnection returning closed connection");
			}
			return newConnection;
		}
		throw new IllegalStateException("Database Not Ready. It is in: " + dbState.get().name() + " state");
	}

}
