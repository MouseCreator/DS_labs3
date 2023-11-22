package org.example.activemq;

public class Main {
    public static void main(String[] args) {
        Thread serverThread = new Thread(ServerMQMain::createServer);
        serverThread.setDaemon(true);
        serverThread.start();
        serverThread.setName("SERVER Thread");
        ClientMQMain.createClient();
    }
}
