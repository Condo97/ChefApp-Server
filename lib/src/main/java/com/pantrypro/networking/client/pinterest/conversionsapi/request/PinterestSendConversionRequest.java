package com.pantrypro.networking.client.pinterest.conversionsapi.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public class PinterestSendConversionRequest {

    public enum EventNames {
        CHECKOUT("checkout");

        private String eventName;

        EventNames(String eventName) {
            this.eventName = eventName;
        }

        @JsonValue
        public String getEventName() {
            return eventName;
        }

    }

    public static class UserData {

        @JsonProperty("hashed_maids")
        List<String> hashedMAIDs;

        public UserData() {

        }

        public UserData(List<String> hashedMAIDs) {
            this.hashedMAIDs = hashedMAIDs;
        }

        public List<String> getHashedMAIDs() {
            return hashedMAIDs;
        }

    }

    @JsonProperty("event_name")
    private EventNames eventName;

    @JsonProperty("action_source")
    private String actionSource;

    @JsonProperty("event_time")
    private Long eventTime;

    @JsonProperty("user_data")
    private UserData userData;

    public PinterestSendConversionRequest() {

    }

    public PinterestSendConversionRequest(EventNames eventName, String actionSource, Long eventTime, UserData userData) {
        this.eventName = eventName;
        this.actionSource = actionSource;
        this.eventTime = eventTime;
        this.userData = userData;
    }

    public EventNames getEventName() {
        return eventName;
    }

    public String getActionSource() {
        return actionSource;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public UserData getUserData() {
        return userData;
    }

}
