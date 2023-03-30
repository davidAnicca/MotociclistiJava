package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.Main;
import org.example.services.Service;

import java.io.IOException;

public class LoginController {

    public TextField userNameText;
    public TextField passwdText;
    public Button logInBtn;

    private Service service;


    public void initialize(Service service) {
        this.service = service;
    }

    public void loginPressed(ActionEvent event) throws IOException {
        String userName = userNameText.getText();
        String passwd = passwdText.getText();
        if (service.checkUser(userName, passwd)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            MainController controller = (MainController) loader.getController();
            controller.setService(service);
            stage.setTitle("Concurs de motociclism");
            stage.setResizable(false);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            userNameText.setText("");
            passwdText.setText("");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorect");
            alert.setHeaderText("Ups..");
            alert.setContentText("Nume de utilizator sau parola invalide :(");
            alert.show();
        }
    }
}
