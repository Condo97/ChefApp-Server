package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class RateLimitedException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.RATE_LIMITED;

    public RateLimitedException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}
