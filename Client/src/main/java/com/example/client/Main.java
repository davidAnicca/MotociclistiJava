package com.example.client;

import com.example.client.controllers.LoginController;
import com.example.client.services.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/login-view.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root,root.getPrefWidth(), root.getPrefHeight());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        LoginController controller = loader.getController();
        controller.initialize(new Service());
    }

    public static void main(String[] args) {
        launch();
    }
}