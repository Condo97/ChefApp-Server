package com.pantrypro.model.http.server.request;

public class CreateRecipeIdeaRequest extends AuthRequest {

    private String ingredients, modifiers;
    private Integer expandIngredientsMagnitude;

    public CreateRecipeIdeaRequest() {

    }

    public CreateRecipeIdeaRequest(String authToken, String ingredients, String modifiers, Integer expandIngredientsMagnitude) {
        super(authToken);
        this.ingredients = ingredients;
        this.modifiers = modifiers;
        this.expandIngredientsMagnitude = expandIngredientsMagnitude;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getModifiers() {
        return modifiers;
    }

    public Integer getExpandIngredientsMagnitude() {
        return expandIngredientsMagnitude;
    }

}
