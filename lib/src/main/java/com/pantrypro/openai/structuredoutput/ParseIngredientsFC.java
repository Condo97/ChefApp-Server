package com.pantrypro.openai.structuredoutput;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

@JSONSchema(name = "parse_ingredient_names", functionDescription = "Parses ingredient names from ingredient and measurement list", strict = JSONSchema.NullableBool.TRUE)
public class ParseIngredientsFC {

    @JSONSchemaParameter(description = "The ingredient names without measurements or amounts")
    private String ingredientNames;

    public ParseIngredientsFC() {

    }

    public String getIngredientNames() {
        return ingredientNames;
    }

}
