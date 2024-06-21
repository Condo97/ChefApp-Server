package com.pantrypro.networking.client.oaifunctioncall.generatedirections;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "generate_recipe", functionDescription = "Generates measured ingredients and directions to make the recipe")
public class GenerateMeasuredIngredientsAndDirectionsFC {

    @FCParameter(description = "All of the ingredients including amounts/measurements needed to make this recipe")
    private List<String> allIngredientsAndMeasurements;

    @FCParameter(description = "The directions to make the recipe. Do not include enumeration indicators.")
    private List<String> directions;

    @FCParameter(description = "On a scale of 1-10, how feasible the recipe is to make in reality")
    private Integer feasibility;

    public GenerateMeasuredIngredientsAndDirectionsFC() {

    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public List<String> getDirections() {
        return directions;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
