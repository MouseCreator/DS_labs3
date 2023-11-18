package org.example.communicator;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketClientCommunicator implements ClientCommunicator {
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public SocketClientCommunicator(ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void send(Request r) {
        try {
            outputStream.writeObject(r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response receive() {
        try {
            return (Response) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
