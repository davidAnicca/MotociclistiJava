module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.json;

    opens com.example.client to javafx.fxml;
    exports com.example.client;
    exports com.example.client.controllers;
    exports com.example.client.services;
}