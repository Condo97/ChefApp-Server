package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.request.AddOrRemoveLikeOrDislikeRequest;

public class AddOrRemoveLikeOrDislikeEndpoint implements Endpoint<AddOrRemoveLikeOrDislikeRequest> {

    @Override
    public Object getResponse(AddOrRemoveLikeOrDislikeRequest request) throws Exception {
        // Validate auth token and get user ID
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Validate user owns this recipe
        if (!RecipeDAOPooled.isUserAssociatedWithRecipe(userID, request.getRecipeID()))
            throw new InvalidAssociatedIdentifierException("User is not associated with recipe.");

        Recipe recipe = RecipeDAOPooled.get(request.getRecipeID());

        int likesCount = recipe.getLikesCount() == null ? 0 : recipe.getLikesCount();
        int dislikesCount = recipe.getDislikesCount() == null ? 0 : recipe.getDislikesCount();

        if (request.getIsLike()) {
            RecipeDAOPooled.updateLikesCount(
                    request.getRecipeID(),
                    likesCount + (request.getShouldAdd() ? 1 : -1)
            );
        } else {
            RecipeDAOPooled.updateDislikesCount(
                    request.getRecipeID(),
                    dislikesCount + (request.getShouldAdd() ? 1 : -1)
            );
        }

        return BodyResponseFactory.createSuccessBodyResponse(null);
    }

}
