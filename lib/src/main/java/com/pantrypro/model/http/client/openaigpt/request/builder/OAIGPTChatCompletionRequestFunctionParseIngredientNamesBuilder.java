package com.pantrypro.model.http.client.openaigpt.request.builder;

import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
import com.pantrypro.model.http.client.openaigpt.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames;

import java.util.List;

public class OAIGPTChatCompletionRequestFunctionParseIngredientNamesBuilder {

    private static final String defaultFunctionDescription = "Parses ingredient names from ingredient and measurement list";
    private static final String defaultIngredientNamesDescription = "The ingredient names";

    public static OAIGPTChatCompletionRequestFunction build() {
        return build(defaultFunctionDescription, defaultIngredientNamesDescription);
    }

    public static OAIGPTChatCompletionRequestFunction build(String functionDescription, String ingredientNamesDescription) {
        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames
        OAIGPTChatCompletionRequestFunctionObjectArray ingredients = new OAIGPTChatCompletionRequestFunctionObjectArray(
                ingredientNamesDescription,
                new OAIGPTChatCompletionRequestFunctionObjectString()
        );

        // Craete OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames
        OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames r = new OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames(
                ingredients
        );

        // Create the OAIGPTChatCompletionRequestFunctionObjectObject as container with required functions
        OAIGPTChatCompletionRequestFunctionObjectObject rContainer = new OAIGPTChatCompletionRequestFunctionObjectObject(r, null, List.of(
                "ingredientNames"
        ));

        // Create OAIGPTChatCompletionRequestFunction with rContainer and function call name
        OAIGPTChatCompletionRequestFunction rFunction = new OAIGPTChatCompletionRequestFunction(
                "parse_ingredient_names",
                functionDescription,
                rContainer
        );

        return rFunction;
    }

}
