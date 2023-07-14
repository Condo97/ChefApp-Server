package com.pantrypro.core.generation.openai;

import com.oaigptconnector.model.Role;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequestMessage;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunctionCall;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OpenAIGPTChatCompletionRequestFactory {

    public static OAIGPTChatCompletionRequest with(String message, int contextCharacterLimit, OpenAIGPTModels model, Integer temperature, int tokenLimit, OAIGPTChatCompletionRequestFunction function) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return with(null, message, contextCharacterLimit, model, temperature, tokenLimit, false, function);
    }

    public static OAIGPTChatCompletionRequest with(String systemMessage, String message, int contextCharacterLimit, OpenAIGPTModels model, Integer temperature, int tokenLimit, OAIGPTChatCompletionRequestFunction function) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return with(systemMessage, message, contextCharacterLimit, model, temperature, tokenLimit, false, function);
    }

    public static OAIGPTChatCompletionRequest with(String systemMessage, String message, int contextCharacterLimit, OpenAIGPTModels model, Integer temperature, int tokenLimit, boolean stream, OAIGPTChatCompletionRequestFunction function) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Create request function call if function is not null
        OAIGPTChatCompletionRequestFunctionCall requestFunctionCall = null;
        if (function != null)
            requestFunctionCall = new OAIGPTChatCompletionRequestFunctionCall(function.getName());

        return with(systemMessage, message, contextCharacterLimit, model, temperature, tokenLimit, stream, requestFunctionCall, List.of(function));
    }

    public static OAIGPTChatCompletionRequest with(String systemMessage, String message, int contextCharacterLimit, OpenAIGPTModels model, Integer temperature, int tokenLimit, boolean stream, OAIGPTChatCompletionRequestFunctionCall functionCall, List<OAIGPTChatCompletionRequestFunction> functions) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        List<OAIGPTChatCompletionRequestMessage> messageRequests = new ArrayList<>();

        // Create system message request and append to messageRequests if not null
        if (systemMessage != null) {
            OAIGPTChatCompletionRequestMessage systemMessageRequest = new OAIGPTChatCompletionRequestMessage(Role.SYSTEM, systemMessage);
            messageRequests.add(systemMessageRequest);
        }

        // Create message request and append to messageRequests if not null
        if (message != null) {
            OAIGPTChatCompletionRequestMessage messageRequest = new OAIGPTChatCompletionRequestMessage(Role.ASSISTANT, message);
            messageRequests.add(messageRequest);
        }

        // Create OpenAIGPTPromptRequest messageRequests and default values
        OAIGPTChatCompletionRequest request = new OAIGPTChatCompletionRequest(
                model.name,
                tokenLimit,
                temperature,
                stream,
                messageRequests,
                functionCall,
                functions
        );

        return request;
    }

}
