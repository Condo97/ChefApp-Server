package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.request.AddOrRemoveLikeOrDislikeRequest;

public class AddOrRemoveLikeOrDislikeEndpoint implements Endpoint<AddOrRemoveLikeOrDislikeRequest> {

    @Override
    public Object getResponse(AddOrRemoveLikeOrDislikeRequest request) throws Exception {
        Recipe recipe = RecipeDAOPooled.get(request.getRecipeID());

        int likesCount = recipe.getLikesCount() == null ? 0 : recipe.getLikesCount();
        int dislikesCount = recipe.getDislikesCount() == null ? 0 : recipe.getDislikesCount();

        RecipeDAOPooled.updateLikesCount(
                request.getRecipeID(),
                request.getIsLike() ? likesCount + (request.getShouldAdd() ? 1 : -1) : dislikesCount + (request.getShouldAdd() ? 1 : -1)
        );

        return BodyResponseFactory.createSuccessBodyResponse(null);
    }

}
