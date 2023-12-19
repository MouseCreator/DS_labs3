package univ.exam.rmi.client;

import univ.exam.controller.ui.CommonClientController;

public class RMIClient {
    public static void main(String[] args) {
        RMIClientCommunicator communicator = RMIClientCommunicator.getInstance();
        try(CommonClientController controller = new CommonClientController(communicator)) {
            controller.mainLoop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
