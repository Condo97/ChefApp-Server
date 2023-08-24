package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class GenerationException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.GENERATION_ERROR;

    public GenerationException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
