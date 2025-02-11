package com.pantrypro.openai.structuredoutput;

import com.oaigptconnector.model.JSONSchemaParameter;
import com.oaigptconnector.model.JSONSchema;

import java.util.List;

@JSONSchema(name = "tag_recipe", functionDescription = "Gets tags that represent the recipe", strict = JSONSchema.NullableBool.TRUE)
public class TagRecipeSO {

    @JSONSchemaParameter(description = "2-5 relevant tags that apply to the recipe from the included list")
    private List<String> tags;

    public TagRecipeSO() {

    }

    public List<String> getTags() {
        return tags;
    }

}
