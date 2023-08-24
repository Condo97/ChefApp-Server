//package com.pantrypro.database.adapters.oai;
//
//import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
//import com.pantrypro.model.http.client.oaifunctioncall.response.functioncall.OAIGPTFunctionCallResponseParseIngredientNames;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IdeaRecipeIngredientFromOpenAIAdapter {
//
//    public static List<IdeaRecipeIngredient> getIdeaRecipeIngredients(OAIGPTFunctionCallResponseParseIngredientNames parseIngredientNamesResponse) {
//        List<IdeaRecipeIngredient> ideaRecipeIngredients = new ArrayList<>();
//        parseIngredientNamesResponse.getIngredientNames().forEach(ingredient ->
//                ideaRecipeIngredients.add(
//                    new IdeaRecipeIngredient(
//                            ingredient
//                    )
//                )
//            );
//
//        return ideaRecipeIngredients;
//    }
//
//}
