package edu.stanford.epad.epadws.arrdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.epad.common.util.EPADLogger;
import edu.stanford.epad.epadws.epaddb.ConnectionPool;
import edu.stanford.epad.epadws.epaddb.DatabaseUtils;
import edu.stanford.epad.epadws.epaddb.EpadDatabaseCommands;

public class DefaultArrDatabaseOperations implements ArrDatabaseOperations {

	private static final EPADLogger log = EPADLogger.getInstance();

	private final ConnectionPool connectionPool;

	@Override
	public int getFreeConnections() {
		return connectionPool.availableConnectionCount();
	}

	@Override
	public int getUsedConnections() {
		return connectionPool.usedConnectionCount();
	}

	public DefaultArrDatabaseOperations(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	@Override
	public void removeOldLogs(int olderThanDays) {
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//delete the active part and part object first than the audit log itself (foreign key constraint)
			c = getConnection();
			ps = c.prepareStatement(ArrDatabaseCommands.DELETE_ACTIVE_PART);
			ps.setInt(1, olderThanDays);
			ps.executeUpdate();
			DatabaseUtils.close(ps);
			ps = c.prepareStatement(ArrDatabaseCommands.DELETE_PART_OBJ);
			ps.setInt(1, olderThanDays);
			ps.executeUpdate();
			DatabaseUtils.close(ps);
			ps = c.prepareStatement(ArrDatabaseCommands.DELETE_AUDIT_RECORD);
			ps.setInt(1, olderThanDays);
			ps.executeUpdate();
			
			
		} catch (SQLException sqle) {
			String debugInfo = DatabaseUtils.getDebugData(rs);
			log.warning("Database operation failed; debugInfo=" + debugInfo, sqle);
		} finally {
			close(c, ps, rs);
		}
		
	}
	private Connection getConnection() throws SQLException
	{
		return connectionPool.getConnection();
	}

	private void close(Connection c)
	{
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, Statement s)
	{
		DatabaseUtils.close(s);
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, PreparedStatement ps)
	{
		DatabaseUtils.close(ps);
		connectionPool.freeConnection(c);
	}

	private void close(Connection c, Statement s, ResultSet rs)
	{
		DatabaseUtils.close(rs);
		close(c, s);
	}

	private void close(Connection c, PreparedStatement ps, ResultSet rs)
	{
		DatabaseUtils.close(rs);
		close(c, ps);
	}
}
