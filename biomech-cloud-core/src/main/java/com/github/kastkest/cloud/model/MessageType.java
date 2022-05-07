package com.github.kastkest.cloud.model;

public enum MessageType {
    FILE("file"),
    LIST("list");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
