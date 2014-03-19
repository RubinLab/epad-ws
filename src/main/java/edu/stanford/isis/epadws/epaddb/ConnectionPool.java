package edu.stanford.isis.epadws.epaddb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.isis.epad.common.util.EPADLogger;
import edu.stanford.isis.epadws.processing.pipeline.threads.ShutdownSignal;

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
	private final String userName;
	private final String userPassword;

	private int initialConnections = 5;

	public ConnectionPool(String url, String userName, String userPass) throws SQLException
	{
		this.connectionUrl = url;
		this.userName = userName;
		this.userPassword = userPass;

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
		return DriverManager.getConnection(connectionUrl, userName, userPassword);
	}

	public synchronized Connection getConnection() throws SQLException
	{
		Connection newConnection = null;
		if (connectionsAvailable.size() == 0) {
			newConnection = createConnection();
			connectionsUsed.add(newConnection);
		} else {
			int size = connectionsAvailable.size();
			newConnection = connectionsAvailable.get(size - 1);
			connectionsAvailable.remove(newConnection);
			connectionsUsed.add(newConnection);
		}
		return newConnection;
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