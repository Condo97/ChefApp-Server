package com.pantrypro.networking.responsefactories;

import com.pantrypro.networking.server.ResponseStatus;
import com.pantrypro.networking.server.response.StatusResponse;

public class StatusResponseFactory {

    public static StatusResponse createSuccessStatusResponse() {
        return createStatusResponse(ResponseStatus.SUCCESS);
    }

    public static StatusResponse createStatusResponse(ResponseStatus status) {
        return new StatusResponse(status);
    }

}
