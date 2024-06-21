package com.pantrypro.networking.server.request;

import java.util.List;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest extends AuthRequest {

//    public static class IngredientsAndMeasurements {
//
//        private String measuredngredient;
//
//        public IngredientsAndMeasurements() {
//
//        }
//
//        public IngredientsAndMeasurements(String ingredient, String measurement) {
//            this.ingredient = ingredient;
//            this.measurement = measurement;
//        }
//
//        public String getIngredient() {
//            return ingredient;
//        }
//
//        public String getMeasurement() {
//            return measurement;
//        }
//
//    }

    Integer recipeID;
    String newName;
    String newSummary;
    Integer newServings;
    List<String> measuredIngredients;

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest() {

    }

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest(String authToken, Integer recipeID, String newName, String newSummary, Integer newServings, List<String> measuredIngredients) {
        super(authToken);
        this.recipeID = recipeID;
        this.newName = newName;
        this.newSummary = newSummary;
        this.newServings = newServings;
        this.measuredIngredients = measuredIngredients;
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

    public Integer getNewServings() {
        return newServings;
    }

    public List<String> getMeasuredIngredients() {
        return measuredIngredients;
    }

}
