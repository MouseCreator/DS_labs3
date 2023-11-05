package org.example.filter;

public interface Filter<T> {
    T getFilter(String input);
}
