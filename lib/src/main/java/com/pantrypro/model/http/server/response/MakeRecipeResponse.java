package com.pantrypro.model.http.server.response;

import java.util.List;
import java.util.Map;

public class MakeRecipeResponse {

    private Map<Integer, String> instructions;
    private List<String> allIngredientsAndMeasurements;
    private Integer estimatedTotalMinutes;

    public MakeRecipeResponse() {

    }

    public MakeRecipeResponse(Map<Integer, String> instructions, List<String> allIngredientsAndMeasurements, Integer estimatedTotalMinutes) {
        this.instructions = instructions;
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
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

}
