package com.pantrypro.core.generation;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.Constants;
import com.pantrypro.core.database.adapters.oai.RecipeInstructionFromOpenAIAdapter;
import com.pantrypro.core.generation.openai.OAIGenerateDirectionsGenerator;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.OAIGPTFunctionCallResponseGenerateDirections;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class RecipeDirectionsGenerator {

    public static List<RecipeInstruction> generateDirections(String name, String summary, List<String> measuredIngredients) throws DBSerializerException, SQLException, OpenAIGPTException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Generate new directions
        OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse = OAIGenerateDirectionsGenerator.generateDirections(
                name,
                summary,
                measuredIngredients,
                Constants.Context_Character_Limit_Generate_Directions,
                Constants.Response_Token_Limit_Generate_Directions
        );

        // Adapt and return
        return RecipeInstructionFromOpenAIAdapter.getRecipeInstructions(generateDirectionsResponse);
    }

}
