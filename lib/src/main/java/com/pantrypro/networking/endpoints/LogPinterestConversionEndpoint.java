package com.pantrypro.networking.endpoints;

import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.core.conversiontracking.pinterest.PinterestConversionLogger;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.client.pinterest.conversionsapi.request.PinterestSendConversionRequest;
import com.pantrypro.networking.server.request.LogPinterestConversionRequest;
import com.pantrypro.networking.server.response.LogPinterestConversionResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class LogPinterestConversionEndpoint {

    public static LogPinterestConversionResponse logPinterestConversion(LogPinterestConversionRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvalidAssociatedIdentifierException, IOException {
        // Null or empty check for authToken
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty())
            throw new InvalidAssociatedIdentifierException("Missing authToken!");

        // Validate authToken
        if (UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken()) == null)
            throw new InvalidAssociatedIdentifierException("Invalid authToken!");

        // Null or empty check for idfa
        if (request.getIdfa() == null || request.getIdfa().isEmpty())
            throw new InvalidAssociatedIdentifierException("Missing IDFA!");

        // Null or empty check for eventName
        if (request.getEventName() == null || request.getEventName().isEmpty())
            throw new InvalidAssociatedIdentifierException("Missing eventName!");

        // Enum unwrap for eventName
        PinterestSendConversionRequest.EventNames eventName = PinterestSendConversionRequest.EventNames.from(request.getEventName());

        // Log pinterest conversion
        PinterestConversionLogger.logPinterestConversion(
                eventName,
                request.getIdfa(),
                request.getTest()
        );

        // Return LogPinterestConversionResponse with didLog to true:)
        return new LogPinterestConversionResponse(true);
    }

}
