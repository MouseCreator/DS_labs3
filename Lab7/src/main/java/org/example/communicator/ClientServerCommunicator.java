package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

public interface ClientServerCommunicator {
    void send(Response response);
    Request receive();
}
