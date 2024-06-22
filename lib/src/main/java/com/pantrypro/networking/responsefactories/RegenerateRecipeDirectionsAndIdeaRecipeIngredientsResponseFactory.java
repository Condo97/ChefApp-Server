package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.networking.server.response.RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory {

    public static RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse from(List<RecipeMeasuredIngredient> recipeMeasuredIngredients, List<RecipeInstruction> recipeInstructions, Integer estimatedServings, Integer feasibility) {
        // Create recipeMeasuredIngredientStrings from recipeMeasuredIngredients
        List<String> recipeMeasuredIngredientStrings = recipeMeasuredIngredients.stream().map(RecipeMeasuredIngredient::getMeasuredIngredient).toList();

        // Create recipeInstructionsMap from recipeInstructions
        Map<Integer, String> recipeInstructionsMap = IntStream.range(0, recipeInstructions.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i, // Index as the key
                        i -> recipeInstructions.get(i).getText() // RecipeInstruction text as the value
                ));

        // Create legacy recipeInstructionStrings
        List<String> recipeInstructionStrings_legacy = null, ideaRecipeIngredientStrings = null;

        if (recipeInstructions != null && !recipeInstructions.isEmpty()) {
            recipeInstructionStrings_legacy = new ArrayList<>();

            for (RecipeInstruction instruction: recipeInstructions) {
                recipeInstructionStrings_legacy.add(instruction.getText());
            }
        }

        // Construct RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse and return
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse response = new RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse(
                recipeMeasuredIngredientStrings,
                recipeInstructionsMap,
                estimatedServings,
                feasibility,
                recipeInstructionStrings_legacy
        );

        return response;
    }

}
