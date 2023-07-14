package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe {

    private OAIGPTChatCompletionRequestFunctionObjectArray instructions;
    private OAIGPTChatCompletionRequestFunctionObjectArray allIngredientsAndMeasurements;
    private OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalMinutes;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe(OAIGPTChatCompletionRequestFunctionObjectArray instructions, OAIGPTChatCompletionRequestFunctionObjectArray allIngredientsAndMeasurements, OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalMinutes) {
        this.instructions = instructions;
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getInstructions() {
        return instructions;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getAllIngredientsAndMeasurements() {
        return allIngredientsAndMeasurements;
    }

    public OAIGPTChatCompletionRequestFunctionObjectInteger getEstimatedTotalMinutes() {
        return estimatedTotalMinutes;
    }

}
