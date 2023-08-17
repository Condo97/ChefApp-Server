package com.pantrypro.core.database.adapters.oai;

import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseGenerateDirections;

import java.util.ArrayList;
import java.util.List;

public class RecipeInstructionFromOpenAIAdapter {

    public static List<RecipeInstruction> getRecipeInstructions(OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse) {
        List<RecipeInstruction> instructions = new ArrayList<>();
        generateDirectionsResponse.getDirections().forEach(direction ->
                instructions.add(
                        new RecipeInstruction(
                                direction
                        )
                )
        );

        return instructions;
    }

}
