package com.pantrypro.networking.server.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pantrypro.networking.server.ResponseStatus;

import java.io.Serializable;

public class StatusResponse implements Serializable {

    private ResponseStatus status;
    private String errorMessage;

    public StatusResponse() {

    }

    public StatusResponse(ResponseStatus status) {
        this.status = status;
    }

    public StatusResponse(ResponseStatus status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    // Is this too hacky?
    @JsonProperty(value = "Success")
    public int getSuccess() {
        return status.Success;
    }

    @JsonProperty(value = "ErrorMessage")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getErrorMessage() {
        return errorMessage;
    }

}
