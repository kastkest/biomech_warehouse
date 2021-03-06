package com.github.kastkest.cloud.model;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Getter
public class ListMessage extends AbstractMassage {

    private final List<FileInfo> files;

    public ListMessage(Path path) throws IOException {

        files = Files.list(path)
                .map(FileInfo::new)
                .toList();

    }

    @Override
    public MessageType getMessageType() {

        return MessageType.LIST;
    }
}
