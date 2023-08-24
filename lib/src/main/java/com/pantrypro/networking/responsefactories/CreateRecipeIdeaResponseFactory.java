package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.networking.server.response.CreateIdeaRecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeIdeaResponseFactory {

    public static CreateIdeaRecipeResponse from(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients, Long remaining) {
        List<String> ingredientStrings = new ArrayList<>();
        List<String> equipmentStrings = new ArrayList<>();

        measuredIngredients.forEach(ingredient -> ingredientStrings.add(ingredient.getIngredientName()));
//        ideaRecipe.getIngredients().forEach(ingredient -> ingredientStrings.add(ingredient.getName()));
//        ideaRecipeEquipment.forEach(equipmentObject -> equipmentStrings.add(equipmentObject.getName()));

        CreateIdeaRecipeResponse response = new CreateIdeaRecipeResponse(
                ingredientStrings,
//                equipmentStrings,
                recipe.getName(),
                recipe.getSummary(),
                recipe.getRecipe_id(),
                remaining
        );

        return response;
    }

}
