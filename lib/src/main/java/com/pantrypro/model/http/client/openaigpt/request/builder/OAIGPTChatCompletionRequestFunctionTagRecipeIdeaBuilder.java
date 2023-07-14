package com.pantrypro.model.http.client.openaigpt.request.builder;

import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
import com.pantrypro.core.generation.tagging.TagFetcher;
import com.pantrypro.model.http.client.openaigpt.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea;

import java.util.List;

public class OAIGPTChatCompletionRequestFunctionTagRecipeIdeaBuilder {

    private static final String defaultFunctionDescription = "Gets tags that represent the recipe";
    private static final String defaultTagsBaseDescription = "2-5 relevant tags that apply to the recipe from the following list:";

    public static OAIGPTChatCompletionRequestFunction build() {
        final String SPACE_STRING = " ";

        // Append tags to defaultTagsBaseDescription
        StringBuilder sb = new StringBuilder(defaultTagsBaseDescription);
        for (String tag: TagFetcher.fetchTagsLowercase()) {
            sb.append(SPACE_STRING);
            sb.append(tag);
        }

        return build(defaultFunctionDescription, sb.toString());
    }

    private static OAIGPTChatCompletionRequestFunction build(String functionDescription, String tagsBaseDescription) {
        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipe
        OAIGPTChatCompletionRequestFunctionObjectArray tags;

        tags = new OAIGPTChatCompletionRequestFunctionObjectArray(
                tagsBaseDescription,
                new OAIGPTChatCompletionRequestFunctionObjectString()
        );

        OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea t = new OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea(tags);

        // Create the OAIGPTChatCompletionRequestFunctionObjectObject
        OAIGPTChatCompletionRequestFunctionObjectObject tContainer = new OAIGPTChatCompletionRequestFunctionObjectObject(t, null, List.of(
                "tags"
        ));

        // Create OAIGPTChatCompletionRequestFunction
        OAIGPTChatCompletionRequestFunction tFunction = new OAIGPTChatCompletionRequestFunction(
                "tag_recipe",
                functionDescription,
                tContainer
        );

        return tFunction;
    }

}
