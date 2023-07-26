package com.pantrypro.core.generation.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.core.OpenAIGPTHttpsClientHelper;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
import com.pantrypro.Constants;
import com.pantrypro.core.generation.OpenAIGPTChatCompletionRequestFactory;
import com.pantrypro.keys.Keys;
import com.pantrypro.model.generation.IdeaRecipeExpandIngredients;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseCreateRecipeIdea;
import com.pantrypro.model.http.client.openaigpt.request.builder.OAIGPTChatCompletionRequestFunctionCreateRecipeIdeaBuilder;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

public class CreateRecipeIdeaGenerator {

    private static final String systemMessagePrefix = "You are a chef.";

    public static OAIGPTFunctionCallResponseCreateRecipeIdea generateCreateRecipeIdeaFunctionCall(String ingredientsString, String modifiersString, Integer expandIngredientsMagnitude, int characterLimit, int responseTokenLimit, AtomicReference<String> outInput) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Create request function
        OAIGPTChatCompletionRequestFunction requestFunction = OAIGPTChatCompletionRequestFunctionCreateRecipeIdeaBuilder.build(IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude).getFunctionDescription());

        // Create system input
        String systemInput = parseSystemInput(expandIngredientsMagnitude);

        // Create input
        String input = parseInput(ingredientsString, modifiersString);

        // Set outInput TODO: Make this better lol
        outInput.set(input);

        // Create request
        OAIGPTChatCompletionRequest request = OpenAIGPTChatCompletionRequestFactory.with(
                systemInput,
                input,
                characterLimit,
                OpenAIGPTModels.GPT_4_0613,
                Constants.DEFAULT_TEMPERATURE,
                responseTokenLimit,
                requestFunction
        );

        // Get response from Open AI
        OAIGPTChatCompletionResponse response = OpenAIGPTHttpsClientHelper.postChatCompletion(request, Keys.openAiAPI);

        // Parse response's choice message function call arguments to OAIGPTFunctionCallResponseCreateRecipeIdea and return TODO: Make this better
        OAIGPTFunctionCallResponseCreateRecipeIdea functionCallResponse = parseCreateRecipeIdeaResponse(response.getChoices()[0].getMessage().getFunction_call().getArguments());

        return functionCallResponse;
    }

    private static String parseSystemInput(Integer expandIngredientsMagnitude) {
        final String spaceString = " ";

        IdeaRecipeExpandIngredients ideaRecipeExpandIngredients = IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude);
        String ideaRecipeSystemMessage = ideaRecipeExpandIngredients.getSystemMessageString();

        return systemMessagePrefix + spaceString + ideaRecipeSystemMessage;
    }

    private static String parseInput(String ingredients, String modifiers) {
        final String ingredientsString = "Ingredients:";
        final String modifiersString = "Modifiers:";
        final String newLineString = "\n";
        final String spaceString = " ";

        StringBuilder sb = new StringBuilder();

        if (ingredients != null && ingredients.length() > 0) {
            // Append ingredientsString, spaceString, and ingredients
            sb.append(ingredientsString);
            sb.append(spaceString);
            sb.append(ingredients);

            // Append new line
            sb.append(newLineString);
        }

        if (modifiers != null && modifiers.length() > 0) {
            // Append modifiersString, spaceString, and modifiers
            sb.append(modifiersString);
            sb.append(spaceString);
            sb.append(modifiers);

            // Append new line
            sb.append(newLineString);
        }

//        if (expandIngredientsMagnitude != null) {
//            // Get and append expand ingredients string
//            String expandIngredients = IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude).getGenerationInputString();
//
//            // Append new line
//            sb.append(newLineString);
//        }

        // Remove last new line if there is enough space for it to exist, basically if the string is not blank
        if (sb.length() >= newLineString.length()) {
            sb.delete(sb.length() - newLineString.length(), sb.length());
        }

//        // Append the ingredients expansion magnitude string if not null TODO: How can I do this better?
//        if (expandIngredientsMagnitude != null) {
//            IdeaRecipeExpandIngredients expandIngredients = IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude);
//
//            // If the reason is none, don't append TODO: How can I do this better?
//            if (expandIngredients != IdeaRecipeExpandIngredients.NONE) {
//                sb.append(newLineString);
//                sb.append(IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude).getSystemMessageString());
//            }
//        }

        // Return built string
        return sb.toString();
    }

    private static OAIGPTFunctionCallResponseCreateRecipeIdea parseCreateRecipeIdeaResponse(String functionCall) throws IOException {
        // Parse with object mapper
        return new ObjectMapper().readValue(functionCall, OAIGPTFunctionCallResponseCreateRecipeIdea.class);
    }

}
