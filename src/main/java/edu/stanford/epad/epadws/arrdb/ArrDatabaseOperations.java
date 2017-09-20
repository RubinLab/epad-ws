package edu.stanford.epad.epadws.arrdb;

public interface ArrDatabaseOperations {

	void removeOldLogs(int olderThanDays);

	int getFreeConnections();

	int getUsedConnections();
}
