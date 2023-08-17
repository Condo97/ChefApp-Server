package com.pantrypro.connectionpool;

import com.pantrypro.util.Connectable;

import java.sql.Connection;

public interface ISQLConncetionPool {

    Connection getConnection() throws InterruptedException;
    void releaseConnection(Connection connection);

}
