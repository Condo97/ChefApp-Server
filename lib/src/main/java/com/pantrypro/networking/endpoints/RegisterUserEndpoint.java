package com.pantrypro.networking.endpoints;

import com.pantrypro.database.dao.factory.User_AuthTokenFactoryDAO;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.AutoIncrementingDBObjectExistsException;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.response.AuthResponse;
import com.pantrypro.networking.server.response.BodyResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class RegisterUserEndpoint {

    public static BodyResponse registerUser() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, IllegalAccessException, InvocationTargetException {
        // Get AuthToken from Database by registering new user
        User_AuthToken u_aT = User_AuthTokenFactoryDAO.createAndSave();

        // Prepare and return new bodyResponse object
        AuthResponse registerUserResponse = new AuthResponse(u_aT.getAuthToken());

        return BodyResponseFactory.createSuccessBodyResponse(registerUserResponse);
    }

}
