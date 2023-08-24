package com.pantrypro.networking.server.request;

import java.util.List;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest extends AuthRequest {

    public static class IngredientsAndMeasurements {

        private String ingredient;
        private String measurement;

        public IngredientsAndMeasurements() {

        }

        public IngredientsAndMeasurements(String ingredient, String measurement) {
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

    Integer recipeID;
    String newName;
    String newSummary;
    List<IngredientsAndMeasurements> ingredientsAndMeasurements;

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest() {

    }

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest(String authToken, Integer recipeID, String newName, String newSummary, List<IngredientsAndMeasurements> ingredientsAndMeasurements) {
        super(authToken);
        this.recipeID = recipeID;
        this.newName = newName;
        this.newSummary = newSummary;
        this.ingredientsAndMeasurements = ingredientsAndMeasurements;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public String getNewName() {
        return newName;
    }

    public String getNewSummary() {
        return newSummary;
    }

    public List<IngredientsAndMeasurements> getIngredientsAndMeasurements() {
        return ingredientsAndMeasurements;
    }

}
