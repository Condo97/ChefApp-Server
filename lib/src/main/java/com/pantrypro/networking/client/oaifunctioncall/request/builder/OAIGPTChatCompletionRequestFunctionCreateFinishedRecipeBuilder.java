//package com.pantrypro.model.http.client.oaifunctioncall.request.builder;
//
//import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectInteger;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
//import com.pantrypro.model.http.client.oaifunctioncall.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateFinishedRecipe;
//
//import java.util.List;
//
//public class OAIGPTChatCompletionRequestFunctionCreateFinishedRecipeBuilder {
//
//    private static final String defaultFunctionDescription = "Creates a recipe from the given summary and ingredients";
//    private static final String defaultInstructionsDescription = "The instructions to make the recipe";
//    private static final String defaultAllIngredientsAndMeasurementsDescription = "All of the ingredients with measurements needed to make this recipe";
//    private static final String defaultEstimatedTotalCaloriesDescription = "Estimated number of total calories for the recipe";
//    private static final String defaultEstimatedTotalMinutesDescription = "The total estimated time in minutes needed to make the recipe";
//    private static final String defaultEstimatedServingsDescription = "Estimated number of servings the recipe makes";
//    private static final String defaultFeasibilityDescription = "On a scale of 1-10, how feasible the recipe is to make in reality";
//
//    public static OAIGPTChatCompletionRequestFunction build() {
//        return build(defaultFunctionDescription, defaultInstructionsDescription, defaultAllIngredientsAndMeasurementsDescription, defaultEstimatedTotalCaloriesDescription, defaultEstimatedTotalMinutesDescription, defaultEstimatedServingsDescription, defaultFeasibilityDescription);
//    }
//
//    public static OAIGPTChatCompletionRequestFunction build(String functionDescription, String instructionsDescription, String allIngredientsAndMeasurementsDescription, String estimatedTotalCaloriesDescription, String estimatedTotalMinutesDescription, String estimatedServingsDescription, String feasibilityDescription) {
//        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesMakeRecipe
//        OAIGPTChatCompletionRequestFunctionObjectArray instructions, allIngredientsAndMeasurements;
//        OAIGPTChatCompletionRequestFunctionObjectInteger estimatedTotalCalories, estimatedTotalMinutes, estimatedServings, feasibility;
//
//        instructions = new OAIGPTChatCompletionRequestFunctionObjectArray(
//                instructionsDescription,
//                new OAIGPTChatCompletionRequestFunctionObjectString()
//        );
//
//        allIngredientsAndMeasurements = new OAIGPTChatCompletionRequestFunctionObjectArray(
//                allIngredientsAndMeasurementsDescription,
//                new OAIGPTChatCompletionRequestFunctionObjectString()
//        );
//
//        estimatedTotalCalories = new OAIGPTChatCompletionRequestFunctionObjectInteger(
//                estimatedTotalCaloriesDescription
//        );
//
//        estimatedTotalMinutes = new OAIGPTChatCompletionRequestFunctionObjectInteger(
//                estimatedTotalMinutesDescription
//        );
//
//        estimatedServings = new OAIGPTChatCompletionRequestFunctionObjectInteger(
//                estimatedServingsDescription
//        );
//
//        feasibility = new OAIGPTChatCompletionRequestFunctionObjectInteger(
//                feasibilityDescription
//        );
//
//        // Create object properties
//        OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateFinishedRecipe r = new OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateFinishedRecipe(
//                instructions,
//                allIngredientsAndMeasurements,
//                estimatedTotalCalories,
//                estimatedTotalMinutes,
//                estimatedServings,
//                feasibility
//        );
//
//        // Create container for object properties TODO: The strings are the required functions, make these stored somewhere better.. These have to be the same as OAIGPTFunctionCallResponseMakeRecipe
//        OAIGPTChatCompletionRequestFunctionObjectObject rContainer = new OAIGPTChatCompletionRequestFunctionObjectObject(r, null, List.of(
//                "instructions",
//                "allIngredientsAndMeasurements",
//                "estimatedTotalMinutes",
//                "estimatedServings",
//                "estimatedCalories",
//                "feasibility"
//        ));
//
//        // Create function from container
//        OAIGPTChatCompletionRequestFunction rFunction = new OAIGPTChatCompletionRequestFunction(
//                "make_recipe",
//                functionDescription,
//                rContainer
//        );
//
//        return rFunction;
//    }
//
//}
