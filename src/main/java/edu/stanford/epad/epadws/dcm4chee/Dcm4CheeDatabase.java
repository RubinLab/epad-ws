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
package edu.stanford.epad.epadws.dcm4chee;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

import edu.stanford.epad.common.util.EPADConfig;
import edu.stanford.epad.common.util.EPADLogger;
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
		String username = EPADConfig.dcm4CheeDatabaseUsername;
		String password = EPADConfig.dcm4CheeDatabasePassword;
		String dcm4CheeDatabaseURL = EPADConfig.dcm4CheeDatabaseURL;

		logger.info("MySql using connection string for DCM4CHEE database: " + dcm4CheeDatabaseURL);

		connectionPool = new ConnectionPool(dcm4CheeDatabaseURL, username, password);
	}

	private void closeConnectionPool()
	{
		connectionPool.dispose();
	}
}
