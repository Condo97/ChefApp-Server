package com.pantrypro.core.database.managers;

import com.dbclient.DBManager;
import com.pantrypro.common.exceptions.AutoIncrementingDBObjectExistsException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.database.managers.helpers.AuthTokenGenerator;
import com.pantrypro.model.database.DBRegistry;
import com.pantrypro.model.database.objects.User_AuthToken;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class User_AuthTokenDBManager {

    public static User_AuthToken getFromDB(Connection conn, String authToken) throws DBSerializerException, SQLException, IllegalAccessException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        List<User_AuthToken> u_aTs = DBManager.selectAllWhere(
                conn,
                User_AuthToken.class,
                DBRegistry.Table.User_AuthToken.auth_token,
                SQLOperators.EQUAL,
                authToken
        );

        // If there are no u_aTs, throw an exception
        if (u_aTs.size() == 0)
            throw new DBObjectNotFoundFromQueryException("No user_authToken found!");

        // If there is more than one u_aTs, it shouldn't be a functionality issue at this moment but print to console to see how widespread this is
        if (u_aTs.size() > 1)
            System.out.println("More than one user_authToken found when getting User_AuthToken.. This should never be seen!");

        // Return first u_aT
        return u_aTs.get(0);
    }

    public static User_AuthToken createInDB(Connection conn) throws AutoIncrementingDBObjectExistsException, DBSerializerException, DBSerializerPrimaryKeyMissingException, IllegalAccessException, SQLException, InterruptedException, InvocationTargetException {
        // Create u_aT and ensure no userID
        User_AuthToken u_aT = create();

        DBManager.deepInsert(conn, u_aT);

        return u_aT;
    }

    /***
     * Creates a User_AuthToken object with an empty userID and generated authToken, just for use in the factory
     *
     * @return a User_AuthToken with only a generated authToken
     */
    private static User_AuthToken create() {
        return create(null);
    }

    /***
     * This creates a new User_AuthToken with the userID and a generated AuthToken
     *
     * @param userID
     * @return User_AuthToken with generated AuthToken
     */
    public static User_AuthToken create(Integer userID) {
        return create(userID, AuthTokenGenerator.generateAuthToken());
    }

    public static User_AuthToken create(Integer userID, String authToken) {
        return new User_AuthToken(userID, authToken);
    }
}
