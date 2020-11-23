package com.aiocw.aihome.easylauncher.extendfun.entity;

import java.io.Serializable;

public class TextMessageSerializable implements Serializable {
    private String clientName;
    private String textMessage;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
