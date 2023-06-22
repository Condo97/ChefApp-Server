package com.pantrypro.core.service.endpoints;

import com.pantrypro.common.exceptions.AutoIncrementingDBObjectExistsException;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.server.response.AuthResponse;
import com.pantrypro.model.http.server.response.BodyResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class RegisterUserEndpoint {

    public static BodyResponse registerUser() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, IllegalAccessException, InvocationTargetException {
        // Get AuthToken from Database by registering new user
        User_AuthToken u_aT = User_AuthTokenDBManager.createInDB();

        // Prepare and return new bodyResponse object
        AuthResponse registerUserResponse = new AuthResponse(u_aT.getAuthToken());

        return BodyResponseFactory.createSuccessBodyResponse(registerUserResponse);
    }

}
