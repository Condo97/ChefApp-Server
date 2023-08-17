package com.pantrypro.core.service.responsefactories;

import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
import com.pantrypro.model.http.server.response.CreateIdeaRecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeIdeaResponseFactory {

    public static CreateIdeaRecipeResponse from(IdeaRecipe ideaRecipe, Long remaining) {
        List<String> ingredientStrings = new ArrayList<>();
        List<String> equipmentStrings = new ArrayList<>();

        ideaRecipe.getIngredients().forEach(ingredient -> ingredientStrings.add(ingredient.getName()));
//        ideaRecipeEquipment.forEach(equipmentObject -> equipmentStrings.add(equipmentObject.getName()));

        CreateIdeaRecipeResponse response = new CreateIdeaRecipeResponse(
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

}
