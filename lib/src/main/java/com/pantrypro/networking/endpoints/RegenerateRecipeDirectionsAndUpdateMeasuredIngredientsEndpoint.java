package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.responsefactories.RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory;
import com.pantrypro.networking.server.request.RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest;
import com.pantrypro.networking.server.response.RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsEndpoint {

    // The big difference between this and MakeRecipeEndpoint is that this one has a more extensive request object that includes the name, summary, and ingredients and updates them before generating the recipe.. It should be able to be simplified to one class
    public static RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse regenerateRecipeDirectionsAndUpdateMeasuredIngredients(RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest request) throws InvalidAssociatedIdentifierException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, OAISerializerException, OpenAIGPTException, OAIDeserializerException, IOException {
        // Ensure request getIngredientsAndMeasurements is not null, otherwise return null TODO: Also ensure name and summary, also make this better maybe throw an exception or something
        if (request.getMeasuredIngredients() == null)
            return null;

        // Validate user recipe association
        PantryPro.validateUserRecipeAssociation(request.getAuthToken(), request.getRecipeID());

        // Update name
        if (request.getNewName() != null && !request.getNewName().isEmpty()) {
            PantryPro.updateName(request.getRecipeID(), request.getNewName());
        }

        // Update summary
        if (request.getNewSummary() != null && !request.getNewSummary().isEmpty()) {
            PantryPro.updateSummary(request.getRecipeID(), request.getNewSummary());
        }

        // Update estimated servings
        if (request.getNewServings() != null) {
            PantryPro.updateEstimatedServings(request.getRecipeID(), request.getNewServings());
        }

        // Update ingredients
        if (request.getMeasuredIngredients() != null && !request.getMeasuredIngredients().isEmpty()) {
            PantryPro.updateIngredientsAndMeasurements(request.getRecipeID(), request.getMeasuredIngredients());
        }

        // Regenerate directions
        PantryPro.regenerateDirections(request.getRecipeID());

        // Get Recipe
        Recipe recipe = PantryPro.getRecipe(request.getRecipeID());

        // Get RecipeDirections
        List<RecipeInstruction> recipeInstructions = PantryPro.getRecipeDirections(request.getRecipeID());

        // Create and return RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse response = RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory.from(
                recipeInstructions,
                recipe.getFeasibility()
        );

        return response;
    }

}
