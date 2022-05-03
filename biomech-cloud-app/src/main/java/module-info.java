module com.github.kastkest.cloud.biomechcloudapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.github.kastkest.cloud.biomechcloudapp to javafx.fxml;
    exports com.github.kastkest.cloud.biomechcloudapp;
}