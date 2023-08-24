package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.compoundobjects.IngredientAndMeasurement;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeDirection;
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
import java.util.ArrayList;
import java.util.List;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsEndpoint {

    public static RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse regenerateRecipeDirectionsAndUpdateMeasuredIngredients(RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest request) throws InvalidAssociatedIdentifierException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, OAISerializerException, OpenAIGPTException, OAIDeserializerException, IOException {
        // Validate user recipe association
        PantryPro.validateUserRecipeAssociation(request.getAuthToken(), request.getRecipeID());

        // Adapt request ingredients and measurements to compoundobject ingredient and measurement
        List<IngredientAndMeasurement> ingredientsAndMeasurements = new ArrayList<>();
        for (RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest.IngredientsAndMeasurements requestIngredientAndMeasurement: request.getIngredientsAndMeasurements()) {
            ingredientsAndMeasurements.add(new IngredientAndMeasurement(
                    requestIngredientAndMeasurement.getIngredient(),
                    requestIngredientAndMeasurement.getMeasurement()
            ));
        }

        // Update ingredients
        PantryPro.updateIngredientsAndMeasurements(request.getRecipeID(), ingredientsAndMeasurements);

        // Regenerate directions
        PantryPro.regenerateDirections(request.getRecipeID());

        // Get Recipe
        Recipe recipe = PantryPro.getRecipe(request.getRecipeID());

        // Get RecipeDirections
        List<RecipeDirection> recipeDirections = PantryPro.getRecipeDirections(request.getRecipeID());

        // Create and return RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse response = RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory.from(
                recipeDirections,
                recipe.getFeasibility()
        );

        return response;
    }

}
