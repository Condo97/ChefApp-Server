package com.pantrypro.networking.server.request;

public class ParsePantryItemsRequest extends AuthRequest {

    private String input, imageDataInput;

    public ParsePantryItemsRequest() {

    }

    public ParsePantryItemsRequest(String authToken, String input, String imgaeDataInput) {
        super(authToken);
        this.input = input;
        this.imageDataInput = imgaeDataInput;
    }

    public String getInput() {
        return input;
    }

    public String getImageDataInput() {
        return imageDataInput;
    }

}
