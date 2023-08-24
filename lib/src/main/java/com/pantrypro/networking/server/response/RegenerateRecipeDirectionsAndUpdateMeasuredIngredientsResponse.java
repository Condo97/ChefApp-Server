package com.pantrypro.networking.server.response;

import java.util.List;

public class RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse {

    private List<String> recipeDirections;
    private Integer feasibility;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private List<String> ideaRecipeIngredients;

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse() {

    }

    public RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse(List<String> recipeDirections, Integer feasibility) {
        this.recipeDirections = recipeDirections;
//        this.ideaRecipeIngredients = ideaRecipeIngredients;
        this.feasibility = feasibility;
    }

    public List<String> getRecipeDirections() {
        return recipeDirections;
    }

//    public List<String> getIdeaRecipeIngredients() {
//        return ideaRecipeIngredients;
//    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
