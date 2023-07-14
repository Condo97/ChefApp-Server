package com.pantrypro.core.service.endpoints;

import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.model.http.server.request.AuthRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.ValidateAuthTokenResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ValidateAuthTokenEndpoint {

    public static BodyResponse validateAuthToken(AuthRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get User_AuthToken, and return if it is null or not.. wait can it be done in one line?
        return BodyResponseFactory.createSuccessBodyResponse(new ValidateAuthTokenResponse(User_AuthTokenDBManager.getFromDB(request.getAuthToken()) != null));
    }

}
