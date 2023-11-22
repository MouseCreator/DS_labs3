package org.example.activemq;

public class ServerMQMain {
    public static void main(String[] args) {
        ServerMQ serverMQ = new ServerMQ();
        String declareDest = "SERVER.DECLARE";
        serverMQ.initialize(declareDest);
        serverMQ.runServer();
    }
}
