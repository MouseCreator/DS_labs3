package org.example.filebase.lock;

public interface CustomLock {
    void lock() throws InterruptedException;
    void unlock();
}
