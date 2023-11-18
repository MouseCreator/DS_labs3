package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketCommunicator implements ClientServerCommunicator {
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public SocketCommunicator(ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void send(Response response) {
        try {
            outputStream.writeObject(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Request receive() {
        try {
            return (Request) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
