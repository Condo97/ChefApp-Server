package com.pantrypro.model.http.server.request;

import java.util.List;

public class CategorizeIngredientsRequest extends AuthRequest {

    private List<String> ingredients;
    private String store;

    public CategorizeIngredientsRequest() {

    }

    public CategorizeIngredientsRequest(String authToken, List<String> ingredients, String store) {
        super(authToken);
        this.ingredients = ingredients;
        this.store = store;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getStore() {
        return store;
    }

}
