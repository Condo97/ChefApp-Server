package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames {

    private OAIGPTChatCompletionRequestFunctionObjectArray ingredientNames;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesParseIngredientNames(OAIGPTChatCompletionRequestFunctionObjectArray ingredientNames) {
        this.ingredientNames = ingredientNames;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getIngredientNames() {
        return ingredientNames;
    }

}
