package com.pantrypro.networking.server.request;

public class LogPinterestConversionRequest {

    private String authToken;
    private String idfa;
    private String eventName;
    private Boolean test;

    public LogPinterestConversionRequest() {

    }

    public LogPinterestConversionRequest(String authToken, String idfa, String eventName) {
        this.authToken = authToken;
        this.idfa = idfa;
        this.eventName = eventName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getIdfa() {
        return idfa;
    }

    public String getEventName() {
        return eventName;
    }

    public Boolean getTest() {
        return test;
    }

}
