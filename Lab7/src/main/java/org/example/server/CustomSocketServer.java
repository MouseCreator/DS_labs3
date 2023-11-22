package org.example.server;

import org.example.communicator.ServerCommunicator;
import org.example.communicator.ServerSocketCommunicator;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.rmi.server.ControllerInitializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomSocketServer implements Server{
    private ServerSocket serverSocket;
    private ThreadPool threadPool;
    private CommonServerController commonServerController;
    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(7777);
            threadPool = new ThreadPoolImpl(4);
            initService();
            mainServerLoop();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the server", e);
        }
    }

    private void initService() {
        ControllerInitializer controllerInitializer = new ControllerInitializer();
        commonServerController = controllerInitializer.simpleDepartmentController();
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

    private void mainServerLoop() {
        while (true) {
            try {
                acceptAndProcess();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void acceptAndProcess() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Accepted!");
        threadPool.submit(()->processClient(clientSocket));
    }

    private void processClient(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ServerCommunicator communicator = new ServerSocketCommunicator(outputStream, inputStream);
            processRequests(communicator, commonServerController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processRequests(ServerCommunicator serverCommunicator, CommonServerController commonServerController) {
        while (true) {
            Request request = serverCommunicator.receive();
            if (request.getMethod().equalsIgnoreCase("CLOSE")) {
                return;
            }
            Response response = commonServerController.get(request);
            serverCommunicator.send(response);
        }
    }


}
