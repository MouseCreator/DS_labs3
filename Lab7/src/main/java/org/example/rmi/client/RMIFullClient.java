package org.example.rmi.client;

import org.example.client.CommonClientController;

public class RMIFullClient {
    public static void main(String[] args) {
        RMICommunicator communicator = RMICommunicator.getInstance();
        try(CommonClientController controller = new CommonClientController(communicator)) {
            controller.start();
        }
    }
}
