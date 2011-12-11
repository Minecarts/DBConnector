package com.minecarts.dbconnector.provider;

import java.sql.Connection;

public interface Provider {
    public String getName();
    public boolean isConnected();
    public boolean connect();
    public void release();
    public Connection getConnection();
}
