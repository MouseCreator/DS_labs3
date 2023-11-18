package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomSocketServer implements Server{
    private ServerSocket serverSocket;
    private ThreadPool threadPool;
    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(7777);
            threadPool = new ThreadPoolImpl(4);
            mainServerLoop();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the server", e);
        }
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
        threadPool.submit(()->processClient(clientSocket));
    }

    private void processClient(Socket clientSocket) {
        try {
            clientSocket.getOutputStream();
            clientSocket.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
