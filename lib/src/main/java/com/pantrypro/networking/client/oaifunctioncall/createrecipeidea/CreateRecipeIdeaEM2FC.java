package com.pantrypro.networking.client.oaifunctioncall.createrecipeidea;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

import java.util.List;

@JSONSchema(name = "create_recipe_idea", functionDescription = "Creates a recipe from ingredients, adding as necessary")
public class CreateRecipeIdeaEM2FC implements CreateRecipeIdeaFC {

    @JSONSchemaParameter(description = "All of the ingredients needed, no measurements, adding as necessary")
    private List<String> ingredients;

    @JSONSchemaParameter(description = "An interesting and fitting name for the recipe")
    private String name;

    @JSONSchemaParameter(name = "short10To24WordEngagingInterestingSummary", description = "A short 10-24 word engaging summary for the recipe")
    private String summary;

    @JSONSchemaParameter(description = "A 1-5 word cuisine type for the recipe")
    private String cuisineType;

    public CreateRecipeIdeaEM2FC() {

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
