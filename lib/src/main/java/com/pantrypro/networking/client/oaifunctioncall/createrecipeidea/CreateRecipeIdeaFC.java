package com.pantrypro.networking.client.oaifunctioncall.createrecipeidea;

import java.util.List;

public interface CreateRecipeIdeaFC {

    List<String> getIngredients();
    String getName();
    String getSummary();
    String getCuisineType();

}
