package com.pantrypro.core.database.adapters;

import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.database.objects.IdeaRecipeEquipment;
import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseCreateRecipeIdea;

import java.util.ArrayList;
import java.util.List;

public class IdeaRecipeFromOpenAIAdapter {

    public static IdeaRecipe getIdeaRecipe(Integer userID, String input, Integer expand_ingredients_magnitude, OAIGPTFunctionCallResponseCreateRecipeIdea oaiFunctionCallResponse) {
        IdeaRecipe ideaRecipe = new IdeaRecipe(
                userID,
                input,
                oaiFunctionCallResponse.getName(),
                oaiFunctionCallResponse.getShort10To40WordEngagingInterestingSummary(),
                oaiFunctionCallResponse.getCuisineType(),
                expand_ingredients_magnitude
        );

        return ideaRecipe;
    }

    public static List<IdeaRecipeIngredient> getIdeaRecipeIngredients(OAIGPTFunctionCallResponseCreateRecipeIdea oaiFunctionCallResponse) {
        List<IdeaRecipeIngredient> ingredients = new ArrayList<>();

        // IdeaID is set in database call TODO: Is this fine?
        for (String ingredient: oaiFunctionCallResponse.getIngredients()) {
            ingredients.add(new IdeaRecipeIngredient(ingredient));
        }

        return ingredients;
    }

//    public static List<IdeaRecipeEquipment> getIdeaRecipeEquipment(OAIGPTFunctionCallResponseCreateRecipeIdea oaiFunctionCallResponse) {
//        List<IdeaRecipeEquipment> equipment = new ArrayList<>();
//
//        // IdeaID is set in database call TODO: Is this fine?
//        for (String equipmentString: oaiFunctionCallResponse.getEquipment()) {
//            equipment.add(new IdeaRecipeEquipment(equipmentString));
//        }
//
//        return equipment;
//    }

}
