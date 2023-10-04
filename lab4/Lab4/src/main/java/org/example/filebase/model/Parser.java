package org.example.filebase.model;

public interface Parser<T> {
    T fromString(String s);
    String toString(T instance);
}
