package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections {

    private OAIGPTChatCompletionRequestFunctionObjectArray directions;
    private OAIGPTChatCompletionRequestFunctionObjectInteger feasibility;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections(OAIGPTChatCompletionRequestFunctionObjectArray directions, OAIGPTChatCompletionRequestFunctionObjectInteger feasibility) {
        this.directions = directions;
        this.feasibility = feasibility;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getDirections() {
        return directions;
    }

    public OAIGPTChatCompletionRequestFunctionObjectInteger getFeasibility() {
        return feasibility;
    }

}
