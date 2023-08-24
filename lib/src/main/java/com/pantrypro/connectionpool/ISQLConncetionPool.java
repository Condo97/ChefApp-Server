package com.pantrypro.connectionpool;

import java.sql.Connection;

public interface ISQLConncetionPool {

    Connection getConnection() throws InterruptedException;
    void releaseConnection(Connection connection);

}
