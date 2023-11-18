package org.example.server;

public interface Client extends AutoCloseable {
    void start();
}
