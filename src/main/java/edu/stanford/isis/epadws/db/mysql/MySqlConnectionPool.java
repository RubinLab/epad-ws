package edu.stanford.isis.epadws.db.mysql;

import edu.stanford.isis.epadws.server.ProxyLogger;
import edu.stanford.isis.epadws.server.ShutdownSignal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Don't know why Java (Oracle/Sun) doesn't provide its own connection pool class?
 *
 * @author amsnyder
 */
public class MySqlConnectionPool implements Runnable
{

    private static final ProxyLogger logger = ProxyLogger.getInstance();

    int initialConnections = 5;
    List<Connection> connectionsAvailable = Collections.synchronizedList(new ArrayList<Connection>());
    List<Connection> connectionsUsed = Collections.synchronizedList(new ArrayList<Connection>());

    String connectionUrl;
    String userName;
    String userPassword;

    public MySqlConnectionPool(String url,String userName, String userPass) throws SQLException
    {
        try {
            this.connectionUrl = url;
            this.userName = userName;
            this.userPassword = userPass;
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
            // creating a new Connection
            newConnection = createConnection();
            // adding Connection to used list
            connectionsUsed.add(newConnection);
        } else {
            int size = connectionsAvailable.size();
            newConnection = connectionsAvailable.get(size-1);
            connectionsAvailable.remove(newConnection);
            connectionsUsed.add(newConnection);
        }
        return newConnection;
    }

    public synchronized void freeConnection(Connection connection){
        connectionsUsed.remove(connection);
        connectionsAvailable.add(connection);
    }

    public int availableConnectionCount()
    {
        return connectionsAvailable.size();
    }

    public int usedConnectionCount(){
        return connectionsUsed.size();
    }

    public void run()
    {
        try {
            ShutdownSignal shutdownSignal = ShutdownSignal.getInstance();
            while ( !shutdownSignal.hasShutdown() )
            {
                synchronized (this)
                {
                    closeExcessConnections();
                }//synchronized
                Thread.sleep(10);
            }//while
        } catch (SQLException sqle) {
            logger.sever("SQL Exception. Lost the connection pool.",sqle);
            sqle.printStackTrace();
        } catch (Exception e) {
            logger.sever("Lost MySQL connection pool! ",e);
            e.printStackTrace();
        }

    }//run

    /**
     * Private method to close excess connections.
     * @throws SQLException -
     */
    private void closeExcessConnections()
        throws SQLException
    {
        int caSize = connectionsAvailable.size();
        while ( caSize > initialConnections )
        {
            Connection connection = connectionsAvailable.get(caSize-1);
            connectionsAvailable.remove(connection);
            connection.close();
            caSize = connectionsAvailable.size();
        }//while
    }

    /**
     * Call during shutdown to get rid of all the connections.
     */
    public void dispose(){
        try{
            synchronized (this) {
                logger.info("Shutting down mysql database connection pool. #avail: "
                        +connectionsAvailable.size()+" #used: "+connectionsUsed.size());

                initialConnections=0;
                closeExcessConnections();
            }
        }catch(SQLException sqle){
            logger.sever("Failed to dispose of connections.",sqle);
        }
    }

}