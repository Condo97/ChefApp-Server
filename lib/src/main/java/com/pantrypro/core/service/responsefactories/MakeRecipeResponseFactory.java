package com.pantrypro.core.service.responsefactories;

import com.pantrypro.model.database.objects.Recipe;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
import com.pantrypro.model.http.server.response.MakeRecipeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeRecipeResponseFactory {

    public static MakeRecipeResponse from(Recipe recipe, List<RecipeInstruction> instructions, List<RecipeMeasuredIngredient> measuredIngredients) {
        // Put instructions in a map with the index to preserve order
        Map<Integer, String> instructionStringsMap = new HashMap<>();
        for (Integer i = 0; i < instructions.size(); i++) {
            instructionStringsMap.put(i, instructions.get(i).getText());
        }

        // Put measuredIngredients in string array
        List<String> allIngredientsAndMeasurementStrings = new ArrayList<>();
        measuredIngredients.forEach(measuredIngredient -> allIngredientsAndMeasurementStrings.add(measuredIngredient.getString()));

        // Get estimatedTotalMinutes and feasibility
        Integer estimatedTotalCalories = recipe.getEstimated_total_calories();
        Integer estimatedTotalMinutes = recipe.getEstimated_total_minutes();
        Integer estimatedServings = recipe.getEstimated_servings();
        Integer feasibility = recipe.getFeasibility();

        // Build response
        MakeRecipeResponse response = new MakeRecipeResponse(
                instructionStringsMap,
                allIngredientsAndMeasurementStrings,
                estimatedTotalCalories,
                estimatedTotalMinutes,
                estimatedServings,
                feasibility
        );

        return response;
    }

}
