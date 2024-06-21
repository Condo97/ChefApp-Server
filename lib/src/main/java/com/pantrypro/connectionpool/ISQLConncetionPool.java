package com.pantrypro.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

public interface ISQLConncetionPool {

    Connection getConnection() throws InterruptedException, SQLException;
    void releaseConnection(Connection connection);

}
