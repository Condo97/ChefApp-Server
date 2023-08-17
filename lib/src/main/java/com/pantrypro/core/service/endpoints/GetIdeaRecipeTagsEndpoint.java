package com.pantrypro.core.service.endpoints;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.model.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.model.http.server.request.TagRecipeIdeaRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class GetIdeaRecipeTagsEndpoint {

    public static BodyResponse tagRecipeIdea(TagRecipeIdeaRequest tagRecipeIdeaRequest) throws InvalidAssociatedIdentifierException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return PantryPro.generatePackSaveIdeaRecipeTags(tagRecipeIdeaRequest);
    }

}
