package com.pantrypro.core.service.responsefactories;

import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.http.server.response.RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse;

import java.util.ArrayList;
import java.util.List;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponseFactory {

    public static RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse from(List<RecipeInstruction> recipeInstructions, List<IdeaRecipeIngredient> ideaRecipeIngredients, Integer feasibility) {
        // Convert recipeInstructions and ideaRecipeIngredients to string arrays
        List<String> recipeInstructionStrings = null, ideaRecipeIngredientStrings = null;

        if (recipeInstructions != null && !recipeInstructions.isEmpty()) {
            recipeInstructionStrings = new ArrayList<>();

            for (RecipeInstruction instruction: recipeInstructions) {
                recipeInstructionStrings.add(instruction.getText());
            }
        }

        if (ideaRecipeIngredients != null && !ideaRecipeIngredients.isEmpty()) {
            ideaRecipeIngredientStrings = new ArrayList<>();

            for (IdeaRecipeIngredient ingredient: ideaRecipeIngredients) {
                ideaRecipeIngredientStrings.add(ingredient.getName());
            }
        }

        // Construct RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse
        RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse response = new RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse(
                recipeInstructionStrings,
                ideaRecipeIngredientStrings,
                feasibility
        );

        return response;
    }

}
