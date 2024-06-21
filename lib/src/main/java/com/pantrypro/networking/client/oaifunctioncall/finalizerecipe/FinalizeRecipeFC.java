package com.pantrypro.networking.client.oaifunctioncall.finalizerecipe;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "make_recipe", functionDescription = "Creates a recipe from the given summary and ingredients, adding measurements to ingredients")
public class FinalizeRecipeFC {

    @FCParameter(description = "The instructions to make the recipe. Do not include enumeration indicators")
    private List<String> instructions;

    @FCParameter(description = "All of the ingredients including amounts/measurements needed to make this recipe")
    private List<String> allIngredientsAndMeasurements;

    @FCParameter(description = "Estimated total number of calories for the recipe")
    private Integer estimatedTotalCalories;

    @FCParameter(description = "Estimated total time in minutes needed to make the recipe")
    private Integer estimatedTotalMinutes;

    @FCParameter(description = "Estimated number of servings the recipe makes")
    private Integer estimatedServings;

    @FCParameter(description = "On a scale of 1-10, how feasible the recipe is to make in reality")
    private Integer feasibility;

    public FinalizeRecipeFC() {

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
