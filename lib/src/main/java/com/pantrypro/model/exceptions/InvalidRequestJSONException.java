package com.pantrypro.model.exceptions;

import com.pantrypro.model.exceptions.ResponseStatusException;
import com.pantrypro.model.http.server.ResponseStatus;

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
