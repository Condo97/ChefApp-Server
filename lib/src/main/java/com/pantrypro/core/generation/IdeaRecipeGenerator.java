package com.pantrypro.core.generation;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.Constants;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.PPRemainingCalculator;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.core.database.adapters.oai.IdeaRecipeFromOpenAIAdapter;
import com.pantrypro.core.generation.openai.OAIIdeaRecipeGenerator;
import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseCreateRecipeIdea;
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
import java.util.concurrent.atomic.AtomicReference;

public class IdeaRecipeGenerator {

    public static IdeaRecipe generateForUser(String authToken, String ingredientsString, String modifiersString, Integer expandIngredientsMagnitude) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, OpenAIGPTException {
        // Get userID from PPUserAuthenticator
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        // If remaining idea recipes is not null (infinite) and less than or equal to 0, throw cap reached exception
        Long remaining = PPRemainingCalculator.getRemainingIdeaRecipes(userID);
        if (remaining != null && remaining <= 0)
            throw new CapReachedException("Cap reached for user when generating idea recipe");

        // Create input atomic reference to get the input from CreateRecipeIdeaGenerator for creating IdeaRecipe object
        AtomicReference<String> input = new AtomicReference<>();

        // Generate if validated
        OAIGPTFunctionCallResponseCreateRecipeIdea createRecipeIdeaFunctionCallResponse = OAIIdeaRecipeGenerator.generateCreateRecipeIdeaFunctionCall(ingredientsString, modifiersString, expandIngredientsMagnitude, Constants.Context_Character_Limit_Create_Recipe_Idea, Constants.Response_Token_Limit_Create_Recipe_Idea, input);

        // Adapt to IdeaRecipe, IdeaRecipeIngredients, and IdeaRecipeEquipment
        return IdeaRecipeFromOpenAIAdapter.getIdeaRecipe(userID, input.get(), expandIngredientsMagnitude, createRecipeIdeaFunctionCallResponse);
    }

}
