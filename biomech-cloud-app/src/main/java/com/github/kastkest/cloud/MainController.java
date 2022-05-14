package com.github.kastkest.cloud;


import com.github.kastkest.cloud.model.*;
import com.github.kastkest.cloud.network.Net;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TableView<FileInfo> clientView;
    public TableView<FileInfo> serverView;
    private Net net;
    private Path clientDir = Paths.get("files");
    private Path serverDir = Paths.get("server_files");


    private void read() {
        try {
            while (true) {
                AbstractMassage message = net.read();
                if (message instanceof ListMessage lm) {
                    serverView.getItems().clear();
                    serverView.getItems().addAll(lm.getFiles());
                    serverView.sort();
                }
                if (message instanceof DownloadMessage) {
                    clientView.getItems().clear();
                    clientView.getItems().addAll(getClientFiles(clientDir));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<FileInfo> getClientFiles(Path path) throws IOException {
        return Files.list(path)
                .map(FileInfo::new)
                .toList();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<FileInfo, String> fileTypeClientColumn = new TableColumn<>("Type");
        fileTypeClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeClientColumn.setPrefWidth(40);

        TableColumn<FileInfo, String> fileNameClientColumn = new TableColumn<>("Name");
        fileNameClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameClientColumn.setPrefWidth(120);

        TableColumn<FileInfo, String> fileTypeServerColumn = new TableColumn<>("Type");
        fileTypeClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeClientColumn.setPrefWidth(40);

        TableColumn<FileInfo, String> fileNameServerColumn = new TableColumn<>("Name");
        fileNameClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameClientColumn.setPrefWidth(120);


        clientView.getColumns().addAll(fileNameClientColumn, fileTypeClientColumn);
        serverView.getColumns().addAll(fileNameServerColumn, fileTypeServerColumn);
        try {
            clientView.getItems().clear();
            clientView.getItems().addAll(getClientFiles(clientDir));
            clientView.sort();

            net = new Net("localhost", 8190);
            Thread.sleep(300);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot show files.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    public void upload(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem().getFilename();
        net.write(new FileMessage(clientDir.resolve(fileName)));
    }

    public void download(ActionEvent actionEvent) throws Exception {
        net.write(new DownloadMessage(serverView.getSelectionModel().getSelectedItem().getFilename()));
    }

    public void refresh(ActionEvent actionEvent) {

    }

    public void removeFromServer(ActionEvent actionEvent) {

    }

    public void createFolder(ActionEvent actionEvent) {

    }
}