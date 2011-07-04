package com.minecarts.dbconnector.providers;

import java.sql.Connection;

public interface Provider {
    public Connection getConnection();
}
