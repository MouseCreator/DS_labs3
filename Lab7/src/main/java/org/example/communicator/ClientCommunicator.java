package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

public interface ClientCommunicator {
    void send(Request request);
    Response receive();
    void close();
}
