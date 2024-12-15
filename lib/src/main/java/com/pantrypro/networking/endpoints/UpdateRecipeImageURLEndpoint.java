package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.dao.pooled.User_AuthTokenDAOPooled;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.MissingRequiredRequestObjectException;
import com.pantrypro.networking.server.request.UpdateRecipeImageURLRequest;

public class UpdateRecipeImageURLEndpoint implements Endpoint<UpdateRecipeImageURLRequest> {

    @Override
    public Object getResponse(UpdateRecipeImageURLRequest request) throws Exception {
        // Ensure values
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty() || request.getRecipeID() == null || request.getImageURL() == null || request.getImageURL().isEmpty())
            throw new MissingRequiredRequestObjectException("Did not include authToken, recipeID, or imageURL.");

        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());

        // TODO: Validate user recipe association?

        // Update Recipe with imageURL
        RecipeDAOPooled.updateImageURL(request.getRecipeID(), request.getImageURL());

        // Return blank string TODO: Is this a fine practice to do check on this
        return "";
    }

}
