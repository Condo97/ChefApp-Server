package com.pantrypro.networking.server.response;

import java.util.List;

public class SearchImagesResponse {

    private List<String> imageURLs;

    public SearchImagesResponse() {

    }

    public SearchImagesResponse(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

}
