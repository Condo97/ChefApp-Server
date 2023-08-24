//package com.pantrypro.core.generationlegacy.openai;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.oaigptconnector.core.OpenAIGPTHttpsHandler;
//import com.oaigptconnector.model.exception.OpenAIGPTException;
//import com.oaigptconnector.model.generation.OpenAIGPTModels;
//import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
//import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
//import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
//import com.pantrypro.Constants;
//import com.pantrypro.core.generationlegacy.OpenAIGPTChatCompletionRequestFactory;
//import com.pantrypro.keys.Keys;
//import com.pantrypro.model.http.client.oaifunctioncall.request.builder.OAIGPTChatCompletionRequestFunctionGenerateDirectionsBuilder;
//import com.pantrypro.model.http.client.oaifunctioncall.response.functioncall.OAIGPTFunctionCallResponseGenerateDirections;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.SQLException;
//import java.util.List;
//
//public class OAIGenerateDirectionsGenerator {
//
//    public static OAIGPTFunctionCallResponseGenerateDirections generateDirections(String title, String summary, List<String> measuredIngredients, int characterLimit, int responseTokenLimit) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
//        // Create generate directions request
//        OAIGPTChatCompletionRequestFunction generateDirectionsRequestFunction = OAIGPTChatCompletionRequestFunctionGenerateDirectionsBuilder.build();
//
//        // Create generate directions input
//        String generateDirectionsInput = parseInput(title, summary, measuredIngredients);
//
//        // Create request
//        OAIGPTChatCompletionRequest request = OpenAIGPTChatCompletionRequestFactory.with(
//                generateDirectionsInput,
//                characterLimit,
//                OpenAIGPTModels.GPT_4_0613,
//                Constants.DEFAULT_TEMPERATURE,
//                responseTokenLimit,
//                generateDirectionsRequestFunction
//        );
//
//        // Get response from Open AI
//        OAIGPTChatCompletionResponse response = OpenAIGPTHttpsHandler.postChatCompletion(request, Keys.openAiAPI);
//
//        // Parse response's choice message function call arguments to OAIGPTFunctionCallResponseGenerateDirections and return
//        OAIGPTFunctionCallResponseGenerateDirections functionCallResponse = parseGenerateDirectionsResponse(response.getChoices()[0].getMessage().getFunction_call().getArguments());
//
//        return functionCallResponse;
//    }
//
//    private static String parseInput(String title, String summary, List<String> measuredIngredients) {
//        // Make Peach Cobbler
//        // With ingredients: peaches flour eggs
//        // With the description A sweet and tangy dessert perfect for summer
//        final String makeString = "Make";
//        final String emptyTitleRecipeString = "recipe";
//        final String withIngredientsString = "With ingredients:";
//        final String withTheDescriptionString = "With the description:";
//        final String commaSpaceDelimiterString = ", ";
//        final String newLineString = "\n";
//        final String spaceString = " ";
//
//        StringBuilder sb = new StringBuilder();
//
//        // "Make Peach Cobbler" or if title is null or empty, "Make Recipe"
//        sb.append(makeString);
//        sb.append(spaceString);
//        sb.append(title.isEmpty() ? emptyTitleRecipeString : title);
//
//        // "With ingredients: peach, flour, eggs"
//        if (measuredIngredients.size() > 0) {
//            sb.append(newLineString);
//
//            sb.append(withIngredientsString);
//            sb.append(spaceString);
//
//            measuredIngredients.forEach(ingredient -> {
//                sb.append(ingredient);
//                sb.append(commaSpaceDelimiterString);
//            });
//
//            // If the end of sb is commaSpaceDelimiterString, remove it
//            if (sb.substring(sb.length() - commaSpaceDelimiterString.length(), sb.length()).equals(commaSpaceDelimiterString)) {
//                sb.delete(sb.length() - commaSpaceDelimiterString.length(), sb.length());
//            }
//        }
//
//        // "With the description: A sweet and tangy dessert perfect for summer"
//        if (!summary.isEmpty()) {
//            sb.append(newLineString);
//
//            sb.append(withTheDescriptionString);
//            sb.append(spaceString);
//
//            sb.append(summary);
//        }
//
//        return sb.toString();
//    }
//
//    private static OAIGPTFunctionCallResponseGenerateDirections parseGenerateDirectionsResponse(String functionCall) throws IOException {
//        // Parse with object mapper
//        return new ObjectMapper().readValue(functionCall, OAIGPTFunctionCallResponseGenerateDirections.class);
//    }
//
//}
