package edu.stanford.epad.epadws.epaddb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.processing.pipeline.threads.ShutdownSignal;

/**
 * 
 * @author amsnyder
 */
public class ConnectionPool implements Runnable
{
	private static final EPADLogger logger = EPADLogger.getInstance();

	private final List<Connection> connectionsAvailable = Collections.synchronizedList(new ArrayList<Connection>());
	private final List<Connection> connectionsUsed = Collections.synchronizedList(new ArrayList<Connection>());

	private final String connectionUrl;
	private final String username;
	private final String password;

	private int initialConnections = 5;

	public ConnectionPool(String connectionUrl, String username, String password) throws SQLException
	{
		this.connectionUrl = connectionUrl;
		this.username = username;
		this.password = password;

		logger.info("Creating connection pool for URL " + connectionUrl);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			for (int count = 0; count < initialConnections; count++) {
				connectionsAvailable.add(createConnection());
			}
		} catch (ClassNotFoundException e) {
			logger.warning(e.toString(), e);
		}
	}

	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection(connectionUrl, username, password);
	}

	public synchronized Connection getConnection() throws SQLException
	{
		if (connectionsAvailable.size() == 0) {
			Connection connection = createConnection();
			connectionsUsed.add(connection);
			return connection;
		} else {
			int size = connectionsAvailable.size();
			Connection connection = connectionsAvailable.get(size - 1);
			connectionsAvailable.remove(connection);
			connectionsUsed.add(connection);
			return connection;
		}
	}

	public synchronized void freeConnection(Connection connection)
	{
		connectionsUsed.remove(connection);
		connectionsAvailable.add(connection);
	}

	public int availableConnectionCount()
	{
		return connectionsAvailable.size();
	}

	public int usedConnectionCount()
	{
		return connectionsUsed.size();
	}

	@Override
	public void run()
	{
		try {
			ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
			while (!shutdownSignal.hasShutdown()) {
				synchronized (this) {
					closeExcessConnections();
				}
				Thread.sleep(10);
			}
		} catch (SQLException sqle) {
			logger.severe("SQL Exception. Lost the connection pool.", sqle);
			sqle.printStackTrace();
		} catch (Exception e) {
			logger.severe("Lost MySQL connection pool! ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Private method to close excess connections.
	 * 
	 * @throws SQLException
	 */
	private void closeExcessConnections() throws SQLException
	{
		int caSize = connectionsAvailable.size();
		while (caSize > initialConnections) {
			Connection connection = connectionsAvailable.get(caSize - 1);
			connectionsAvailable.remove(connection);
			connection.close();
			caSize = connectionsAvailable.size();
		}
	}

	/**
	 * Call during shutdown to get rid of all the connections.
	 */
	public void dispose()
	{
		try {
			synchronized (this) {
				logger.info("Shutting down mysql database connection pool. #avail: " + connectionsAvailable.size() + " #used: "
						+ connectionsUsed.size());
				initialConnections = 0;
				closeExcessConnections();
			}
		} catch (SQLException sqle) {
			logger.severe("Failed to dispose of connections.", sqle);
		}
	}
}