package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.entity.Participant;
import org.example.entity.Probe;
import org.example.services.Service;

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

    public void setService(Service service) throws SQLException {
        this.service = service;
        init();
    }

    private void init() throws SQLException {
        probesCapacityParticipantsCount = FXCollections.observableArrayList();
        resultObsList = FXCollections.observableArrayList();
        List<Probe> probes = service.getProbes();
        for(Probe probe : probes){
            probesCapacityParticipantsCount.add(
                    probe.getCod().toString() + "->"
                    +service.getParticipantsCount(probe.getCod())
                    +" participanti"
            );
        }
        probesList.setItems(probesCapacityParticipantsCount);
        resultList.setItems(resultObsList);
    }

    public void searchByTeam(ActionEvent event) throws SQLException {
        String teamName = teamNameText.getText();
        resultObsList = FXCollections.observableArrayList();
        List<Participant> participants = service.getParticipantsByTeam(teamName);
        for(Participant participant : participants){
            resultObsList.add(participant.getName());
        }
        resultList.setItems(resultObsList);
    }

    public void addParticipant(ActionEvent event) throws IOException, SQLException {
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

    public void update() throws SQLException {
        probesCapacityParticipantsCount = FXCollections.observableArrayList();
        resultObsList = FXCollections.observableArrayList();
        List<Probe> probes = service.getProbes();
        for(Probe probe : probes){
            probesCapacityParticipantsCount.add(
                    probe.getCod().toString() + "->"
                            +service.getParticipantsCount(probe.getCod())
                            +" participanti"
            );
        }
        probesList.setItems(probesCapacityParticipantsCount);
        resultList.setItems(resultObsList);
    }
}
