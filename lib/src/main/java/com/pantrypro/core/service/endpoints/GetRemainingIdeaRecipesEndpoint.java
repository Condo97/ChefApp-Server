package com.pantrypro.core.service.endpoints;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.database.calculators.RemainingCalculator;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.database.calculators.IdeaRecipeRemainingCalculator;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.PPRemainingCalculator;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.AuthRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.GetRemainingResponse;
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

public class GetRemainingIdeaRecipesEndpoint {

    public static BodyResponse getRemaining(AuthRequest authRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Get remaining
        Long remaining = new IdeaRecipeRemainingCalculator().calculateRemaining(authRequest.getAuthToken());

        // Build getRemainingResponse
        GetRemainingResponse getRemainingResponse = new GetRemainingResponse(remaining);

        // Build and return success body response with getRemainingResponse
        return BodyResponseFactory.createSuccessBodyResponse(getRemainingResponse);
    }

}
