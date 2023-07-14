package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea {

    private OAIGPTChatCompletionRequestFunctionObjectArray tags;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesTagRecipeIdea(OAIGPTChatCompletionRequestFunctionObjectArray tags) {
        this.tags = tags;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getTags() {
        return tags;
    }

}
