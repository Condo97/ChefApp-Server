package com.pantrypro.model.http.client.openaigpt.response.functioncall;

import java.util.List;

public class OAIGPTFunctionCallResponseMakeRecipe {

    private List<String> instructions, allIngredientsAndMeasurements;
    private Integer estimatedTotalCalories, estimatedTotalMinutes, estimatedServings, feasibility;

    public OAIGPTFunctionCallResponseMakeRecipe() {

    }

    public List<String> getInstructions() {
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
