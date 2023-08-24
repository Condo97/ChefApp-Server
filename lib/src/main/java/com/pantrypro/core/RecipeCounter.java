package com.pantrypro.core;

import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class RecipeCounter {

    public static Long countRecipes(String authToken) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get userID from authToken
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        return countRecipes(userID);
    }

    public static Long countRecipes(Integer userID) throws DBSerializerException, SQLException, InterruptedException {
        return RecipeDAOPooled.countForToday(userID);
    }

}
