package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.JSONSchemaDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.compoundobjects.IngredientAndCategory;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.networking.responsefactories.CategorizeIngredientsResponseFactory;
import com.pantrypro.networking.server.request.CategorizeIngredientsRequest;
import com.pantrypro.networking.server.response.CategorizeIngredientsResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class CategorizeIngredientsEndpoint {

    public static CategorizeIngredientsResponse categorizeIngredients(CategorizeIngredientsRequest categorizeIngredientsRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, OAISerializerException, JSONSchemaDeserializerException {
        // TODO: User validation, including some sort of count

        // Categorize ingredients
        List<IngredientAndCategory> ingredientsAndCategories = PantryPro.categorizeIngredients(
                categorizeIngredientsRequest.getIngredients(),
                categorizeIngredientsRequest.getStore()
        );

        // Adapt to CategorizeIngredientsResponse and return
        CategorizeIngredientsResponse response = CategorizeIngredientsResponseFactory.from(ingredientsAndCategories);

        return response;
    }

}
