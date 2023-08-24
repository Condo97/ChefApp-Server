package com.pantrypro.networking.server.response;

public class GetRemainingResponse {
    private Long remaining;

    public GetRemainingResponse() {

    }

    public GetRemainingResponse(Long remaining) {
        this.remaining = remaining;
    }

    public Long getRemaining() {
        return remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }
}
