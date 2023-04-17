package com.example.client.services;

import com.example.client.controllers.MainController;
import com.example.client.out.Operations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Service {

    private Operations server;

    public Service() throws IOException {
        server = new Operations();
    }

    public boolean checkUser(String userName, String passwd) throws Exception {
        return server.checkUser(userName, passwd);
    }

    public List<String> getProbes() throws Exception {
        return server.getProbes();
    }

    public List<String> getProbeNames() throws Exception {
        return server.getProbeNames();
    }

    public List<String> getParticipantsByTeam(String team) throws Exception {
        return server.getParticipantsByTeam(team);
    }

    public List<String> getTeams() throws Exception {
        return server.getTeams();
    }

    public String addParticipant(String name, String team, String capacity) throws Exception {
        return server.addParticipant(name, team, capacity);
    }

    public void setCtrl(MainController mainController) {
        server.setController(mainController);
    }
}
