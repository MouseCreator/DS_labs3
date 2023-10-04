package org.example.filebase.manager;

public interface FileManager {
    String read(String filename) throws InterruptedException;
    void write(String filename, String content) throws InterruptedException;
    void append(String filename, String content) throws InterruptedException;
}
