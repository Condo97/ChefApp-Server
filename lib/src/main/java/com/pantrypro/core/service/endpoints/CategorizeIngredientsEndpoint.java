package com.pantrypro.core.service.endpoints;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.model.http.server.request.CategorizeIngredientsRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class CategorizeIngredientsEndpoint {

    public static BodyResponse categorizeIngredients(CategorizeIngredientsRequest categorizeIngredientsRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Generate from request
        return PantryPro.generatePackSaveCategorizeIngredientsFunction(categorizeIngredientsRequest);
    }

}
