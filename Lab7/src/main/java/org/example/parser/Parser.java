package org.example.parser;

public interface Parser<T> {
    T parse(String filename);
}
