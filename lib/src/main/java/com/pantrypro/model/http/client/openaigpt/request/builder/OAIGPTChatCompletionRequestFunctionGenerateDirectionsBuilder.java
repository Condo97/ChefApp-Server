package com.pantrypro.model.http.client.openaigpt.request.builder;

import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
import com.pantrypro.model.http.client.openaigpt.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections;

import java.util.List;

public class OAIGPTChatCompletionRequestFunctionGenerateDirectionsBuilder {

    private static final String defaultFunctionDescription = "Generates directions to make the recipe";
    private static final String defaultDirectionsDescription = "The directions to make the recipe";
    private static final String defaultFeasibilityDescription = "On a scale of 1-10, how feasible the recipe is to make in reality";

    public static OAIGPTChatCompletionRequestFunction build() {
        return build(defaultFunctionDescription, defaultDirectionsDescription, defaultFeasibilityDescription);
    }

    public static OAIGPTChatCompletionRequestFunction build(String functionDescription, String directionsDescription, String feasibilityDescription) {
        // Create directions and feasibility
        OAIGPTChatCompletionRequestFunctionObjectArray directions = new OAIGPTChatCompletionRequestFunctionObjectArray(
                directionsDescription,
                new OAIGPTChatCompletionRequestFunctionObjectString()
        );

        OAIGPTChatCompletionRequestFunctionObjectInteger feasibility = new OAIGPTChatCompletionRequestFunctionObjectInteger(
                feasibilityDescription
        );

        // Create OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections
        OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections r = new OAIGPTChatCompletionRequestFunctionObjectPropertiesGenerateDirections(
                directions,
                feasibility
        );

        // Create the OAIGPTChatCompletionRequestFunctionObjectObject as container with required functions
        OAIGPTChatCompletionRequestFunctionObjectObject rContainer = new OAIGPTChatCompletionRequestFunctionObjectObject(r, null, List.of(
                "directions",
                "feasibility"
        ));

        // Create OAIGPTChatCompletionRequestFunction with rContainer and function call name
        OAIGPTChatCompletionRequestFunction rFunction = new OAIGPTChatCompletionRequestFunction(
                "generate_directions",
                functionDescription,
                rContainer
        );

        return rFunction;
    }

}
