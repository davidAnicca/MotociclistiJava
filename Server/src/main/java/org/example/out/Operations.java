package org.example.out;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Participant;
import org.example.entity.Probe;
import org.example.entity.Team;
import org.example.services.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Operations {
    private final Service service;

    private final static Logger Logger = LogManager.getLogger();

    private final Object locker = new Object();

    private final ServerSocket serverSocket;

    private final List<Client> clients;

    public Operations(Service service) throws IOException {
        this.service = service;
        clients = new ArrayList<>();
        serverSocket = new ServerSocket(12345);
        acceptClient();
    }

    private void acceptClient() {
        Logger.info("Accepting clients...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                Logger.info(
                        "Accept a client from ip "
                                + clientSocket.getRemoteSocketAddress().toString()
                                + " port "
                                + clientSocket.getPort()
                );
                Thread clientThread = new Thread(() -> clientCommunication(clientSocket));
                clientThread.start();
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    private void clientCommunication(Socket clientSocket) {
        Client client = new Client(clientSocket);
        synchronized (locker) {
            clients.add(client);
        }
        try {
            beginReceive(client);
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

    }

    private void beginReceive(Client client) throws IOException {
        while (true) {
            try {
                String message = client.receive();
                Logger.info(
                        "message from client: "
                                + client.getSocket().getRemoteSocketAddress().toString()
                                + "\n-->>"
                                + message
                );
                treatMessage(message, client);
            } catch (Exception e) {
                Logger.error(e.getMessage());
                client.close();
                clients.remove(client);
                return;
            }
        }
    }

    private void treatMessage(String message, Client client) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonObject = objectMapper.readTree(message);

            switch (jsonObject.get("command").asText()) {
                case "checkUser" -> treatCheck(jsonObject, client);
                case "getProbes" -> treatGetProbes(jsonObject, client);
                case "getProbeNames" -> treatGetProbeNames(jsonObject, client);
                case "getTeams" -> treatGetTeams(jsonObject, client);
                case "getParticipantsByTeam" -> treatGetParticipants(jsonObject, client);
                case "addParticipant" -> treatAdd(jsonObject, client);
                default -> Logger.error("Unknown command:" +
                        "\n->> " + jsonObject.toString());
            }
        } catch (Exception e) {
            Logger.error("unable to treat a message");
            Logger.error(e.getMessage());
        }
    }

    private void treatAdd(JsonNode jsonObject, Client client) throws Exception {
        String name = jsonObject.get("name").asText();
        String team = jsonObject.get("team").asText();
        String capacity = jsonObject.get("capacity").asText();
        synchronized (locker) {
            service.addParticipant(name, team, capacity);
        }

        var stopMessage = JsonNodeFactory.instance.objectNode();
        stopMessage.put("command", "stop");

        client.send(stopMessage.toString());

        var response = JsonNodeFactory.instance.objectNode();
        response.put("command", "addParticipantResponse");
        response.put("status", "Success");
        response.put("message", "Participantul a fost adaugat cu succes!");

        client.send(response.toString());
        Logger.info("Added new participant");
        Thread.sleep(200);
        broadcast();
    }

    private void broadcast() {
        Map<String, Object> updateMessage = new HashMap<>();
        updateMessage.put("command", "update");
        updateMessage.put("message", "New participant added.");

        String jsonMessage = JsonUtils.convertToJson(updateMessage);

        synchronized (locker) {
            for (Client client : clients) {
                try {
                    Logger.info(
                            "Sending notification to client "
                                    + ((InetSocketAddress) client.getSocket().getRemoteSocketAddress()).getPort());
                    client.send(jsonMessage);
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                }
            }
        }
    }

    private void treatGetParticipants(JsonNode jsonObject, Client client) throws Exception {
        String team = jsonObject.get("team").asText();
        List<Participant> participants = service.getParticipantsByTeam(team);
        List<Map<String, Object>> participantsObjects = new ArrayList<>();
        for (Participant participant : participants) {
            Map<String, Object> participantObject = new HashMap<>();
            participantObject.put("name", participant.getName());
            participantObject.put("capacity", participant.getCapacity());
            participantsObjects.add(participantObject);
        }
        Map<String, Object> stopMessage = new HashMap<>();
        stopMessage.put("command", "stop");
        client.send(JsonUtils.convertToJson(stopMessage));
        Map<String, Object> response = new HashMap<>();
        response.put("command", "participantsList");
        response.put("participants", participantsObjects);
        client.send(JsonUtils.convertToJson(response));
    }

    private void treatGetTeams(JsonNode jsonObject, Client client) throws Exception {
        List<Team> teams = service.getTeams();
        List<Object> teamsList = new ArrayList<>();
        for (Team team : teams) {
            Map<String, String> teamMap = new HashMap<>();
            teamMap.put("code", team.getCode().toString());
            teamMap.put("name", team.getName());
            teamsList.add(teamMap);
        }
        Map<String, Object> stopMessage = new HashMap<>();
        stopMessage.put("command", "stop");
        client.send(JsonUtils.convertToJson(stopMessage));
        Map<String, Object> response = new HashMap<>();
        response.put("command", "teamsList");
        response.put("teams", teamsList);
        client.send(JsonUtils.convertToJson(response));
    }

    private void treatGetProbeNames(Object jsonObject, Client client) throws Exception {
        List<Probe> probes = service.getProbes();
        List<Object> probesList = probes.stream()
                .map(probe -> new HashMap<String, String>() {{
                            put("code", probe.getCod().toString());
                            put("name", probe.getName());
                        }}
                )
                .collect(Collectors.toList());

        Map<String, Object> stop = new HashMap<String, Object>() {{
            put("command", "stop");
        }};
        client.send(JsonUtils.convertToJson(stop));
        Map<String, Object> response = new HashMap<String, Object>() {{
            put("command", "probeNamesList");
            put("probes", probesList);
        }};
        String jsonMessage = JsonUtils.convertToJson(response);
        client.send(jsonMessage);
    }


    private void treatGetProbes(JsonNode jsonObject, Client client) throws Exception {
        List<Probe> probes = service.getProbes();
        List<Object> probesList = new ArrayList<>();
        for (Probe probe : probes) {
            Integer code = probe.getCod();
            String name = probe.getName();
            int count = service.getParticipantsCount(code);
            probesList.add(Map.of("code", code, "name", name, "count", count));
        }
        ObjectNode stop = JsonNodeFactory.instance.objectNode();
        stop.put("command", "stop");
        client.send(stop.toString());
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("command", "probesList");
        response.set("probes", JsonNodeFactory.instance.pojoNode(probesList));
        client.send(response.toString());
    }

    private void treatCheck(JsonNode jsonObject, Client client) throws Exception {
        String userName = jsonObject.get("userName").asText();
        String passwd = jsonObject.get("passwd").asText();
        Boolean result = service.checkUser(userName, passwd);
        ObjectNode responseMessage = JsonNodeFactory.instance.objectNode();
        responseMessage.put("command", "checkUser");
        responseMessage.put("result", result);
        String jsonMessage = responseMessage.toString();
        client.send(jsonMessage);
    }
}
