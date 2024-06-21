package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class MissingRequiredRequestObjectException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.MISSING_REQUIRED_REQUEST_OBJECT;

    public MissingRequiredRequestObjectException(String responseMessage) {
        super(responseMessage);
    }

    public MissingRequiredRequestObjectException(String message, Throwable cause, String responseMessage) {
        super(message, cause, responseMessage);
    }

    public MissingRequiredRequestObjectException(Throwable cause, String responseMessage) {
        super(cause, responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return null;
    }
}
