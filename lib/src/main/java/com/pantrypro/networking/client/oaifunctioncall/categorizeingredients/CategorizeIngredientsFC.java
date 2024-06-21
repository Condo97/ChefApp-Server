package com.pantrypro.networking.client.oaifunctioncall.categorizeingredients;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "categorize_ingredients", functionDescription = "Categorizes ingredients for easy finding in a list.")
public class CategorizeIngredientsFC {

    public static class IngredientsWithCategories {

        @FCParameter(description = "The ingredient")
        private String ingredient;

        @FCParameter(description = "The category for the ingredient")
        private String category;

        public IngredientsWithCategories() {

        }

        public IngredientsWithCategories(String ingredient, String category) {
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

    @FCParameter(description = "A list of ingredients each mapped to a category")
    List<IngredientsWithCategories> ingredientsWithCategories;

    public CategorizeIngredientsFC() {

    }

    public CategorizeIngredientsFC(List<IngredientsWithCategories> ingredientsWithCategories) {
        this.ingredientsWithCategories = ingredientsWithCategories;
    }

    public List<IngredientsWithCategories> getIngredientsWithCategories() {
        return ingredientsWithCategories;
    }

}
