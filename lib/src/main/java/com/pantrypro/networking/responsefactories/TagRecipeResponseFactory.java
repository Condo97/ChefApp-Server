package com.pantrypro.networking.responsefactories;

import com.pantrypro.database.objects.recipe.RecipeTag;
import com.pantrypro.networking.server.response.TagRecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class TagRecipeResponseFactory {

    public static TagRecipeResponse from(List<RecipeTag> tags) {
        List<String> tagStrings = new ArrayList<>();

        tags.forEach(tag -> {
            tagStrings.add(tag.getTag().toLowerCase());
        });

        TagRecipeResponse response = new TagRecipeResponse(
                tagStrings
        );

        return response;
    }


}
