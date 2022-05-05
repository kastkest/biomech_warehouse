package com.github.kastkest.cloud;

import java.io.*;
import java.net.Socket;

public class FileMessageHandler implements Runnable {

    private final File dir;
    private final DataInputStream is;
    private final DataOutputStream os;

    public FileMessageHandler(Socket socket) throws IOException {
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client accepted :D");
        dir = new File("server_files");

    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = is.readUTF();
                if (command.equals("#file#")) {
                    readFile();
                    os.writeUTF("");
                    sendStatusOk();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void sendStatusOk() throws IOException {
        os.writeUTF("#status#");
        os.writeUTF("OK");
        os.flush();
    }

    private void readFile() throws IOException {
        // read file name
        String fileName = is.readUTF();

        // file
        File file = dir.toPath().resolve(fileName).toFile();

        // read file lenght
        long size = is.readLong();

        //allocate buffer
        byte[] buffer = new byte[256];


        // write bytes from client to server file
        try (OutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < (size + 255) / 256; i++) {
                int readCount = is.read(buffer);
                fos.write(buffer, 0, readCount);

            }
        }
    }
}
