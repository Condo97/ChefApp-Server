package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.exceptions.MissingRequiredRequestObjectException;
import com.pantrypro.networking.server.request.UpdateRecipeImageURLRequest;

public class UpdateRecipeImageURLEndpoint implements Endpoint<UpdateRecipeImageURLRequest> {

    @Override
    public Object getResponse(UpdateRecipeImageURLRequest request) throws Exception {
        // Ensure values
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty() || request.getRecipeID() == null || request.getImageURL() == null || request.getImageURL().isEmpty())
            throw new MissingRequiredRequestObjectException("Did not include authToken, recipeID, or imageURL.");

        // Validate auth token and get user ID
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Validate user owns this recipe
        if (!RecipeDAOPooled.isUserAssociatedWithRecipe(userID, request.getRecipeID()))
            throw new InvalidAssociatedIdentifierException("User is not associated with recipe.");

        // Update Recipe with imageURL
        RecipeDAOPooled.updateImageURL(request.getRecipeID(), request.getImageURL());

        // Return blank string
        return "";
    }

}
