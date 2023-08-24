//package com.pantrypro.database.adapters.oai;
//
//import com.pantrypro.database.objects.recipe.Recipe;
//import com.pantrypro.database.objects.recipe.RecipeDirection;
//import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
//import com.pantrypro.model.http.client.oaifunctioncall.response.functioncall.OAIGPTFunctionCallResponseCreateFinishedRecipe;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecipeFromOpenAIAdapter {
//
//    public static Recipe getRecipe(Integer ideaID, OAIGPTFunctionCallResponseCreateFinishedRecipe oaiFunctionCallResponse) {
//        Recipe recipe = new Recipe(ideaID, oaiFunctionCallResponse.getEstimatedTotalCalories(), oaiFunctionCallResponse.getEstimatedTotalMinutes(), oaiFunctionCallResponse.getEstimatedServings(), oaiFunctionCallResponse.getFeasibility());
//
//        return recipe;
//    }
//
//    public static List<RecipeDirection> getRecipeInstructions(OAIGPTFunctionCallResponseCreateFinishedRecipe oaiFunctionCallResponse) {
//        List<RecipeDirection> instructions = new ArrayList<>();
//
//        // RecipeID is set after database call TODO: Is this fine?
//        for (String instruction: oaiFunctionCallResponse.getInstructions()) {
//            instructions.add(new RecipeDirection(instruction));
//        }
//
//        return instructions;
//    }
//
//    public static List<RecipeMeasuredIngredient> getRecipeMeasuredIngredients(OAIGPTFunctionCallResponseCreateFinishedRecipe oaiFunctionCallResponse) {
//        List<RecipeMeasuredIngredient> measuredIngredients = new ArrayList<>();
//
//        // RecipeID is set after database call TODO: Is this fine?
//        for (String measuredIngredient: oaiFunctionCallResponse.getAllIngredientsAndMeasurements()) {
//            measuredIngredients.add(new RecipeMeasuredIngredient(measuredIngredient));
//        }
//
//        return measuredIngredients;
//    }
//
//
//}
