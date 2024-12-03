package com.pantrypro.database.dao.pooled;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.User_AuthTokenDAO;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.AutoIncrementingDBObjectExistsException;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class User_AuthTokenDAOPooled {

    public static void insert(User_AuthToken u_aT) throws SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, AutoIncrementingDBObjectExistsException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            User_AuthTokenDAO.insert(conn, u_aT);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static User_AuthToken get(String authToken) throws SQLException, InterruptedException, DBSerializerException, DBObjectNotFoundFromQueryException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return User_AuthTokenDAO.get(conn, authToken);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}
