package com.github.kastkest.cloud.model;

import lombok.Getter;

import java.io.IOException;

@Getter
public class DeleteMessage extends AbstractMassage {

    private final String name;

    public DeleteMessage(String name) throws IOException {
        this.name = name;
    }

    @Override
    public MessageType getMessageType() {

        return MessageType.DELETE;
    }
}
