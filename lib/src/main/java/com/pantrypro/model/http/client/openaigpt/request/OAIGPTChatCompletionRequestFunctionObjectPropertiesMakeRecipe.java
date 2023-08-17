package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe {

    private OAIGPTChatCompletionRequestFunctionObjectArray instructions;
    private OAIGPTChatCompletionRequestFunctionObjectArray allIngredientsAndMeasurements;
    private OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalCalories;
    private OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalMinutes;
    private OAIGPTChatCompletionRequestFunctionObjectInteger estimatedServings;
    private OAIGPTChatCompletionRequestFunctionObjectInteger feasibility;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe(OAIGPTChatCompletionRequestFunctionObjectArray instructions, OAIGPTChatCompletionRequestFunctionObjectArray allIngredientsAndMeasurements, OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalCalories, OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalMinutes, OAIGPTChatCompletionRequestFunctionObjectInteger estimatedServings, OAIGPTChatCompletionRequestFunctionObjectInteger feasibility) {
        this.instructions = instructions;
        this.allIngredientsAndMeasurements = allIngredientsAndMeasurements;
        this.estimatedTotalCalories = estimatedTotalCalories;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
        this.estimatedServings = estimatedServings;
        this.feasibility = feasibility;
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

    public OAIGPTChatCompletionRequestFunctionObjectInteger getEstimatedServings() {
        return estimatedServings;
    }

    public OAIGPTChatCompletionRequestFunctionObjectInteger getEstimatedTotalCalories() {
        return estimatedTotalCalories;
    }

    public OAIGPTChatCompletionRequestFunctionObjectInteger getFeasibility() {
        return feasibility;
    }

}
