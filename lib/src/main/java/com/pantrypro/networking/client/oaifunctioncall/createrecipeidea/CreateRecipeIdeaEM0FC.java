package com.pantrypro.networking.client.oaifunctioncall.createrecipeidea;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "create_recipe_idea", functionDescription = "Creates a recipe")
public class CreateRecipeIdeaEM0FC implements CreateRecipeIdeaFC {

    @FCParameter(description = "All of the ingredients needed, no measurements")
    private List<String> ingredients;

    @FCParameter(description = "An interesting and fitting name for the recipe")
    private String name;

    @FCParameter(name = "short10To24WordEngagingInterestingSummary", description = "A short 10-24 word engaging summary for the recipe")
    private String summary;

    @FCParameter(description = "A 1-5 word cuisine type for the recipe")
    private String cuisineType;

    public CreateRecipeIdeaEM0FC() {

    }

    @Override
    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getCuisineType() {
        return cuisineType;
    }

}
