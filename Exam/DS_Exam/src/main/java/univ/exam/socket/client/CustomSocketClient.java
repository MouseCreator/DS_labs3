package univ.exam.socket.client;

import univ.exam.controller.ui.CommonClientController;

import java.io.IOException;
import java.net.Socket;

public class CustomSocketClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 7777);
            ClientSocketCommunicator communicator = ClientSocketCommunicator.getInstance(socket);
            try(CommonClientController commonClientController = new CommonClientController(communicator)) {
                commonClientController.mainLoop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
