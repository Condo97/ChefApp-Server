package com.pantrypro.networking.server.response;

import java.util.List;

public class CategorizeIngredientsResponse {

    public static class IngredientCategory {
        public String ingredient;
        public String category;

        public IngredientCategory() {

        }

        public IngredientCategory(String ingredient, String category) {
            this.ingredient = ingredient;
            this.category = category;
        }

        public String getIngredient() {
            return ingredient;
        }

        public String getCategory() {
            return category;
        }
    }

    private List<IngredientCategory> ingredientCategories;

    public CategorizeIngredientsResponse() {

    }

    public CategorizeIngredientsResponse(List<IngredientCategory> ingredientCategories) {
        this.ingredientCategories = ingredientCategories;
    }

    public List<IngredientCategory> getIngredientCategories() {
        return ingredientCategories;
    }

}
