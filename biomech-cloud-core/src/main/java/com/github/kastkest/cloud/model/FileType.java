package com.github.kastkest.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FileType {
    FILE("file"),
    DIRECTORY("folder");

    private String name;
}
