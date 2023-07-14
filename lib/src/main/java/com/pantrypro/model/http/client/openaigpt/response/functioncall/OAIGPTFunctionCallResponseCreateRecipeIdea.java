package com.pantrypro.model.http.client.openaigpt.response.functioncall;

import java.util.List;

public class OAIGPTFunctionCallResponseCreateRecipeIdea {

    private List<String> ingredients;
//    private List<String> equipment;
    private String name, short10To40WordEngagingInterestingSummary, cuisineType;

    public OAIGPTFunctionCallResponseCreateRecipeIdea() {

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

    public String getShort10To40WordEngagingInterestingSummary() {
        return short10To40WordEngagingInterestingSummary;
    }

    public String getCuisineType() {
        return cuisineType;
    }

}
