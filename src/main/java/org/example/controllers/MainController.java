package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.entity.Participant;
import org.example.entity.Probe;
import org.example.services.Service;

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
        new Alert(Alert.AlertType.CONFIRMATION).show();
        resultList.setItems(resultObsList);
    }

    public void addParticipant(ActionEvent event) {
    }
}
