package com.example.client.controllers;

import com.example.client.services.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;


public class AddParticipantController {
    public TextField nameText;
    public ComboBox<String> teamCombo;
    public ComboBox<String> capacityCombo;
    public Button addBtn;

    private Service service;

    ObservableList<String> teamsObs = FXCollections.observableArrayList();
    ObservableList<String> capacityObs = FXCollections.observableArrayList();

    public void setSource(MainController source) {
        this.source = source;
    }

    private MainController source;

    public void addParticipant(ActionEvent event) throws Exception {
        String name = nameText.getText();
        if (name.equals(""))
            return;
        String team = teamCombo.getValue();
        if (team.equals(""))
            return;
        String capacity = capacityCombo.getValue();
        if (capacity.equals(""))
            return;
        String response = service.addParticipant(name, team, capacity);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Răspuns");
        confirm.setHeaderText("Starea adăugării:");
        confirm.setContentText(response);
        confirm.show();
        source.update();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setService(Service service) throws Exception {
        this.service = service;
        init();
    }

    private void init() throws Exception {
        teamsObs.addAll(service.getTeams());
        teamCombo.setItems(teamsObs);
        capacityObs.addAll(service.getProbeNames());
        capacityCombo.setItems(capacityObs);
    }
}
