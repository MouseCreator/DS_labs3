package org.example.server;

public class CustomServer {
    public static void main(String[] args) {
        try(CustomSocketServer server = new CustomSocketServer()) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
