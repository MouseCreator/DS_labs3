package org.example.activemq;

import org.example.client.CommonClientController;

public class ClientMQMain {
    public static void main(String[] args) {
        ClientMQ clientMQ = new ClientMQ();
        clientMQ.start( "SERVER.DECLARE");
        try(CommonClientController controller = new CommonClientController(clientMQ)){
            controller.start();
        }
    }
}
