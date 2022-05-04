package com.github.kastkest.cloud.biomech_app;


import com.github.kastkest.cloud.biomech_app.network.Net;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Net net;

    public ListView<String> view;

    public TextField text;

    private void readListFiles() {
        try {
            view.getItems().clear();
            Long filesCont = net.readLong();
            for (int i = 0; i < filesCont; i++) {
                String fileName = net.readUTF();
                view.getItems().addAll(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read() {

        try {
            while (true) {
                String command = net.readUTF();
                if (command.equals("#list#")) {
                    readListFiles();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            net = new Net("localhost", 8189);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}