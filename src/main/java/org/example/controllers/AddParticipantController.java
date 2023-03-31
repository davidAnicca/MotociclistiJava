package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entity.Probe;
import org.example.entity.Team;
import org.example.services.Service;

import java.sql.SQLException;
import java.util.List;

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

    public void addParticipant(ActionEvent event) throws SQLException {
        String name = nameText.getText();
        if (name.equals(""))
            return;
        String team = teamCombo.getValue();
        if (team.equals(""))
            return;
        String capacity = capacityCombo.getValue();
        if (capacity.equals(""))
            return;
        service.addParticipant(name, team, capacity);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmare");
        confirm.setHeaderText("Ok!");
        confirm.setContentText("Participantul " + name + " a fost adugat cu succes <3");
        confirm.show();
        source.update();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setService(Service service) throws SQLException {
        this.service = service;
        init();
    }

    private void init() throws SQLException {
        List<Team> teams = service.getTeams();
        for (Team team : teams) {
            teamsObs.add(team.getName());
        }
        List<Probe> probes = service.getProbes();

        for (Probe probe : probes) {
            capacityObs.add(probe.getCod().toString());
        }
        teamCombo.setItems(teamsObs);
        capacityCombo.setItems(capacityObs);
    }
}
