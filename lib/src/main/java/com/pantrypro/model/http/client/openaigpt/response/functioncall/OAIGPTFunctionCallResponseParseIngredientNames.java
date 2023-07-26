package com.pantrypro.model.http.client.openaigpt.response.functioncall;

import java.util.List;

public class OAIGPTFunctionCallResponseParseIngredientNames {

    private List<String> ingredientNames;

    public OAIGPTFunctionCallResponseParseIngredientNames() {

    }

    public OAIGPTFunctionCallResponseParseIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

}
