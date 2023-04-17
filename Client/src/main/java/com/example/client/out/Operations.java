package com.example.client.out;

import com.example.client.controllers.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Operations {

    private final static Logger Logger = LogManager.getLogger();
    private final Socket clientSocket = new Socket();
    private Server server;
    private final InetAddress serverIpAddress = InetAddress.getByName("127.0.0.1");
    private final int SERVER_PORT = 12345;

    private MainController controller = null;
    private Thread notificationThread;


    public Operations() throws IOException {
        start();
    }

    private void start() throws IOException {
        Logger.info("Connecting to server...");
        clientSocket.connect(new InetSocketAddress(serverIpAddress, SERVER_PORT));
        server = new Server(clientSocket);
        notificationThread = new Thread(this::receiveNotifications);
        notificationThread.setDaemon(true);
        notificationThread.start();
    }

    private void receiveNotifications() {
        if (controller == null) {
            Logger.info("Null ctrl");
            return;
        }
        Logger.info("Starting notification thread...");
        while (true) {
            if (controller != null) {
                Platform.runLater(() -> {
                    controller.enable();
                });
            }
            try {
                String message = server.receive();
                Logger.info("Notification received...");
                JSONObject cmd = new JSONObject(message);
                Logger.info("->> Notification: " + message);

                if (cmd.getString("command").equals("stop")) {
                    Logger.debug("->> Stop notification command received");
                    Platform.runLater(() -> {
                        controller.disable();
                    });
                    return;
                }
                if(!cmd.getString("command").equals("update")){
                    throw new RuntimeException("Nothing good happened in this point\n" +
                            "Notification thread cached something fishy ;p");
                }
                Platform.runLater(() -> {
                    try {
                        controller.update_list();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    controller.disable();
                });
                Logger.info("N thread interrupted by true notification...");
                return;
            } catch (Exception e) {
                Logger.error("S-a rupt conexiunea cu serverul :(\n" +
                        "Aplicatia se va inchide acum...");
                Logger.error(e.getMessage());
                Disconnect();
                return;
            }
        }
    }

    public boolean checkUser(String userName, String passwd) throws Exception {
        JSONObject message = new JSONObject();
        message.put("command", "checkUser");
        message.put("userName", userName);
        message.put("passwd", passwd);
        String jsonMessage = message.toString();
        server.send(jsonMessage);
        String response = server.receive();
        JSONObject jsonObject = new JSONObject(response);
        Logger.info("Check user response: " + response);
        return jsonObject.getBoolean("result");
    }


    private void Disconnect() {
        notificationThread.interrupt();
        System.exit(0);
    }

    public List<String> getProbes() throws Exception {
        controller.disable();
        JSONObject requestMessage = new JSONObject();
        Logger.info("Sending request for probes...");
        requestMessage.put("command", "getProbes");
        server.send(requestMessage.toString());
        String response = server.receive();
        JSONObject jsonObject = new JSONObject(response);
        Logger.info("Response1 -> " + response);
        if (jsonObject.getString("command").equals("stop")) {
            response = server.receive();
        }
        Logger.info("Response2 -> " + response);
        jsonObject = new JSONObject(response);
        if (!jsonObject.getString("command").equals("probesList")) {
            startNotificationThread();
            Logger.info("Abort fill probes list");
            return new ArrayList<String>();
        }
        JSONArray probesList = jsonObject.getJSONArray("probes");
        startNotificationThread();
        List<String> probesStringList = new ArrayList<>();
        for (int i = 0; i < probesList.length(); i++) {
            JSONObject probe = probesList.getJSONObject(i);
            probesStringList.add(((Integer) probe.getInt("code")).toString() + " -> "
                    + ((Integer) probe.getInt("count")).toString()
                    + " participanți");
        }
        return probesStringList;
    }

    private void startNotificationThread() throws InterruptedException {
        if (notificationThread.isAlive()) Thread.sleep(200);
        if (notificationThread.isAlive()) {
            Logger.info("N thread still alive after 200 mils... ");
            return;
        }
        Logger.info("N thread found asleep. Restart... ");
        notificationThread = new Thread(this::receiveNotifications);
        notificationThread.start();
    }

    public void setController(MainController mainController) {
        Logger.info("ctrl set");
        controller = mainController;
    }

    public List<String> getParticipantsByTeam(String team) throws Exception {
        controller.disable();
        Logger.info("request participants by team");
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("command", "getParticipantsByTeam");
        requestMessage.put("team", team);
        server.send(requestMessage.toString());
        String response = server.receive();
        Logger.info("Response1 -> " + response);
        if (new JSONObject(response).getString("command").equals("stop")) {
            response = server.receive();
        }
        if (!new JSONObject(response).getString("command").equals("participantsList")) {
            startNotificationThread();
            return new ArrayList<>();
        }
        Logger.info("Response2 -> " + response);
        JSONObject json = new JSONObject(response);
        startNotificationThread();
        List<String> participants = new ArrayList<>();
        JSONArray jsonParticipants = json.getJSONArray("participants");
        for (int i = 0; i < jsonParticipants.length(); i++) {
            participants.add(jsonParticipants.getJSONObject(i).getString("name")
                    + " -> "
                    + ((Integer) jsonParticipants.getJSONObject(i).getInt("capacity")).toString());
        }
        return participants;
    }

    public List<String> getProbeNames() throws Exception {
        controller.disable();
        Logger.info("request probes (codes)");
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("command", "getProbeNames");
        server.send(requestMessage.toString());
        String response = server.receive();
        Logger.info("Response1 -> " + response);
        if (new JSONObject(response).getString("command").equals("stop")) {
            response = server.receive();
        }
        if (!new JSONObject(response).getString("command").equals("probeNamesList")) {
            startNotificationThread();
            return new ArrayList<>();
        }
        Logger.info("Response2 -> " + response);
        JSONObject json = new JSONObject(response);
        ///startNotificationThread();
        JSONArray probesList = json.getJSONArray("probes");
        List<String> probes = new ArrayList<>();
        for (int i = 0; i < probesList.length(); i++) {
            probes.add(((Integer)probesList.getJSONObject(i).getInt("code")).toString());
            Logger.info("->>" + ((Integer)probesList.getJSONObject(i).getInt("code")).toString());
        }
        return probes;
    }

    public List<String> getTeams() throws Exception {
        Logger.info("request teams");
        if (controller != null) {
            controller.disable();
        }

        JSONObject requestMessage = new JSONObject();
        requestMessage.put("command", "getTeams");
        String jsonRequest = requestMessage.toString();

        server.send(jsonRequest);
        String response = server.receive();

        Logger.info("Response1 -> " + response);
        if (new JSONObject(response).getString("command").equals("stop")) {
            response = server.receive();
        }
        if (!new JSONObject(response).getString("command").equals("teamsList")) {
            ///startNotificationThread();
            return new ArrayList<>();
        }
        Logger.info("Response2 -> " + response);
        JSONObject json = new JSONObject(response);
        //startNotificationThread();

        JSONArray teamsList = json.getJSONArray("teams");
        List<String> teams = new ArrayList<>();

        for (int i = 0; i < teamsList.length(); i++) {
            teams.add(teamsList.getJSONObject(i).getString("name"));
        }
        return teams;
    }

    public String addParticipant(String name, String team, String capacity) throws Exception {
        controller.disable();
        Logger.debug("sending request for add");
        JSONObject requestMessage = new JSONObject();
        requestMessage.put("command", "addParticipant");
        requestMessage.put("name", name);
        requestMessage.put("team", team);
        requestMessage.put("capacity", capacity);
        server.send(requestMessage.toString());

        String response = server.receive();

        Logger.info("Response1 -> " + response);
        if (new JSONObject(response).getString("command").equals("stop")) {
            response = server.receive();
        }
        if (!new JSONObject(response).getString("command").equals("addParticipantResponse")) {
            startNotificationThread();
            return "Eșuat";
        }
        startNotificationThread();
        Logger.info("Response2 -> " + response);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("status");
    }
}