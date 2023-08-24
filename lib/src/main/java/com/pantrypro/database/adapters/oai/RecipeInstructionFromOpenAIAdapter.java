//package com.pantrypro.database.adapters.oai;
//
//import com.pantrypro.database.objects.recipe.RecipeDirection;
//import com.pantrypro.model.http.client.oaifunctioncall.response.functioncall.OAIGPTFunctionCallResponseGenerateDirections;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecipeInstructionFromOpenAIAdapter {
//
//    public static List<RecipeDirection> getRecipeInstructions(OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse) {
//        List<RecipeDirection> instructions = new ArrayList<>();
//        generateDirectionsResponse.getDirections().forEach(direction ->
//                instructions.add(
//                        new RecipeDirection(
//                                direction
//                        )
//                )
//        );
//
//        return instructions;
//    }
//
//}
