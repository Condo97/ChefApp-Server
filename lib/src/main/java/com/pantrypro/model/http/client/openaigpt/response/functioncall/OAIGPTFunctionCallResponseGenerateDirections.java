package com.pantrypro.model.http.client.openaigpt.response.functioncall;

import java.util.List;

public class OAIGPTFunctionCallResponseGenerateDirections {

    private List<String> directions;
    private Integer feasibility;

    public OAIGPTFunctionCallResponseGenerateDirections() {

    }

    public OAIGPTFunctionCallResponseGenerateDirections(List<String> directions, Integer feasibility) {
        this.directions = directions;
        this.feasibility = feasibility;
    }

    public List<String> getDirections() {
        return directions;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}
