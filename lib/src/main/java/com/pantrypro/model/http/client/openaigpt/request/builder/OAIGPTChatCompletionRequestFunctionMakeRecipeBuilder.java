package com.pantrypro.model.http.client.openaigpt.request.builder;

import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
import com.pantrypro.model.http.client.openaigpt.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe;

import java.util.List;

public class OAIGPTChatCompletionRequestFunctionMakeRecipeBuilder {

    private static final String defaultFunctionDescription = "Creates a recipe from the given summary and ingredients";
    private static final String defaultInstructionsDescription = "The instructions to make the recipe";
    private static final String defaultAllIngredientsAndMeasurementsDescription = "All of the ingredients with measurements needed to make this recipe";
    private static final String defaultEstimatedTotalMinutesDescription = "The total estimated time in minutes needed to make the recipe";
    private static final String defaultFeasibilityDescription = "On a scale of 1-10, how feasible the recipe is to make in reality";

    public static OAIGPTChatCompletionRequestFunction build() {
        return build(defaultFunctionDescription, defaultInstructionsDescription, defaultAllIngredientsAndMeasurementsDescription, defaultEstimatedTotalMinutesDescription, defaultFeasibilityDescription);
    }

    public static OAIGPTChatCompletionRequestFunction build(String functionDescription, String instructionsDescription, String allIngredientsAndMeasurementsDescription, String estimatedTotalMinutesDescription, String feasibilityDescription) {
        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe
        OAIGPTChatCompletionRequestFunctionObjectArray instructions, allIngredientsAndMeasurements;
        OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalMinutes, feasibility;

        instructions = new OAIGPTChatCompletionRequestFunctionObjectArray(
                instructionsDescription,
                new OAIGPTChatCompletionRequestFunctionObjectString()
        );

        allIngredientsAndMeasurements = new OAIGPTChatCompletionRequestFunctionObjectArray(
                allIngredientsAndMeasurementsDescription,
                new OAIGPTChatCompletionRequestFunctionObjectString()
        );

        estimatedTotalMinutes = new OAIGPTChatCompletionRequestFunctionObjectInteger(
                estimatedTotalMinutesDescription
        );

        feasibility = new OAIGPTChatCompletionRequestFunctionObjectInteger(
                feasibilityDescription
        );

        // Create object properties
        OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe r = new OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe(
                instructions,
                allIngredientsAndMeasurements,
                estimatedTotalMinutes,
                feasibility
        );

        // Create container for object properties TODO: The strings are the required functions, make these stored somewhere better.. These have to be the same as OAIGPTFunctionCallResponseMakeRecipe
        OAIGPTChatCompletionRequestFunctionObjectObject rContainer = new OAIGPTChatCompletionRequestFunctionObjectObject(r, null, List.of(
                "instructions",
                "allIngredientsAndMeasurements",
                "estimatedTotalMinutes",
                "feasibility"
        ));

        // Create function from container
        OAIGPTChatCompletionRequestFunction rFunction = new OAIGPTChatCompletionRequestFunction(
                "make_recipe",
                functionDescription,
                rContainer
        );

        return rFunction;
    }

}
