package com.pantrypro.networking.client.oaifunctioncall.generatedirections;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

import java.util.List;

@JSONSchema(name = "generate_recipe", functionDescription = "Generates measured ingredients and directions to make the recipe", strict = JSONSchema.NullableBool.TRUE)
public class GenerateMeasuredIngredientsAndDirectionsSO {

    @JSONSchemaParameter(description = "The ingredients given in the input with correct measurements.")
    private List<String> allIngredientsAndMeasurements;

    @JSONSchemaParameter(description = "The directions to make the recipe. Do not include enumeration indicators.")
    private List<String> directions;

    @JSONSchemaParameter(description = "The estimated servings for the recipe")
    private Integer estimatedServings;

    @JSONSchemaParameter(description = "On a scale of 1-10, how feasible the recipe is to make in reality")
    private Integer feasibility;

    public GenerateMeasuredIngredientsAndDirectionsSO() {

    }

    public List<String> getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public List<String> getDirections() {
        return directions;
    }

    public Integer getEstimatedServings() {
        return estimatedServings;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
