package com.journaldev.spring.Response;

import org.springframework.stereotype.Component;

@Component
public class LoginResponse implements GenericResponse {
    private int id;
    private String userName;
    private String name;
    private String email;
    private String sessionId;
    private String messageType;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}

