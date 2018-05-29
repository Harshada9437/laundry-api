package com.laundry.spring.response;

import java.util.List;

public class LaundryResponseList {
    private List<LaundryResponse> requests;
    private String message;
    private String messageType;

    public List<LaundryResponse> getRequests() {
        return requests;
    }

    public void setRequests(List<LaundryResponse> requests) {
        this.requests = requests;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
