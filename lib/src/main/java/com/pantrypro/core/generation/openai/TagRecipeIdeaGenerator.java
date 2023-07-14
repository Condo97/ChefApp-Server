package com.pantrypro.core.generation.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.core.OpenAIGPTHttpsClientHelper;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
import com.pantrypro.Constants;
import com.pantrypro.keys.Keys;
import com.pantrypro.model.http.client.openaigpt.request.builder.OAIGPTChatCompletionRequestFunctionTagRecipeIdeaBuilder;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseTagRecipeIdea;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class TagRecipeIdeaGenerator {

    public static OAIGPTFunctionCallResponseTagRecipeIdea generateTagRecipeIdea(String input, int characterLimit, int responseTokenLimit) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Create request function
        OAIGPTChatCompletionRequestFunction requestFunction = OAIGPTChatCompletionRequestFunctionTagRecipeIdeaBuilder.build();

        // Create request
        OAIGPTChatCompletionRequest request = OpenAIGPTChatCompletionRequestFactory.with(
                input,
                characterLimit,
                OpenAIGPTModels.GPT_4_0613,
                Constants.DEFAULT_TEMPERATURE,
                responseTokenLimit,
                requestFunction
        );

        // Get response from Open AI
        OAIGPTChatCompletionResponse response = OpenAIGPTHttpsClientHelper.postChatCompletion(request, Keys.openAiAPI);

        // Parse response's choice message function call arguments to OAIGPTFunctionCallResponseMakeRecipe
        OAIGPTFunctionCallResponseTagRecipeIdea functionCallResponse = parseTagRecipeIdeaResponse(response.getChoices()[0].getMessage().getFunction_call().getArguments());

        return functionCallResponse;
    }

    private static OAIGPTFunctionCallResponseTagRecipeIdea parseTagRecipeIdeaResponse(String functionCall) throws IOException {
        return new ObjectMapper().readValue(functionCall, OAIGPTFunctionCallResponseTagRecipeIdea.class);
    }

}
