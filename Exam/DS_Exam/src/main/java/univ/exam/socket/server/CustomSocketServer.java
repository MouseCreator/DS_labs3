package univ.exam.socket.server;

import univ.exam.controller.initializer.ServerControllerInitializer;

public class CustomSocketServer {
    public static void main(String[] args) {
        ServerHandler serverHandler = new ServerHandler(ServerControllerInitializer.getLetterServerController(), 4);
        serverHandler.start();
    }
}
