package com.pantrypro.model.http.client.openaigpt.response.functioncall;

import java.util.List;

public class OAIGPTFunctionCallResponseMakeRecipe {

    private List<String> instructions, allIngredientsAndMeasurements;
    private Integer estimatedTotalMinutes;

    public OAIGPTFunctionCallResponseMakeRecipe() {

    }

    public List<String> getInstructions() {
        return instructions;
    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public Integer getEstimatedTotalMinutes() {
        return estimatedTotalMinutes;
    }

}
