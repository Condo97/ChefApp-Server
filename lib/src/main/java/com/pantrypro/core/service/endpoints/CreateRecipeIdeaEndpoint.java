package com.pantrypro.core.service.endpoints;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.database.calculators.IdeaRecipeRemainingCalculator;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.service.responsefactories.CreateRecipeIdeaResponseFactory;
import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.CreateIdeaRecipeRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.CreateIdeaRecipeResponse;
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

public class CreateRecipeIdeaEndpoint {

    public static BodyResponse createRecipeIdea(CreateIdeaRecipeRequest createIdeaRecipeRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, OpenAIGPTException, IOException, AppStoreStatusResponseException, UnrecoverableKeyException, CapReachedException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Create ideaRecipe for user, which does save and validation
        IdeaRecipe ideaRecipe = PantryPro.createSaveIdeaRecipe(
                createIdeaRecipeRequest.getAuthToken(),
                createIdeaRecipeRequest.getIngredients(),
                createIdeaRecipeRequest.getModifiers(),
                createIdeaRecipeRequest.getExpandIngredientsMagnitude()
        );

        // Get remaining ideaRecipes for user
        Long remaining = new IdeaRecipeRemainingCalculator().calculateRemaining(createIdeaRecipeRequest.getAuthToken());

        // Adapt to CreateIdeaRecipeResponse
        CreateIdeaRecipeResponse createIdeaRecipeResponse = CreateRecipeIdeaResponseFactory.from(
                ideaRecipe,
                remaining
        );

        // Return createIdeaRecipeResponse in success BodyResponse
        return BodyResponseFactory.createSuccessBodyResponse(createIdeaRecipeResponse);
    }

}
