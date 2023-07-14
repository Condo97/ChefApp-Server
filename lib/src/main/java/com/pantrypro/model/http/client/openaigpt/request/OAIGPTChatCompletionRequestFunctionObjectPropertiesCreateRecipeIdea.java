package com.pantrypro.model.http.client.openaigpt.request;

import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectArray;
import com.oaigptconnector.model.request.chat.completion.function.objects.OAIGPTChatCompletionRequestFunctionObjectString;

public class OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateRecipeIdea {

    private OAIGPTChatCompletionRequestFunctionObjectArray ingredients;
//    private OAIGPTChatCompletionRequestFunctionObjectArray equipment;
    private OAIGPTChatCompletionRequestFunctionObjectString name;
    private OAIGPTChatCompletionRequestFunctionObjectString short10To40WordEngagingInterestingSummary;
    private OAIGPTChatCompletionRequestFunctionObjectString cuisineType;

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateRecipeIdea() {

    }

    public OAIGPTChatCompletionRequestFunctionObjectPropertiesCreateRecipeIdea(OAIGPTChatCompletionRequestFunctionObjectArray ingredients, OAIGPTChatCompletionRequestFunctionObjectString name, OAIGPTChatCompletionRequestFunctionObjectString short10To40WordEngagingInterestingSummary, OAIGPTChatCompletionRequestFunctionObjectString cuisineType) {
        this.ingredients = ingredients;
//        this.equipment = equipment;
        this.name = name;
        this.short10To40WordEngagingInterestingSummary = short10To40WordEngagingInterestingSummary;
        this.cuisineType = cuisineType;
    }

    public OAIGPTChatCompletionRequestFunctionObjectArray getIngredients() {
        return ingredients;
    }

//    public OAIGPTChatCompletionRequestFunctionObjectArray getEquipment() {
//        return equipment;
//    }

    public OAIGPTChatCompletionRequestFunctionObjectString getName() {
        return name;
    }

    public OAIGPTChatCompletionRequestFunctionObjectString getShort10To40WordEngagingInterestingSummary() {
        return short10To40WordEngagingInterestingSummary;
    }

    public OAIGPTChatCompletionRequestFunctionObjectString getCuisineType() {
        return cuisineType;
    }

}
