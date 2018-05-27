package com.laundry.spring.response.util;

public class UploadResponse implements GenericResponse {

    private String filePath;
    private String message;
    private Object MessageType;

    public String getMessage() {
        return message;
    }

    public Object getMessageType() {
        return MessageType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void setMessageType(String message)

    {
        this.MessageType = MessageType;
    }

    @Override
    public void setMessage(String message)

    {

        this.message = message;
    }
}


