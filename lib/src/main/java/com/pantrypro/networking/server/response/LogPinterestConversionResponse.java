package com.pantrypro.networking.server.response;

public class LogPinterestConversionResponse {

    private Boolean didLog;

    public LogPinterestConversionResponse() {

    }

    public LogPinterestConversionResponse(Boolean didLog) {
        this.didLog = didLog;
    }

    public Boolean getDidLog() {
        return didLog;
    }

}
