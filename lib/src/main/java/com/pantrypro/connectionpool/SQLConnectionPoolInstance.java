package com.pantrypro.connectionpool;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnectionPoolInstance {
    private static SQLConnectionPool instance;

    public static SQLConnectionPool create(String url, String user, String password, int size) throws SQLException {
        instance = SQLConnectionPool.create(url, user, password, size);

        return instance;
    }

    public synchronized static Connection getConnection() throws InterruptedException {
        return instance.getConnection();
    }

    public static void releaseConnection(Connection connection) {
        instance.releaseConnection(connection);
    }
}
