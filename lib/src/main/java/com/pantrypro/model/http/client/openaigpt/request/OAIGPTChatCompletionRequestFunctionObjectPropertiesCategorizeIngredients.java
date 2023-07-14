package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients {

    private OAIGPTChatCompletionRequestFunctionObjectArray ingredientsWithCategories;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients(OAIGPTChatCompletionRequestFunctionObjectArray ingredientsWithCategories) {
        this.ingredientsWithCategories = ingredientsWithCategories;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getIngredientsWithCategories() {
        return ingredientsWithCategories;
    }

}
