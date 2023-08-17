package com.pantrypro.core;

import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class IdeaRecipeCounter {

    public static Long countIdeaRecipes(String authToken) throws InterruptedException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return countIdeaRecipes(authToken, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static Long countIdeaRecipes(String authToken, Connection conn) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get userID from authToken
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        return countIdeaRecipes(userID, conn);
    }

    public static Long countIdeaRecipes(Integer userID, Connection conn) throws DBSerializerException, SQLException, InterruptedException {
        return IdeaRecipeDBManager.countForToday(conn, userID);
    }

}
