package com.pantrypro.core.generation.tagging;

import com.pantrypro.model.database.objects.IdeaRecipeTag;

import java.util.List;

public class TagFilterer {

    public static void removeInvalidTags(List<IdeaRecipeTag> tags) {
        // Get valid tags from TagFetcher
        List<String> validTags = TagFetcher.fetchTagsLowercase();

        // Remove all not valid tags
        tags.removeIf(t -> {
            if (!validTags.contains(t.getTagLowercase())) {
                // TODO: Better logging
                System.out.println("Removed tag \"" + t.getTagLowercase() + "\"");

                return true;
            }

            return false;
        });
    }

}
