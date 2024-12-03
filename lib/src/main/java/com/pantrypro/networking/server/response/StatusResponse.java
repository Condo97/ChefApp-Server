package com.pantrypro.networking.server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pantrypro.networking.server.ResponseStatus;

import java.io.Serializable;

public class StatusResponse implements Serializable {

    private ResponseStatus status;

    public StatusResponse() {

    }

    public StatusResponse(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    // Is this too hacky?
    @JsonProperty(value = "Success")
    public int getSuccess() {
        return status.Success;
    }

}
