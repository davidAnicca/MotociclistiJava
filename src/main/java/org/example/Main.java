package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controllers.LoginController;
import org.example.services.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private final static Logger log = LogManager.getLogger();

    private static Properties props;

    public static void main(String[] args) {

        log.debug("test debug");

        props = new Properties();
        try {
            log.error("test - this is not an error!");
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            log.error("Cannot find bd.config " + e);
        }
        log.error("test - this is not an error!");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        log.debug("Start JavaFX");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root,root.getPrefWidth(), root.getPrefHeight());
        primaryStage.setScene(scene);
        primaryStage.show();

        LoginController controller = loader.getController();
        controller.initialize(new Service(props));
        primaryStage.setTitle("Concurs de motociclism");
        primaryStage.setResizable(false);
        log.debug("End Start JavaFX");
    }
}