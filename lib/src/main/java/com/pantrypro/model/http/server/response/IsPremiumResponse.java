package com.pantrypro.model.http.server.response;

public class IsPremiumResponse {

    boolean isPremium;

    public IsPremiumResponse() {

    }

    public IsPremiumResponse(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        isPremium = isPremium;
    }

}
