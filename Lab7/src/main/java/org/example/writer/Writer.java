package org.example.writer;

public interface Writer<T> {
    void write(String filename, T instance);
}
