package com.pantrypro.networking.server.request;

public class AddOrRemoveLikeOrDislikeRequest extends AuthRequest {

    private Integer recipeID;
    private Boolean shouldAdd; // If false, will remove
    private Boolean isLike; // If false, will do action to dislike

    public AddOrRemoveLikeOrDislikeRequest() {

    }

    public AddOrRemoveLikeOrDislikeRequest(String authToken, Integer recipeID, Boolean shouldAdd, Boolean isLike) {
        super(authToken);
        this.recipeID = recipeID;
        this.shouldAdd = shouldAdd;
        this.isLike = isLike;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public Boolean getShouldAdd() {
        return shouldAdd;
    }

    public Boolean getIsLike() {
        return isLike;
    }

}
