package com.pantrypro.networking.server.request;

public class LogPinterestConversionRequest {

    private String authToken;
    private String idfa;
    private String eventName;
    private String eventID;
    private Boolean test;

    public LogPinterestConversionRequest() {

    }

    public LogPinterestConversionRequest(String authToken, String idfa, String eventName, String eventID, Boolean test) {
        this.authToken = authToken;
        this.idfa = idfa;
        this.eventName = eventName;
        this.eventID = eventID;
        this.test = test;
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

    public String getEventID() {
        return eventID;
    }

    public Boolean getTest() {
        return test;
    }

}
