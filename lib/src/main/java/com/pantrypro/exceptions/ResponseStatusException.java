package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public abstract class ResponseStatusException extends Exception {

    private String responseMessage;

    public ResponseStatusException(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public abstract ResponseStatus getResponseStatus();

    public String getResponseMessage() {
        return responseMessage;
    }

}
