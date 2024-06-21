package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.networking.server.response.MakeRecipeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRecipeResponseFactory {

    public static MakeRecipeResponse from(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients, List<RecipeInstruction> directions) {
        // Put directions in a map with the index to preserve order
        Map<Integer, String> instructionStringsMap = new HashMap<>();
        for (Integer i = 0; i < directions.size(); i++) {
            instructionStringsMap.put(i, directions.get(i).getText());
        }

        // Put ingredients and measurements in string array
        List<String> ingredientsAndMeasurements = new ArrayList<>();
        measuredIngredients.forEach(measuredIngredient -> ingredientsAndMeasurements.add(measuredIngredient.getMeasuredIngredient()));

        // Get estimatedTotalMinutes and feasibility
        Integer estimatedTotalCalories = recipe.getEstimatedTotalCalories();
        Integer estimatedTotalMinutes = recipe.getEstimatedTotalMinutes();
        Integer estimatedServings = recipe.getEstimatedServings();
        Integer feasibility = recipe.getFeasibility();

        // Build response
        MakeRecipeResponse response = new MakeRecipeResponse(
                instructionStringsMap,
                ingredientsAndMeasurements,
                estimatedTotalCalories,
                estimatedTotalMinutes,
                estimatedServings,
                feasibility
        );

        return response;
    }

}
