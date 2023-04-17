package org.example.out;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.services.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Operations {
    private final Service service;

    private final static Logger log = LogManager.getLogger();

    private final Object locker = new Object();

    private final ServerSocket serverSocket = new ServerSocket(12420);

    private final List<Client> clients;

    public Operations(Service service) throws IOException {
        this.service = service;
        clients = new ArrayList<>();
        serverSocket.accept();
    }
}
