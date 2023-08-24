package com.pantrypro.database.compoundobjects;

public class IngredientAndMeasurement {

    private String ingredient;
    private String measurement;

    public IngredientAndMeasurement() {

    }

    public IngredientAndMeasurement(String ingredient, String measurement) {
        this.ingredient = ingredient;
        this.measurement = measurement;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasurement() {
        return measurement;
    }

}
