package com.pantrypro.networking.server.response;

import java.util.List;
import java.util.Map;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse {

    private List<String> allIngredientsAndMeasurements;
    private Map<Integer, String> instructions;
    private Integer estimatedServings;
    private Integer feasibility;


    // LEGACY
    private List<String> recipeDirections;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<String> ideaRecipeIngredients;

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse() {

    }

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse(List<String> allIngredientsAndMeasurements, Map<Integer, String> instructions, Integer estimatedServings, Integer feasibility, List<String> recipeDirections) {
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.instructions = instructions;
        this.estimatedServings = estimatedServings;
        this.feasibility = feasibility;
        this.recipeDirections = recipeDirections;
    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public Map<Integer, String> getInstructions() {
        return instructions;
    }

    public Integer getEstimatedServings() {
        return estimatedServings;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

    // LEGACY

    public List<String> getRecipeDirections() {
        return recipeDirections;
    }

}
