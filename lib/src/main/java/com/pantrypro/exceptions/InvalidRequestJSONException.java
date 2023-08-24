package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class InvalidRequestJSONException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.JSON_ERROR;

    public InvalidRequestJSONException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
