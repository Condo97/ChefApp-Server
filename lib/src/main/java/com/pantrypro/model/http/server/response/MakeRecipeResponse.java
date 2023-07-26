package com.pantrypro.model.http.server.response;

import java.util.List;
import java.util.Map;

public class MakeRecipeResponse {

    private Map<Integer, String> instructions;
    private List<String> allIngredientsAndMeasurements;
    private Integer estimatedTotalMinutes, feasibility;

    public MakeRecipeResponse() {

    }

    public MakeRecipeResponse(Map<Integer, String> instructions, List<String> allIngredientsAndMeasurements, Integer estimatedTotalMinuts, Integer feasibility) {
        this.instructions = instructions;
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
        this.feasibility = feasibility;
    }

    public Map<Integer, String> getInstructions() {
        return instructions;
    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public Integer getEstimatedTotalMinutes() {
        return estimatedTotalMinutes;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
