package com.pantrypro.model.exceptions;

import com.pantrypro.model.http.server.ResponseStatus;

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
