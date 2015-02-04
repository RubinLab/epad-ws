package edu.stanford.epad.epadws.epaddb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicReference;

import com.mongodb.MongoClient;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;

public class EpadDatabase
{
	private static EPADLogger log = EPADLogger.getInstance();

	private static final EpadDatabase ourInstance = new EpadDatabase();

	private ConnectionPool connectionPool;
	private MongoClient mongoClient;
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

	public void startup(String dbVersion)
	{
		try {
			databaseState.set(DatabaseState.STARTING);
			long time = System.currentTimeMillis();

			if (!tablesUpToDate(dbVersion, true)) {
				log.info("IMPORTANT: NEED to add the epaddb tables using MySQL command-line!!");
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

	public MongoClient getMongoConnection() throws Exception
	{
		if (mongoClient == null)
		{
			try {
				mongoClient = new MongoClient( "localhost" );
			} catch (Exception e) {
				log.warning("Error connecting to MongoDB", e);
				throw e;
			}
		}
		return mongoClient;
	}
	
	private boolean tablesUpToDate(String requiredVersion, boolean update)
	{
		boolean result = false;
		Connection conn = null;
		Statement s = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		String version = "";
		try {
			sb.append("Checking MySQL database. ");
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
				version = rs.getString("version");
				sb.append("Database version is: ").append(version).append(" ");
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
		if (getDouble(version) == getDouble(requiredVersion))
		{
			log.info("ePad's MySQL tables appear to be up to date.");
		}
		else if (getDouble(version) > getDouble(requiredVersion))
		{
			log.info("ePad's MySQL tables appear to be a new version, maybe OK.");
		}
		else if (update)
		{
			log.info("ePad's MySQL tables appear to be an older version, attempting to update");
			result = false;
			try
			{
				double reqdVersion = getDouble(requiredVersion);
				double nextVersion = getDouble(version) + 0.001;
				for (double vers = nextVersion; vers < (reqdVersion+0.001); vers = vers + 0.001)
				{
					String verstr = new DecimalFormat("#.###").format(vers);
					InputStream is = this.getClass().getClassLoader().getResourceAsStream("sql/epaddb" + verstr + ".sql");
					if (is != null)
					{
						log.info("Running script: epaddb" + verstr + ".sql");
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
				        String script = "";
		                String inputLine;
		                while ((inputLine = br.readLine()) != null)
		                {
		                	if (inputLine.startsWith("--")) continue;
		                	if (inputLine.startsWith("#")) continue;
		                	script = script + "\n" + inputLine;
		                }
		                br.close();
						log.info("Script:" + script + "\n -- end of script --");
						if (Math.abs(vers - reqdVersion) < 0.001)
							result = true;
						getEPADDatabaseOperations().runSQLScript(script);
					}
				}
			}
			catch (Exception x) {
				log.warning("Error updating db version", x);
			}
		}
		else
		{
			log.warning("ePad's MySQL tables appear to be an older version.");
			result = false;
		}
		return result;
	}
	
	private static double getDouble(String value)
	{
		try {
			return new Double(value.trim()).doubleValue();
		} catch (Exception x) {
			return 0;
		}
	}
}
