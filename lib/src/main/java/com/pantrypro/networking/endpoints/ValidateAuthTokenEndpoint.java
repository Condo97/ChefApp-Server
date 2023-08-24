package com.pantrypro.networking.endpoints;

import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.networking.server.request.AuthRequest;
import com.pantrypro.networking.server.response.ValidateAuthTokenResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ValidateAuthTokenEndpoint {

    public static ValidateAuthTokenResponse validateAuthToken(AuthRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get userID from UserAuthenticator and return conditional that it is not null in ValidateAuthTokenResponse.. wait can it be done in one line?
        return new ValidateAuthTokenResponse(UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken()) != null);
    }

}
