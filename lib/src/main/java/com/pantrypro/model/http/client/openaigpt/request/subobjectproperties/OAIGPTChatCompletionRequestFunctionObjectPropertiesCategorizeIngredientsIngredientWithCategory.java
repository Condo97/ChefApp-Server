package com.pantrypro.model.http.client.openaigpt.request.subobjectproperties;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory {

    private OAIGPTChatCompletionRequestFunctionObjectString ingredient;
    private OAIGPTChatCompletionRequestFunctionObjectString category;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCategorizeIngredientsIngredientWithCategory(OAIGPTChatCompletionRequestFunctionObjectString ingredient, OAIGPTChatCompletionRequestFunctionObjectString category) {
        this.ingredient = ingredient;
        this.category = category;
    }

    public OAIGPTChatCompletionRequestFunctionObjectString getIngredient() {
        return ingredient;
    }

    public OAIGPTChatCompletionRequestFunctionObjectString getCategory() {
        return category;
    }

}
