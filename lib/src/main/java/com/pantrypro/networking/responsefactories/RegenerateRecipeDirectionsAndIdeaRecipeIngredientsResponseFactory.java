package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.RecipeDirection;
import com.pantrypro.networking.server.response.RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse;

import java.util.ArrayList;
import java.util.List;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory {

    public static RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse from(List<RecipeDirection> recipeDirections, Integer feasibility) {
        // Convert recipeInstructions and ideaRecipeIngredients to string arrays
        List<String> recipeInstructionStrings = null, ideaRecipeIngredientStrings = null;

        if (recipeDirections != null && !recipeDirections.isEmpty()) {
            recipeInstructionStrings = new ArrayList<>();

            for (RecipeDirection instruction: recipeDirections) {
                recipeInstructionStrings.add(instruction.getText());
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
                recipeInstructionStrings,
                feasibility
        );

        return response;
    }

}
