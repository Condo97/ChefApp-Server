package com.pantrypro.openai.structuredoutput.createrecipeidea;

import java.util.List;

public interface CreateRecipeIdeaSO {

    List<String> getIngredients();
    String getName();
    String getSummary();
    String getCuisineType();

}
