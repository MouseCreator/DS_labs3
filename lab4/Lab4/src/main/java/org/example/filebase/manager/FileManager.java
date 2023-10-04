package org.example.filebase.manager;

public interface FileManager {
    String read(String filename);
    void write(String filename, String content);
    void append(String filename, String content);
}
