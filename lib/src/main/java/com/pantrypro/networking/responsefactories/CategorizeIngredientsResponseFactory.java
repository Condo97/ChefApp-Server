package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.compoundobjects.IngredientAndCategory;
import com.pantrypro.networking.server.response.CategorizeIngredientsResponse;

import java.util.ArrayList;
import java.util.List;

public class CategorizeIngredientsResponseFactory {

    public static CategorizeIngredientsResponse from(List<IngredientAndCategory> ingredientsAndCategories) {
        List<CategorizeIngredientsResponse.IngredientCategory> ingredientCategories = new ArrayList<>();

        // Parse to ingredient categories list
        ingredientsAndCategories.forEach(iwc -> ingredientCategories.add(new CategorizeIngredientsResponse.IngredientCategory(iwc.getIngredient(), iwc.getCategory())));

        // Create CategorizeIngredientsResponse
        CategorizeIngredientsResponse categorizeIngredientsResponse = new CategorizeIngredientsResponse(ingredientCategories);

        return categorizeIngredientsResponse;
    }

}
