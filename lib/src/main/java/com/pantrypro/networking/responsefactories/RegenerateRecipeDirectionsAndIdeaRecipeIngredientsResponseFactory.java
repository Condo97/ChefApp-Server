package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.networking.server.response.RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory {

    public static RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse from(List<RecipeInstruction> recipeInstructions, Integer feasibility) {
        // Convert recipeInstructions and ideaRecipeIngredients to string arrays
        Map<Integer, String> recipeInstructionsMap = IntStream.range(0, recipeInstructions.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i, // Index as the key
                        i -> recipeInstructions.get(i).getText() // RecipeInstruction text as the value
                ));
        List<String> recipeInstructionStrings_legacy = null, ideaRecipeIngredientStrings = null;

        if (recipeInstructions != null && !recipeInstructions.isEmpty()) {
            recipeInstructionStrings_legacy = new ArrayList<>();

            for (RecipeInstruction instruction: recipeInstructions) {
                recipeInstructionStrings_legacy.add(instruction.getText());
            }
        }

//        if (ideaRecipeIngredients != null && !ideaRecipeIngredients.isEmpty()) {
//            ideaRecipeIngredientStrings = new ArrayList<>();
//
//            for (IdeaRecipeIngredient ingredient: ideaRecipeIngredients) {
//                ideaRecipeIngredientStrings.add(ingredient.getName());
//            }
//        }

        // Construct RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse response = new RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse(
                recipeInstructionsMap,
                feasibility,
                recipeInstructionStrings_legacy
        );

        return response;
    }

}
