package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.JSONSchemaDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.exceptions.AuthTokenExpiredException;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.responsefactories.MakeRecipeResponseFactory;
import com.pantrypro.networking.server.request.MakeRecipeRequest;
import com.pantrypro.networking.server.response.MakeRecipeResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import com.pantrypro.core.Endpoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class MakeRecipeEndpoint implements Endpoint<MakeRecipeRequest> {

    @Override
    public Object getResponse(MakeRecipeRequest request) throws Exception {
        return makeRecipe(request);
    }

    public static MakeRecipeResponse makeRecipe(MakeRecipeRequest makeRecipeRequest) throws DBSerializerPrimaryKeyMissingException, SQLException, DBObjectNotFoundFromQueryException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, DBSerializerException, OpenAIGPTException, InstantiationException, InvalidAssociatedIdentifierException, OAISerializerException, JSONSchemaDeserializerException, AuthTokenExpiredException {
        // COMPATIBILITY - Get recipeID from request recipeID or if null ideaID
        Integer recipeID;
        if (makeRecipeRequest.getRecipeID() != null) {
            recipeID = makeRecipeRequest.getRecipeID();
        } else {
            recipeID = makeRecipeRequest.getIdeaID();
        }

        // Validate user recipe association
        PantryPro.validateUserRecipeAssociation(makeRecipeRequest.getAuthToken(), recipeID);

        // Finalize and save recipe
        PantryPro.finalizeSaveRecipe(recipeID, makeRecipeRequest.getAdditionalInput(), makeRecipeRequest.getAuthToken());

        // Get Recipe
        Recipe recipe = PantryPro.getRecipe(recipeID);

        // Get RecipeMeasuredIngredients
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = PantryPro.getRecipeMeasuredIngredients(recipeID);

        // Get RecipeDirections
        List<RecipeInstruction> recipeInstructions = PantryPro.getRecipeDirections(recipeID);

        // Create MakeRecipeResponse and return
        MakeRecipeResponse makeRecipeResponse = MakeRecipeResponseFactory.from(
                recipe,
                recipeMeasuredIngredients,
                recipeInstructions
        );

        return makeRecipeResponse;
    }

}
