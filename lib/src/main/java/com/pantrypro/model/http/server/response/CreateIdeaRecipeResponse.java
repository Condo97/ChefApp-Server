package com.pantrypro.model.http.server.response;

import java.util.List;

public class CreateIdeaRecipeResponse {

    private List<String> ingredients;
//    private List<String> equipment;
    private String name, summary, cuisineType;
    private Integer ideaID;
    private Long remaining;

    public CreateIdeaRecipeResponse() {

    }

    public CreateIdeaRecipeResponse(List<String> ingredients, String name, String summary, String cuisineType, Integer ideaID, Long remaining) {
        this.ingredients = ingredients;
//        this.equipment = equipment;
        this.name = name;
        this.summary = summary;
        this.cuisineType = cuisineType;
        this.ideaID = ideaID;
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

    public Integer getIdeaID() {
        return ideaID;
    }

    public Long getRemaining() {
        return remaining;
    }

}
