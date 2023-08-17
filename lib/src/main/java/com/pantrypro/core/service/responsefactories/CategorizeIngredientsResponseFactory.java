package com.pantrypro.core.service.responsefactories;

import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseCategorizeIngredients;
import com.pantrypro.model.http.server.response.CategorizeIngredientsResponse;

import java.util.ArrayList;
import java.util.List;

public class CategorizeIngredientsResponseFactory {

    public static CategorizeIngredientsResponse from(OAIGPTFunctionCallResponseCategorizeIngredients oaigptFunctionCallResponseCategorizeIngredients) {
        List<CategorizeIngredientsResponse.IngredientCategory> ingredientCategories = new ArrayList<>();

        // Parse to ingredient categories list
        oaigptFunctionCallResponseCategorizeIngredients.getIngredientsWithCategories().forEach(iwc -> ingredientCategories.add(new CategorizeIngredientsResponse.IngredientCategory(iwc.getIngredient(), iwc.getCategory())));

        // Create CategorizeIngredientsResponse
        CategorizeIngredientsResponse categorizeIngredientsResponse = new CategorizeIngredientsResponse(ingredientCategories);

        return categorizeIngredientsResponse;
    }

}
