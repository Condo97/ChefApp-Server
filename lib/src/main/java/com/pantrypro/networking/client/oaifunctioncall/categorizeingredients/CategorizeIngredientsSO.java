package com.pantrypro.networking.client.oaifunctioncall.categorizeingredients;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

import java.util.List;

@JSONSchema(name = "categorize_ingredients", functionDescription = "Categorizes ingredients for easy finding in a list.", strict = JSONSchema.NullableBool.TRUE)
public class CategorizeIngredientsSO {

    public static class IngredientsWithCategories {

        @JSONSchemaParameter(description = "The ingredient")
        private String ingredient;

        @JSONSchemaParameter(description = "The category for the ingredient")
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

    @JSONSchemaParameter(description = "A list of ingredients each mapped to a category")
    List<IngredientsWithCategories> ingredientsWithCategories;

    public CategorizeIngredientsSO() {

    }

    public CategorizeIngredientsSO(List<IngredientsWithCategories> ingredientsWithCategories) {
        this.ingredientsWithCategories = ingredientsWithCategories;
    }

    public List<IngredientsWithCategories> getIngredientsWithCategories() {
        return ingredientsWithCategories;
    }

}
