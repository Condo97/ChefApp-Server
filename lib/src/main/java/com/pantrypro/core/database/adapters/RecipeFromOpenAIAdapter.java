package com.pantrypro.core.database.adapters;

import com.pantrypro.model.database.objects.Recipe;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseMakeRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeFromOpenAIAdapter {

    public static Recipe getRecipe(Integer ideaID, OAIGPTFunctionCallResponseMakeRecipe oaiFunctionCallResponse) {
        Recipe recipe = new Recipe(ideaID, oaiFunctionCallResponse.getEstimatedTotalMinutes());

        return recipe;
    }

    public static List<RecipeInstruction> getRecipeInstructions(OAIGPTFunctionCallResponseMakeRecipe oaiFunctionCallResponse) {
        List<RecipeInstruction> instructions = new ArrayList<>();

        // RecipeID is set after database call TODO: Is this fine?
        for (String instruction: oaiFunctionCallResponse.getInstructions()) {
            instructions.add(new RecipeInstruction(instruction));
        }

        return instructions;
    }

    public static List<RecipeMeasuredIngredient> getRecipeMeasuredIngredients(OAIGPTFunctionCallResponseMakeRecipe oaiFunctionCallResponse) {
        List<RecipeMeasuredIngredient> measuredIngredients = new ArrayList<>();

        // RecipeID is set after database call TODO: Is this fine?
        for (String measuredIngredient: oaiFunctionCallResponse.getAllIngredientsAndMeasurements()) {
            measuredIngredients.add(new RecipeMeasuredIngredient(measuredIngredient));
        }

        return measuredIngredients;
    }


}
