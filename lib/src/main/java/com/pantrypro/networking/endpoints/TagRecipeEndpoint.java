package com.pantrypro.networking.endpoints;

import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.core.PantryPro;
import com.pantrypro.database.objects.recipe.RecipeTag;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.responsefactories.TagRecipeResponseFactory;
import com.pantrypro.networking.server.request.TagRecipeRequest;
import com.pantrypro.networking.server.response.TagRecipeResponse;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class TagRecipeEndpoint {

    public static TagRecipeResponse tagRecipe(TagRecipeRequest tagRecipeRequest) throws InvalidAssociatedIdentifierException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OAISerializerException, OAIDeserializerException {
        // Validate user and recipe association
        PantryPro.validateUserRecipeAssociation(tagRecipeRequest.getAuthToken(), tagRecipeRequest.getRecipeID());

        // Tag recipe
        List<RecipeTag> recipeTags = PantryPro.tagReicpe(tagRecipeRequest.getRecipeID());

        // Adapt to TagRecipeResponse and return
        TagRecipeResponse tagRecipeResponse = TagRecipeResponseFactory.from(recipeTags);

        return tagRecipeResponse;

    }

}
