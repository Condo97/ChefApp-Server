package com.pantrypro.model.http.server.response;

public class ValidateAuthTokenResponse {

    private Boolean isValid;

    public ValidateAuthTokenResponse() {

    }

    public ValidateAuthTokenResponse(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getIsValid() {
        return isValid;
    }

}
