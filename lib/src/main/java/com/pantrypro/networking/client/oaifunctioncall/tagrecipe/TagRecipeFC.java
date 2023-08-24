package com.pantrypro.networking.client.oaifunctioncall.tagrecipe;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "tag_recipe", functionDescription = "Gets tags that represent the recipe")
public class TagRecipeFC {

    @FCParameter(description = "2-5 relevant tags that apply to the recipe from the included list")
    private List<String> tags;

    public TagRecipeFC() {

    }

    public List<String> getTags() {
        return tags;
    }

}
