package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeDirection;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.responsefactories.MakeRecipeResponseFactory;
import com.pantrypro.networking.server.request.MakeRecipeRequest;
import com.pantrypro.networking.server.response.MakeRecipeResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class MakeRecipeEndpoint {

    public static MakeRecipeResponse makeRecipe(MakeRecipeRequest makeRecipeRequest) throws DBSerializerPrimaryKeyMissingException, SQLException, DBObjectNotFoundFromQueryException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, DBSerializerException, OpenAIGPTException, InstantiationException, InvalidAssociatedIdentifierException, OAISerializerException, OAIDeserializerException {
        // Validate user recipe association
        PantryPro.validateUserRecipeAssociation(makeRecipeRequest.getAuthToken(), makeRecipeRequest.getRecipeID());

        // Finalize and save recipe
        PantryPro.finalizeSaveRecipe(makeRecipeRequest.getRecipeID());

        // Get Recipe
        Recipe recipe = PantryPro.getRecipe(makeRecipeRequest.getRecipeID());

        // Get RecipeMeasuredIngredients
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = PantryPro.getRecipeMeasuredIngredients(makeRecipeRequest.getRecipeID());

        // Get RecipeDirections
        List<RecipeDirection> recipeDirections = PantryPro.getRecipeDirections(makeRecipeRequest.getRecipeID());

        // Create MakeRecipeResponse and return
        MakeRecipeResponse makeRecipeResponse = MakeRecipeResponseFactory.from(
                recipe,
                recipeMeasuredIngredients,
                recipeDirections
        );

        return makeRecipeResponse;
    }

}
