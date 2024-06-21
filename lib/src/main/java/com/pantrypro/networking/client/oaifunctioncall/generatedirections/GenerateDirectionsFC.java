package com.pantrypro.networking.client.oaifunctioncall.generatedirections;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "generate_directions", functionDescription = "Generates directions to make the recipe")
public class GenerateDirectionsFC {

    @FCParameter(description = "The directions to make the recipe. Do not include enumeration indicators.")
    private List<String> directions;

    @FCParameter(description = "On a scale of 1-10, how feasible the recipe is to make in reality")
    private Integer feasibility;

    public GenerateDirectionsFC() {

    }

    public List<String> getDirections() {
        return directions;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
