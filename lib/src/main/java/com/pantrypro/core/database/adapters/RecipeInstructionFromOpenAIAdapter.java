package com.pantrypro.core.database.adapters;

import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseGenerateDirections;

import java.util.ArrayList;
import java.util.List;

public class RecipeInstructionFromOpenAIAdapter {

    public static List<RecipeInstruction> getRecipeInstructions(Integer recipeID, OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse) {
        List<RecipeInstruction> instructions = new ArrayList<>();
        generateDirectionsResponse.getDirections().forEach(direction ->
                instructions.add(
                        new RecipeInstruction(
                                recipeID,
                                direction
                        )
                )
        );

        return instructions;
    }

}
