package com.pantrypro.model.http.server.response;

import java.util.List;
import java.util.Map;

public class MakeRecipeResponse {

    private Map<Integer, String> instructions;
    private List<String> allIngredientsAndMeasurements;
    private Integer estimatedTotalCalories, estimatedTotalMinutes, estimatedServings, feasibility;

    public MakeRecipeResponse() {

    }

    public MakeRecipeResponse(Map<Integer, String> instructions, List<String> allIngredientsAndMeasurements, Integer estimatedTotalCalories, Integer estimatedTotalMinutes, Integer estimatedServings, Integer feasibility) {
        this.instructions = instructions;
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.estimatedTotalCalories = estimatedTotalCalories;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
        this.estimatedServings = estimatedServings;
        this.feasibility = feasibility;
    }

    public Map<Integer, String> getInstructions() {
        return instructions;
    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
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
