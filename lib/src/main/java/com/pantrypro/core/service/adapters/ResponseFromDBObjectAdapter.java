package com.pantrypro.core.service.adapters;

import com.pantrypro.model.database.objects.*;
import com.pantrypro.model.http.server.response.CreateRecipeIdeaResponse;
import com.pantrypro.model.http.server.response.MakeRecipeResponse;
import com.pantrypro.model.http.server.response.RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse;
import com.pantrypro.model.http.server.response.TagRecipeIdeaResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseFromDBObjectAdapter {

    public static CreateRecipeIdeaResponse from(IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ideaRecipeIngredients, Long remaining) {
        List<String> ingredientStrings = new ArrayList<>();
        List<String> equipmentStrings = new ArrayList<>();

        ideaRecipeIngredients.forEach(ingredient -> ingredientStrings.add(ingredient.getName()));
//        ideaRecipeEquipment.forEach(equipmentObject -> equipmentStrings.add(equipmentObject.getName()));

        CreateRecipeIdeaResponse response = new CreateRecipeIdeaResponse(
                ingredientStrings,
//                equipmentStrings,
                ideaRecipe.getName(),
                ideaRecipe.getSummary(),
                ideaRecipe.getCuisineType(),
                ideaRecipe.getId(),
                remaining
        );

        return response;
    }

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
        Integer estimatedTotalMinutes = recipe.getEstimated_total_minutes();
        Integer feasibility = recipe.getFeasibility();

        // Build response
        MakeRecipeResponse response = new MakeRecipeResponse(
                instructionStringsMap,
                allIngredientsAndMeasurementStrings,
                estimatedTotalMinutes,
                feasibility
        );

        return response;
    }

    public static TagRecipeIdeaResponse from(List<IdeaRecipeTag> tags) {
        List<String> tagStrings = new ArrayList<>();

        tags.forEach(tag -> {
            tagStrings.add(tag.getTagLowercase());
        });

        TagRecipeIdeaResponse response = new TagRecipeIdeaResponse(
                tagStrings
        );

        return response;
    }

}
