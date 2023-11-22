package org.example.activemq;

import org.example.communicator.ServerCommunicator;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.server.CommonServerController;

public class ServerRunnable implements Runnable{
    private final CommonServerController controller;
    private final ServerCommunicator serverCommunicator;

    public ServerRunnable(ServerCommunicator serverCommunicator, CommonServerController controller) {
        this.controller = controller;
        this.serverCommunicator = serverCommunicator;
    }
    @Override
    public void run() {
        while (true) {
            Request request = serverCommunicator.receive();
            if (request.getMethod().equals("CLOSE")) {
                return;
            }
            Response response = controller.get(request);
            serverCommunicator.send(response);
        }
    }
}
