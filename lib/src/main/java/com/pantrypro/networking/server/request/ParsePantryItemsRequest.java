package com.pantrypro.networking.server.request;

public class ParsePantryItemsRequest extends AuthRequest {

    private String input;

    public ParsePantryItemsRequest() {

    }

    public ParsePantryItemsRequest(String authToken, String input) {
        super(authToken);
        this.input = input;
    }

    public String getInput() {
        return input;
    }

}
