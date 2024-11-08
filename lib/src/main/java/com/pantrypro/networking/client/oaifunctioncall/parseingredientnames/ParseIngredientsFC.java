package com.pantrypro.networking.client.oaifunctioncall.parseingredientnames;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

@JSONSchema(name = "parse_ingredient_names", functionDescription = "Parses ingredient names from ingredient and measurement list")
public class ParseIngredientsFC {

    @JSONSchemaParameter(description = "The ingredient names without measurements or amounts")
    private String ingredientNames;

    public ParseIngredientsFC() {

    }

    public String getIngredientNames() {
        return ingredientNames;
    }

}
