package com.example.client.controllers;

import com.example.client.services.Service;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    public ListView<String> probesList;
    public TextField teamNameText;
    public Button searchByTeamBtn;
    public ListView<String> resultList;
    public Button addParticipantButton;
    private Service service;
    private ObservableList<String> probesCapacityParticipantsCount;
    private ObservableList<String> resultObsList;

    public void setService(Service service) throws Exception {
        this.service = service;
        init();
    }

    private void init() throws Exception {
        service.setCtrl(this);

        probesCapacityParticipantsCount = FXCollections.observableArrayList();
        resultObsList = FXCollections.observableArrayList();
        List<String> probes = service.getProbes();
        probesCapacityParticipantsCount.addAll(probes);
        probesList.setItems(probesCapacityParticipantsCount);
        resultList.setItems(resultObsList);

        probesList.setItems(probesCapacityParticipantsCount);
        resultList.setItems(resultObsList);
    }

    public void searchByTeam(ActionEvent event) throws Exception {
        String teamName = teamNameText.getText();
        resultObsList = FXCollections.observableArrayList();
        List<String> participants = service.getParticipantsByTeam(teamName);
        resultObsList.addAll(participants);
        resultList.setItems(resultObsList);
    }

    public void addParticipant(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/add-view.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        AddParticipantController controller = (AddParticipantController) loader.getController();
        stage.setTitle("Adaugare participant");
        stage.setResizable(false);
        controller.setService(service);
        controller.setSource(this);
    }

    public void update() throws Exception {

    }

    public void enable() {
        addParticipantButton.setDisable(false);
        addParticipantButton.setVisible(true);
    }

    public void disable() {
        addParticipantButton.setDisable(true);
        addParticipantButton.setVisible(false);
    }

    public void update_list() throws Exception {
        probesCapacityParticipantsCount = FXCollections.observableArrayList();
        resultObsList = FXCollections.observableArrayList();
        List<String> probes = service.getProbes();
        probesCapacityParticipantsCount.addAll(probes);
        probesList.setItems(probesCapacityParticipantsCount);
        resultList.setItems(resultObsList);
    }
}
