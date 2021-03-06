package com.github.kastkest.cloud;


import com.github.kastkest.cloud.model.*;
import com.github.kastkest.cloud.network.Net;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public TableView<FileInfo> clientView;
    public TableView<FileInfo> serverView;
    public ComboBox<String> disksBox;
    public TextField pathField;

    private Net net;
    private Path clientDir = Paths.get("files");


    private void read() {
        try {
            while (true) {

                AbstractMassage message = net.read();
                if (message instanceof ListMessage lm) {
                    serverView.getItems().clear();
                    serverView.getItems().addAll(lm.getFiles());
                    serverView.sort();
                }
                if (message instanceof FileMessage fm) {
                    Files.write(Paths.get(pathField.getText()).resolve(fm.getName()), fm.getBytes());
                    clientView.getItems().clear();
                    clientView.getItems().addAll(getClientFiles(Paths.get(pathField.getText())));
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

        TableColumn<FileInfo, String> fileTypeServerColumn = new TableColumn<>("Type");
        fileTypeServerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeServerColumn.setPrefWidth(50);

        TableColumn<FileInfo, String> fileNameServerColumn = new TableColumn<>("Name");
        fileNameServerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameServerColumn.setPrefWidth(145);

        TableColumn<FileInfo, Long> fileSizeServerColumn = new TableColumn<>("Size");
        fileSizeServerColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeServerColumn.setPrefWidth(120);
        fileSizeServerColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateServerColumn = new TableColumn<>("Date Modified");
        fileDateServerColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateServerColumn.setPrefWidth(120);

        serverView.getColumns().addAll(fileNameServerColumn, fileTypeServerColumn, fileSizeServerColumn, fileDateServerColumn);

        TableColumn<FileInfo, String> fileTypeClientColumn = new TableColumn<>("Type");
        fileTypeClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeClientColumn.setPrefWidth(50);

        TableColumn<FileInfo, String> fileNameClientColumn = new TableColumn<>("Name");
        fileNameClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameClientColumn.setPrefWidth(148);

        TableColumn<FileInfo, Long> fileSizeClientColumn = new TableColumn<>("Size");
        fileSizeClientColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeClientColumn.setPrefWidth(120);
        fileSizeClientColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateClientColumn = new TableColumn<>("Date Modified");
        fileDateClientColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf1)));

        fileDateClientColumn.setPrefWidth(120);

        clientView.getColumns().addAll(fileNameClientColumn, fileTypeClientColumn, fileSizeClientColumn, fileDateClientColumn);
        clientView.getSortOrder().add(fileTypeClientColumn);

        disksBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        clientView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(clientView.getSelectionModel().getSelectedItem().getFilename());
                    if (Files.isDirectory(path)) {
                        try {
                            pathField.setText(path.normalize().toAbsolutePath().toString());
                            clientView.getItems().clear();
                            clientView.getItems().addAll(getClientFiles(path));
                            clientView.sort();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        try {
            updateList(clientDir);
            net = new Net("localhost", 8190);
            Thread.sleep(300);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateList (Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            clientView.getItems().clear();
            clientView.getItems().addAll(getClientFiles(path));
            clientView.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot show files.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    public void upload(ActionEvent actionEvent) throws IOException {
        Path path = Paths.get(pathField.getText());
        String fileName = clientView.getSelectionModel().getSelectedItem().getFilename();
        net.write(new FileMessage(path.resolve(fileName)));
    }

    public void download(ActionEvent actionEvent) throws Exception {
        net.write(new DownloadMessage(serverView.getSelectionModel().getSelectedItem().getFilename()));
    }


    public void removeFromServer(ActionEvent actionEvent) throws IOException {
        String filename = serverView.getSelectionModel().getSelectedItem().getFilename();
        net.write(new DeleteMessage(filename));
    }



    public void passUp(ActionEvent actionEvent) throws IOException {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updateList(upperPath);
        }
    }

    public void selectDisk(ActionEvent actionEvent) throws IOException {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(element.getSelectionModel().getSelectedItem()));
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}