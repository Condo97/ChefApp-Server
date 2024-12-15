package com.pantrypro.networking.server.request;

public class UpdateRecipeImageURLRequest extends AuthRequest {

    private Integer recipeID;
    private String imageURL;

    public UpdateRecipeImageURLRequest() {

    }

    public UpdateRecipeImageURLRequest(String authToken, Integer recipeID, String imageURL) {
        super(authToken);
        this.recipeID = recipeID;
        this.imageURL = imageURL;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public String getImageURL() {
        return imageURL;
    }

}
