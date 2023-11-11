package org.example.manager;

public interface FileManager {
    void copyXML(String origin, String target);
    void clearXML(String filename);
    boolean isFilePresent(String src);
}
