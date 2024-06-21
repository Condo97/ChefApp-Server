package com.pantrypro.networking.server.response;

import java.util.List;
import java.util.Map;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse {


    private Map<Integer, String> instructions;
    private Integer feasibility;
    // TODO: Estimated servings


    // LEGACY
    private List<String> recipeDirections;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<String> ideaRecipeIngredients;

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse() {

    }

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse(Map<Integer, String> instructions, Integer feasibility, List<String> recipeDirections) {
        this.instructions = instructions;
        this.feasibility = feasibility;
        this.recipeDirections = recipeDirections;
    }

    public Map<Integer, String> getInstructions() {
        return instructions;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

    public List<String> getRecipeDirections() {
        return recipeDirections;
    }

}
