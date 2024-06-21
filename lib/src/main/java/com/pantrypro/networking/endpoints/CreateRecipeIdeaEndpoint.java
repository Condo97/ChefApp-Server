package com.pantrypro.networking.endpoints;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.calculators.RecipeRemainingCalculator;
import com.pantrypro.database.compoundobjects.RecipeWithIngredients;
import com.pantrypro.exceptions.CapReachedException;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.networking.responsefactories.CreateRecipeIdeaResponseFactory;
import com.pantrypro.networking.server.request.CreateIdeaRecipeRequest;
import com.pantrypro.networking.server.response.CreateIdeaRecipeResponse;
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

    public static CreateIdeaRecipeResponse createRecipeIdea(CreateIdeaRecipeRequest createIdeaRecipeRequest) throws SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, UnrecoverableKeyException, CapReachedException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, OAISerializerException, OAIDeserializerException, DBSerializerPrimaryKeyMissingException, DBSerializerException, AppStoreErrorResponseException {
        // Create save recipe idea
        RecipeWithIngredients recipeWithIngredients = PantryPro.createSaveRecipeIdea(
                createIdeaRecipeRequest.getAuthToken(),
                createIdeaRecipeRequest.getIngredients(),
                createIdeaRecipeRequest.getModifiers(),
                createIdeaRecipeRequest.getExpandIngredientsMagnitude()
        );

        // Get remaining ideaRecipes for user
        Long remaining = new RecipeRemainingCalculator().calculateRemaining(createIdeaRecipeRequest.getAuthToken());

        // Adapt to CreateIdeaRecipeResponse
        CreateIdeaRecipeResponse createIdeaRecipeResponse = CreateRecipeIdeaResponseFactory.from(
                recipeWithIngredients.getRecipe(),
                recipeWithIngredients.getMeasuredIngredients(),
                remaining
        );

        // Return createIdeaRecipeResponse
        return createIdeaRecipeResponse;
//        return BodyResponseFactory.createSuccessBodyResponse(createIdeaRecipeResponse);
    }

}
