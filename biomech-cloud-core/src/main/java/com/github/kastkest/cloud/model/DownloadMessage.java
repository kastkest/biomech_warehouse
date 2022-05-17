package com.github.kastkest.cloud.model;

import lombok.Getter;

import java.io.IOException;



@Getter
public class DownloadMessage extends AbstractMassage {

    private final String name;

    public DownloadMessage(String name) throws IOException {
        this.name = name;
    }

    @Override
    public MessageType getMessageType() {

        return MessageType.DOWNLOAD;
    }
}
