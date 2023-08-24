package com.pantrypro.networking.server.response;

import java.util.List;
import java.util.Map;

public class MakeRecipeResponse {

    public static class MakeRecipeResponseIngredientAndMeasurement {

        private String ingredient;
        private String measurement;

        public MakeRecipeResponseIngredientAndMeasurement() {

        }

        public MakeRecipeResponseIngredientAndMeasurement(String ingredient, String measurement) {
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

    private Map<Integer, String> directions;
//    private List<String> allIngredientsAndMeasurements;
    private List<MakeRecipeResponseIngredientAndMeasurement> ingredientsAndMeasurements;
    private Integer estimatedTotalCalories, estimatedTotalMinutes, estimatedServings, feasibility;

    public MakeRecipeResponse() {

    }

    public MakeRecipeResponse(Map<Integer, String> directions, List<MakeRecipeResponseIngredientAndMeasurement> ingredientsAndMeasurements, Integer estimatedTotalCalories, Integer estimatedTotalMinutes, Integer estimatedServings, Integer feasibility) {
        this.directions = directions;
        this.ingredientsAndMeasurements = ingredientsAndMeasurements;
        this.estimatedTotalCalories = estimatedTotalCalories;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
        this.estimatedServings = estimatedServings;
        this.feasibility = feasibility;
    }

    public Map<Integer, String> getDirections() {
        return directions;
    }

    public List<MakeRecipeResponseIngredientAndMeasurement> getIngredientsAndMeasurements() {
        return ingredientsAndMeasurements;
    }

    public Integer getEstimatedTotalCalories() {
        return estimatedTotalCalories;
    }

    public Integer getEstimatedTotalMinutes() {
        return estimatedTotalMinutes;
    }

    public Integer getEstimatedServings() {
        return estimatedServings;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
