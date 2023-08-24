package com.pantrypro.networking.endpoints;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.pantrypro.core.PPPremiumValidator;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.request.AuthRequest;
import com.pantrypro.networking.server.response.BodyResponse;
import com.pantrypro.networking.server.response.IsPremiumResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class GetIsPremiumEndpoint {

    public static IsPremiumResponse getIsPremium(AuthRequest request) throws SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerException, DBSerializerPrimaryKeyMissingException {
        // Get userID from authRequest
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(userID);

        // Build isPremiumResponse and return
        IsPremiumResponse isPremiumResponse = new IsPremiumResponse(isPremium);

        return isPremiumResponse;
    }

}
