package com.pantrypro.database.compoundobjects;

public class IngredientAndCategory {

    private String ingredient;
    private String category;

    public IngredientAndCategory() {

    }

    public IngredientAndCategory(String ingredient, String category) {
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
