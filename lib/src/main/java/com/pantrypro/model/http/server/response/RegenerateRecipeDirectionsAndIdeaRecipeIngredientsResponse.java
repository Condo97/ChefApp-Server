package com.pantrypro.model.http.server.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse {

    private List<String> recipeDirections;
    private Integer feasibility;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> ideaRecipeIngredients;

    public RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse() {

    }

    public RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse(List<String> recipeDirections, List<String> ideaRecipeIngredients, Integer feasibility) {
        this.recipeDirections = recipeDirections;
        this.ideaRecipeIngredients = ideaRecipeIngredients;
        this.feasibility = feasibility;
    }

    public List<String> getRecipeDirections() {
        return recipeDirections;
    }

    public List<String> getIdeaRecipeIngredients() {
        return ideaRecipeIngredients;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
