package com.minecarts.dbconnector.pool;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import snaq.db.ConnectionPool; //http://www.snaq.net/java/DBPool/

public class MySQLPool implements Pool {
    public String name;
    public ConnectionPool pool = null;
    public boolean connected;
    
    public MySQLPool(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return String.format("(%s)%s", this.getClass().getName(), name);
    }
    
    public boolean connect(String url, String username, String password, int minConn, int maxConn, int maxCreated, int connTimeout){
        if(pool != null) release();
        
        try {
            //Ensure the JDBC driver is registered with the java.sql.DriverManager
            Class c = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) c.newInstance();
            DriverManager.registerDriver(driver);
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        //Create our connection pool
        pool = new ConnectionPool(
                        String.format("%s@%s", username, url), //Pool name
                        minConn, //Minimum connections held in the pool
                        maxConn, //Maximum connections held in the pool
                        maxCreated, //Maximum connections that can be created
                        connTimeout, //Idle timeout of connections in seconds
                        url,
                        username,
                        password
                );
        pool.registerMBean();

        //By default the pool doesn't connect to minecarts, only when a connection is
        //requested from the pool, so we can force it to connect by calling init on the pool
        //but this isn't required
        pool.init();
        connected = true;
        
        return connected;
    }

    public Connection getConnection(){
        if (connected && this.pool != null) {
            try {
                Connection con = pool.getConnection(3000); //3 second timeout
                if (con != null) {
                    return con;
                }
                else {
                    System.out.println("Timeout getting a connection from the connection pool.");
                }
            }
            catch (SQLException sqlx) {
                System.out.println("Unexpected SQL error getting a connection from the pool:");
                sqlx.printStackTrace();
            }
        }
        else {
            System.out.println("Pool was not connected and attempted to get a connection from it");
        }
        return null;
    }
    
    public void release() {
        connected = false;
        pool.releaseForcibly();
        pool.unregisterMBean();
        pool = null;
    }
}
