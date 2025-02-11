package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.JSONSchemaDeserializerException;
import com.oaigptconnector.model.OAIChatCompletionRequestMessageBuilder;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.request.chat.completion.CompletionRole;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequestMessage;
import com.oaigptconnector.model.request.chat.completion.content.InputImageDetail;
import com.pantrypro.core.PantryPro;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.MissingRequiredRequestObjectException;
import com.pantrypro.openai.structuredoutput.ParsePantryItemsSO;
import com.pantrypro.networking.server.request.ParsePantryItemsRequest;
import com.pantrypro.networking.server.response.ParsePantryItemsResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ParsePantryItemsEndpoint {

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(com.oaigptconnector.Constants.AI_TIMEOUT_MINUTES)).build();

    public static ParsePantryItemsResponse parsePantryItems(ParsePantryItemsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MissingRequiredRequestObjectException, OAISerializerException, OpenAIGPTException, IOException, JSONSchemaDeserializerException {
        // If either request authToken or input are null or input is empty, throw MissingRequiredRequestObjectException
        if (request.getAuthToken() == null || ((request.getInput() == null || request.getInput().isEmpty()) && (request.getImageDataInput() == null || request.getImageDataInput().isEmpty())))
            throw new MissingRequiredRequestObjectException("Please make sure authToken and input are included and not null or empty.");

        // Validate AuthToken TODO: Is this even necessary, what's the specific use case and how can this implementation be improved?
        UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // TODO: Move all this logic somewhere else probably lol

        // Create user message with text input, image data input, or both
        OAIChatCompletionRequestMessageBuilder userMessageBuilder = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER);

        if (request.getInput() != null && !request.getInput().isEmpty())
            userMessageBuilder.addText(request.getInput());

        if (request.getImageDataInput() != null && !request.getImageDataInput().isEmpty())
            userMessageBuilder.addImage("data:image/png;base64,\n" + request.getImageDataInput(), null);

        OAIChatCompletionRequestMessage userMessage = userMessageBuilder.build();

        // Create messages array from userMessage
        List<OAIChatCompletionRequestMessage> messages = List.of(
                userMessage
        );

        // Deserialize soResponse to ParsePantryItemsSO
        ParsePantryItemsSO parsePantryItemsSO = PantryPro.getStructuredOutput(
                    ParsePantryItemsSO.class,
                    messages
            );

        // Map to ParsePantryItemsResponse.BarItem List
        List<ParsePantryItemsResponse.PantryItem> pantryItems = parsePantryItemsSO.getBarItems().stream()
                .map(b -> new ParsePantryItemsResponse.PantryItem(
                        b.getItem(),
                        b.getCategory()))
                .collect(Collectors.toList());

        // Create and return ParsePantryItemsResponse with barItems
        return new ParsePantryItemsResponse(pantryItems);
    }

}

