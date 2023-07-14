package com.pantrypro.model.http.server.request;

import java.util.List;

public class MakeRecipeRequest extends AuthRequest {

    Integer ideaID;

    public MakeRecipeRequest() {

    }

    public MakeRecipeRequest(String authToken, Integer ideaID) {
        super(authToken);
        this.ideaID = ideaID;
    }

    public Integer getIdeaID() {
        return ideaID;
    }

}
