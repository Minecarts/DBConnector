package com.minecarts.dbconnector.pool;

import java.sql.Connection;

public interface Pool {
    public boolean connect(String url, String username, String password, int minConn, int maxConn, int maxCreated, int connTimeout);
    public void release();
    public Connection getConnection();
}
