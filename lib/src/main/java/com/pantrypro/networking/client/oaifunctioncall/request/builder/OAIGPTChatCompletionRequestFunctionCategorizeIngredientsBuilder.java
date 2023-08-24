//package com.pantrypro.model.http.client.oaifunctioncall.request.builder;
//
//import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectObject;
//import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;
//import com.pantrypro.model.http.client.oaifunctioncall.request.OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients;
//import com.pantrypro.model.http.client.oaifunctioncall.request.subobjectproperties.OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory;
//
//import java.util.List;
//
//public class OAIGPTChatCompletionRequestFunctionCategorizeIngredientsBuilder {
//
//    private static final String defaultFunctionDescription = "Categorizes ingredients for easy shopping in a store";
//    private static final String defaultIngredientsWithCategoriesDescription = "A list of ingredients each mapped to a category";
//    private static final String defaultIngredientDescription = "The ingredient";
//    private static final String defaultCategoryDescription = "The category for the ingredient";
//
//    public static OAIGPTChatCompletionRequestFunction build() {
//        return build(
//                defaultFunctionDescription,
//                defaultIngredientsWithCategoriesDescription,
//                defaultIngredientDescription,
//                defaultCategoryDescription
//        );
//    }
//
//    public static OAIGPTChatCompletionRequestFunction build(String functionDescription, String ingredientsWithCategoriesDescription, String ingredientDescription, String categoryDescription) {
//        // Create the ingredient and category objects
//        OAIGPTChatCompletionRequestFunctionObjectString ingredient = new OAIGPTChatCompletionRequestFunctionObjectString(ingredientDescription);
//        OAIGPTChatCompletionRequestFunctionObjectString category = new OAIGPTChatCompletionRequestFunctionObjectString(categoryDescription);
//
//        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory as iwcObjectProperties
//        OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory iwcObjectProperties = new OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory(
//                ingredient,
//                category
//        );
//
//        // Put the iwcObjectProperties in an object with null description
//        OAIGPTChatCompletionRequestFunctionObjectObject iwcObject = new OAIGPTChatCompletionRequestFunctionObjectObject(
//                iwcObjectProperties,
//                null,
//                List.of(
//                        "ingredient",
//                        "category"
//                )
//        );
//
//        // Create OAIGPTChatCompletionRequestFunctionObjectArray using iwcObject
//        OAIGPTChatCompletionRequestFunctionObjectArray iwcArray = new OAIGPTChatCompletionRequestFunctionObjectArray(ingredientsWithCategoriesDescription, iwcObject);
//
//        // Create the OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients
//        OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients ciObjectProperties = new OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredients(iwcArray);
//
//        // Create the categorizeShoppingList parent object
//        OAIGPTChatCompletionRequestFunctionObjectObject cslParametersContainerObject = new OAIGPTChatCompletionRequestFunctionObjectObject(
//                ciObjectProperties,
//                null,
//                List.of(
//                        "ingredientsWithCategories"
//                )
//        );
//
//        // Create OAIGPTChatCompletionRequestFunction.. phew!
//        OAIGPTChatCompletionRequestFunction rFunction = new OAIGPTChatCompletionRequestFunction(
//                "categorize_ingredients",
//                functionDescription,
//                cslParametersContainerObject
//        );
//
//        return rFunction;
//    }
//
//}
