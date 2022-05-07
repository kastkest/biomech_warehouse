package com.github.kastkest.cloud.model;

import java.io.Serializable;

public abstract class AbstractMassage implements Serializable {

    public abstract MessageType getMessageType();
}
