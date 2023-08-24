package com.pantrypro.exceptions;

import com.pantrypro.networking.server.ResponseStatus;

public class CapReachedException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.CAP_REACHED_ERROR;

    public CapReachedException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}