package com.laundry.spring.response.util;


import org.springframework.stereotype.Component;

/**
 * Created by System1 on 9/1/2016.
 */

@Component
public class MessageResponse implements GenericResponse {
    private String messageType;
    private String message;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
