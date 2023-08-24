package com.pantrypro.networking.client.oaifunctioncall.parseingredientnames;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

@FunctionCall(name = "parse_ingredient_names", functionDescription = "Parses ingredient names from ingredient and measurement list")
public class ParseIngredientsFC {

    @FCParameter(description = "The ingredient names without measurements or amounts")
    private String ingredientNames;

    public ParseIngredientsFC() {

    }

    public String getIngredientNames() {
        return ingredientNames;
    }

}
