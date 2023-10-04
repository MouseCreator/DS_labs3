package org.example.filebase.model;

import java.util.List;

public interface Parser<T> {
    T fromString(String s);
    String toString(T instance);
    List<T> fromStringMulti(String data);
    String toStringMulti(List<T> instances);
}
