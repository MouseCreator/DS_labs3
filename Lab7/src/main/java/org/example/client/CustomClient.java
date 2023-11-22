package org.example.client;

import org.example.communicator.ClientCommunicator;
import org.example.communicator.SocketClientCommunicator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CustomClient {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 7777);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            ClientCommunicator communicator = new SocketClientCommunicator(objectOutputStream, objectInputStream);
            try (CommonClientController controller = new CommonClientController(communicator)) {
                controller.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
