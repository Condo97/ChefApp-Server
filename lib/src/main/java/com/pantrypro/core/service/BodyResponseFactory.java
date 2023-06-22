package com.pantrypro.core.service;


import com.pantrypro.model.http.server.ResponseStatus;
import com.pantrypro.model.http.server.response.BodyResponse;

public class BodyResponseFactory {

    public static BodyResponse createSuccessBodyResponse(Object object) {
        return createBodyResponse(ResponseStatus.SUCCESS, object);
    }

    public static BodyResponse createBodyResponse(ResponseStatus status, Object object) {
        return new BodyResponse(status, object);
    }

}
