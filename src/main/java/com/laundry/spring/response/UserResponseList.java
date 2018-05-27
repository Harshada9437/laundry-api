package com.laundry.spring.response;

import com.laundry.spring.DTO.UserDto;
import com.laundry.spring.response.util.GenericResponse;

import java.util.List;

public class UserResponseList implements GenericResponse{
    private List<UserDto> users;
    private String message;
    private String messageType;

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
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
