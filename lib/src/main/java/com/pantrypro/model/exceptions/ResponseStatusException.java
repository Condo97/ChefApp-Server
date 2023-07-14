package com.pantrypro.model.exceptions;

import com.pantrypro.model.http.server.ResponseStatus;

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
