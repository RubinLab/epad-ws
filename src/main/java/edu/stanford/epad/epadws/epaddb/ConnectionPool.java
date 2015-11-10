//Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without modification, are permitted provided that
//the following conditions are met:
//
//Redistributions of source code must retain the above copyright notice, this list of conditions and the following
//disclaimer.
//
//Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
//following disclaimer in the documentation and/or other materials provided with the distribution.
//
//Neither the name of The Board of Trustees of the Leland Stanford Junior University nor the names of its
//contributors (Daniel Rubin, et al) may be used to endorse or promote products derived from this software without
//specific prior written permission.
//
//THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
//INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
//SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
//WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
//USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

	private int initialConnections = 25;

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
			logger.info("Creating new connection, used:" + connectionsUsed.size());
			Connection connection = createConnection();
			connectionsUsed.add(connection);
			return connection;
		} else {
			int size = connectionsAvailable.size();
			Connection connection = connectionsAvailable.get(size - 1);
			connectionsAvailable.remove(connection);
			if (!connection.isValid(1)) {
				logger.info("Closing invalid/expired connection for URL " + connectionUrl); // TODO Remove this log eventually
				connection.close();
				connection = createConnection();
			}
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
	private synchronized void closeExcessConnections() throws SQLException
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