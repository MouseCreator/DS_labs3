package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

public interface ServerCommunicator {
    void send(Response response);
    Request receive();
}
