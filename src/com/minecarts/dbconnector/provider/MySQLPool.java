package com.minecarts.dbconnector.provider;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import snaq.db.ConnectionPool; //http://www.snaq.net/java/DBPool/

public class MySQLPool implements Provider {
    private final String name;
    private final String url;
    private final String username;
    private final String password;
    private final int minConn;
    private final int maxConn;
    private final int maxCreated;
    private final int connTimeout;
    
    private ConnectionPool pool = null;
    
    static {
        try {
            // Register MySQL JDBC driver
            Class c = Class.forName("com.mysql.jdbc.Driver");
            Driver driver = (Driver) c.newInstance();
            DriverManager.registerDriver(driver);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public MySQLPool(String name, String url, String username, String password, int minConn, int maxConn, int maxCreated, int connTimeout) {
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
        this.minConn = minConn;
        this.maxConn = maxConn;
        this.maxCreated = maxCreated;
        this.connTimeout = connTimeout;
    }
    
    @Override
    public String toString() {
        return String.format("{MySQLPool \"%s\"}", name);
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isConnected() {
        return pool != null;
    }
    
    public boolean connect() {
        if(isConnected()) return true;
        
        //Create our connection pool
        pool = new ConnectionPool(
                        name, //Pool name
                        minConn, //Minimum connections held in the pool
                        maxConn, //Maximum connections held in the pool
                        maxCreated, //Maximum connections that can be created
                        connTimeout, //Idle timeout of connections in seconds
                        url,
                        username,
                        password
                );

        //By default the pool doesn't connect to minecarts, only when a connection is
        //requested from the pool, so we can force it to connect by calling init on the pool
        //but this isn't required
        pool.init();
        
        return isConnected();
    }

    public Connection getConnection() {
        if(!isConnected()) connect();
        
        try {
            Connection conn = pool.getConnection(3000); //3 second timeout
            if(conn != null) return conn;
            
            System.out.println("Timeout getting a connection from the connection pool.");
        }
        catch(SQLException e) {
            System.out.println("Unexpected SQL error getting a connection from the pool:");
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void release() {
        if(pool != null) {
            pool.releaseForcibly();
            pool = null;
        }
    }
    
}
