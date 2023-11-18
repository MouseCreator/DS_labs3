package org.example.server;

public interface ThreadPool {
    void submit(Runnable task);
    void shutdown();
}
