package com.pantrypro.model.http.client.apple.itunes.exception;

import com.pantrypro.model.http.client.apple.itunes.response.error.AppleItunesErrorResponse;
import com.pantrypro.model.http.client.common.exception.ResponseException;

public class AppleItunesResponseException extends ResponseException {
    public AppleItunesResponseException(AppleItunesErrorResponse errorObject) {
        super(errorObject);
    }

    @Override
    public Object getErrorObject() {
        Object o = getErrorObject();
        return (o instanceof AppleItunesErrorResponse) ? (AppleItunesErrorResponse)o : null;
    }
}
