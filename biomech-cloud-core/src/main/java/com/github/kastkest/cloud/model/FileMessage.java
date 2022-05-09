package com.github.kastkest.cloud.model;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileMessage extends AbstractMassage{

    private final String name;
    private final byte[] bytes;

    public FileMessage(Path path) throws IOException {
        name = path.getFileName().toString();
        bytes = Files.readAllBytes(path);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FILE;
    }
}
