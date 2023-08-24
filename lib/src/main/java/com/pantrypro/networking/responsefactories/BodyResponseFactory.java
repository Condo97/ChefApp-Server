package com.pantrypro.networking.responsefactories;


import com.pantrypro.networking.server.ResponseStatus;
import com.pantrypro.networking.server.response.BodyResponse;

public class BodyResponseFactory {

    public static BodyResponse createSuccessBodyResponse(Object object) {
        return createBodyResponse(ResponseStatus.SUCCESS, object);
    }

    public static BodyResponse createBodyResponse(ResponseStatus status, Object object) {
        return new BodyResponse(status, object);
    }

}
