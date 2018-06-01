package com.journaldev.spring.Response;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserListResponse {

    private List<UserResponse> customers;
    private String message;
    private String messageType;

    public List<UserResponse> getCustomers() {
        return customers;
    }

    public void setCustomers(List<UserResponse> customers) {
        this.customers = customers;
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
