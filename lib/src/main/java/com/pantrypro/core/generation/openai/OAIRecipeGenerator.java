package com.pantrypro.core.generation.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.core.OpenAIGPTHttpsHandler;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
import com.pantrypro.Constants;
import com.pantrypro.core.generation.OpenAIGPTChatCompletionRequestFactory;
import com.pantrypro.keys.Keys;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseMakeRecipe;
import com.pantrypro.model.http.client.openaigpt.request.builder.OAIGPTChatCompletionRequestFunctionMakeRecipeBuilder;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class OAIRecipeGenerator {

    public static OAIGPTFunctionCallResponseMakeRecipe generateMakeRecipeFunctionCall(String input, int characterLimit, int responseTokenLimit) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Create request function
        OAIGPTChatCompletionRequestFunction requestFunction = OAIGPTChatCompletionRequestFunctionMakeRecipeBuilder.build();

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
        OAIGPTChatCompletionResponse response = OpenAIGPTHttpsHandler.postChatCompletion(request, Keys.openAiAPI);

        // Parse response's choice message function call arguments to OAIGPTFunctionCallResponseMakeRecipe TODO: Make this better
        OAIGPTFunctionCallResponseMakeRecipe functionCallResponse = parseMakeRecipeResponse(response.getChoices()[0].getMessage().getFunction_call().getArguments());

        return functionCallResponse;
    }

    private static OAIGPTFunctionCallResponseMakeRecipe parseMakeRecipeResponse(String functionCall) throws IOException {
        // Parse with object mapper
        return new ObjectMapper().readValue(functionCall, OAIGPTFunctionCallResponseMakeRecipe.class);
    }

}
