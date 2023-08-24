package com.pantrypro.database.dao.factory;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.User_AuthTokenDAO;
import com.pantrypro.database.dao.helpers.AuthTokenGenerator;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.AutoIncrementingDBObjectExistsException;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class User_AuthTokenFactoryDAO {

    /***
     * Creates a User_AuthToken object with an empty userID and generated authToken, just for use when registering a new user
     *
     * @return a User_AuthToken with only a generated authToken
     */
    public static User_AuthToken createAndSave() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return createAndSave(null);
    }

    /***
     * This creates a new User_AuthToken with the userID and a generated AuthToken
     *
     * @param userID
     * @return User_AuthToken with generated AuthToken
     */
    public static User_AuthToken createAndSave(Integer userID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException {
        return createAndSave(userID, AuthTokenGenerator.generateAuthToken());
    }

    public static User_AuthToken createAndSave(Integer userID, String authToken) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create User_AuthToken object
        User_AuthToken u_aT = new User_AuthToken(
                userID,
                authToken
        );

        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            // Insert User_AuthToken object and return
            User_AuthTokenDAO.insert(conn, u_aT);

            return u_aT;
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}
