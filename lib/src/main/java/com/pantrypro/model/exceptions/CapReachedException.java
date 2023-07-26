package com.pantrypro.model.exceptions;

import com.pantrypro.model.exceptions.ResponseStatusException;
import com.pantrypro.model.http.server.ResponseStatus;

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