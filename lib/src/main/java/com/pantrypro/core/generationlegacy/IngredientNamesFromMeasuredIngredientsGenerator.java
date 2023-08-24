//package com.pantrypro.core.generationlegacy;
//
//import com.oaigptconnector.model.exception.OpenAIGPTException;
//import com.pantrypro.Constants;
//import com.pantrypro.core.generationlegacy.openai.OAIParseIngredientNamesGenerator;
//import com.pantrypro.database.adapters.oai.IdeaRecipeIngredientFromOpenAIAdapter;
//import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
//import com.pantrypro.model.http.client.oaifunctioncall.response.functioncall.OAIGPTFunctionCallResponseParseIngredientNames;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.SQLException;
//import java.util.List;
//
//public class IngredientNamesFromMeasuredIngredientsGenerator {
//
//    public static List<IdeaRecipeIngredient> generateIngredientNames(List<String> measuredIngredients) throws DBSerializerException, SQLException, OpenAIGPTException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        // Generate new idea recipe ingredients from get ingredients without measurements
//        OAIGPTFunctionCallResponseParseIngredientNames parseIngredientNamesResponse = OAIParseIngredientNamesGenerator.getParsedIngredientNames(
//                measuredIngredients,
//                Constants.Context_Character_Limit_Get_Ingredients_Without_Measurements,
//                Constants.Response_Token_Limit_Get_Ingredients_Without_Measurements
//        );
//
//        // Adapt to IdeaRecipeIngredient list
//        return IdeaRecipeIngredientFromOpenAIAdapter.getIdeaRecipeIngredients(parseIngredientNamesResponse);
//    }
//
//}
