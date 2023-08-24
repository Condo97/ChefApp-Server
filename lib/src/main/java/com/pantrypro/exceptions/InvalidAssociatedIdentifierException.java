package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class InvalidAssociatedIdentifierException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.INVALID_ASSOCIATED_ID;

    public InvalidAssociatedIdentifierException(String message) {
        super(message);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
