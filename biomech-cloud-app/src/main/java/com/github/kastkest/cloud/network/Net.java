package com.github.kastkest.cloud.network;

import com.github.kastkest.cloud.model.AbstractMassage;
import java.io.IOException;
import java.net.Socket;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class Net {

    private final Socket socket;
    private final ObjectDecoderInputStream is;
    private final ObjectEncoderOutputStream os;

    private final String host;
    private final int port;

    public Net(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(host, port);
        os = new ObjectEncoderOutputStream(socket.getOutputStream());
        is = new ObjectDecoderInputStream(socket.getInputStream());
    }

    public AbstractMassage read() throws Exception {
        return (AbstractMassage) is.readObject();
    }

    public void write(AbstractMassage message) throws IOException {
        os.writeObject(message);
    }
}

