package com.pantrypro.networking.server.request;

public class TikAPIGetVideoInfoRequest extends AuthRequest {

    private String videoID;

    public TikAPIGetVideoInfoRequest() {

    }

    public TikAPIGetVideoInfoRequest(String authToken, String videoID) {
        super(authToken);
        this.videoID = videoID;
    }

    public String getVideoID() {
        return videoID;
    }

}
