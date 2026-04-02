package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class AuthTokenExpiredException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.AUTH_TOKEN_EXPIRED;

    public AuthTokenExpiredException(String message) {
        super(message);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
