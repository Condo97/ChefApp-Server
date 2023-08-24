package com.pantrypro.core.generation.tagging;

import com.pantrypro.database.objects.recipe.RecipeTag;

import java.util.List;

public class TagFilterer {

    public static boolean isTagValid(RecipeTag tag) {
       return isTagValid(tag.getTag());
    }

    public static boolean isTagValid(String tag) {
        // Return true if tags from TagFetcher contains tag, otherwise return false
        for (String validTag: TagFetcher.getFetchedTagsLowercase())
            if (validTag.equals(tag.toLowerCase()))
                return true;

        return false;
    }

    public static void removeInvalidTags(List<RecipeTag> tags) {
        // Get valid tags from TagFetcher
        List<String> validTags = TagFetcher.getFetchedTagsLowercase();

        // Remove all not valid tags
        tags.removeIf(t -> {
            if (!validTags.contains(t.getTag().toLowerCase())) {
                // TODO: Better logging
                System.out.println("Removed tag \"" + t.getTag() + "\"");

                return true;
            }

            return false;
        });
    }

}
