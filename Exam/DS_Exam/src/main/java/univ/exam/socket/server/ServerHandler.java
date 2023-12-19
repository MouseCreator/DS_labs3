package univ.exam.socket.server;

import univ.exam.controller.LetterServiceController;
import univ.exam.dto.PassableRequest;
import univ.exam.dto.PassableResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {
    private final LetterServiceController commonServerController;
    private final ExecutorService executorService;
    public ServerHandler(LetterServiceController commonServerController, int maxThreads) {
        this.commonServerController = commonServerController;
        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    public void start() {
        int port = 7777;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started!");
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected!");
                executorService.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                 ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream())

                 ) {
                PassableRequest input;
                writer.flush();
                while ((input = (PassableRequest) reader.readObject()) != null) {
                    PassableResponse output = commonServerController.get(input);
                    writer.writeObject(output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Connection with " + clientSocket.getInetAddress() + " closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
