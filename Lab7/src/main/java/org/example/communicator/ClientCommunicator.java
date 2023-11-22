package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

public interface ClientCommunicator {
    Response sendAndReceive(Request request);
    void close();
}
