package com.pantrypro.core;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.User_AuthTokenDAO;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class UserAuthenticator {

    public static Integer getUserIDFromAuthToken(String authToken) throws InterruptedException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return getUserIDFromAuthToken(authToken, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static Integer getUserIDFromAuthToken(String authToken, Connection conn) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        User_AuthToken u_aT = User_AuthTokenDAO.get(conn, authToken);

        return u_aT.getUserID();
    }

}
