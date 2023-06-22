package com.pantrypro.core.service.endpoints;

import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.service.PPPremiumValidator;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.AuthRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.IsPremiumResponse;
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

    public static BodyResponse getIsPremium(AuthRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Get u_aT from authRequest
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(u_aT.getUserID());

        // Build isPremiumResponse
        IsPremiumResponse isPremiumResponse = new IsPremiumResponse(isPremium);

        // Build and return success body response with isPremiumResponse
        return BodyResponseFactory.createSuccessBodyResponse(isPremiumResponse);
    }

}
