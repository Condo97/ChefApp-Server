package com.pantrypro.networking.server.response;

import java.util.List;

public class CreateIdeaRecipeResponse {

    private List<String> ingredients;
//    private List<String> equipment;
    private String name, summary, cuisineType;
    private Integer recipeID;
    private Integer ideaID;
    private Long remaining;

    public CreateIdeaRecipeResponse() {

    }

    public CreateIdeaRecipeResponse(List<String> ingredients, String name, String summary, String cuisineType, Integer recipeID, Long remaining) {
        this.ingredients = ingredients;
//        this.equipment = equipment;
        this.name = name;
        this.summary = summary;
        this.cuisineType = cuisineType;
        this.recipeID = recipeID;
        this.ideaID = recipeID;
        this.remaining = remaining;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

//    public List<String> getEquipment() {
//        return equipment;
//    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public Long getRemaining() {
        return remaining;
    }

    // LEGACY //

    public Integer getIdeaID() {
        return ideaID;
    }

}
