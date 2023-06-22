package com.pantrypro.core.service.endpoints;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.generation.openai.function.createrecipeidea.OAIGPTChatCompletionRequestFunctionCreateRecipeIdeaBuilder;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.server.request.func.CreateRecipeIdeaRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class CreateRecipeIdeaEndpoint {

    public static BodyResponse createRecipeIdea(CreateRecipeIdeaRequest createRecipeIdeaRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, OpenAIGPTException, IOException {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(createRecipeIdeaRequest.getAuthToken());

        // Create formatted string with ingredients and modifiers and stuff
        StringBuilder formattedRecipeIdeaRequestStringBuilder = new StringBuilder();
        formattedRecipeIdeaRequestStringBuilder.append(createRecipeIdeaRequest.getIngredients());
        formattedRecipeIdeaRequestStringBuilder.append("\n");
        formattedRecipeIdeaRequestStringBuilder.append(createRecipeIdeaRequest.getModifiers());
        String formattedRecipeIdeaRequestString = formattedRecipeIdeaRequestStringBuilder.toString();

//        // Create chat for conversation with all the ingredients and modifiers in a specified format
//        Chat chat = ChatDBManager.createInDB(Sender.USER, formattedRecipeIdeaRequestString, LocalDateTime.now());

        // Create recipe idea from builder
        OAIGPTChatCompletionRequestFunction recipeIdeaFunction = OAIGPTChatCompletionRequestFunctionCreateRecipeIdeaBuilder.build();

        return null;

//        // Create function request from factory
//        OAIGPTChatCompletionRequest gptRequest = OpenAIGPTChatCompletionRequestFactory.with(conversation, 400, OpenAIGPTModels.GPT_4_0613, Constants.DEFAULT_TEMPERATURE, 400, false, recipeIdeaFunction);

//        System.out.println(new ObjectMapper().writeValueAsString(gptRequest));

//        // Get the response from OpenAI
//        OAIGPTChatCompletionResponse response = OpenAIGPTHttpsClientHelper.postChatCompletion(gptRequest, Keys.openAiAPI);

//        return BodyResponseFactory.createSuccessBodyResponse(response);

        // Parse the response

        // Parse the arguments component to a JSON object

        // Create a response to the client and with the ingredients, equipment, etc.

    }

}
