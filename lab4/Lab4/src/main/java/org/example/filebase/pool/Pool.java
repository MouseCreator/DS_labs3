package org.example.filebase.pool;

import java.util.Optional;

public interface Pool<T> {
    void append(T value) throws InterruptedException;
    Optional<T> get() throws InterruptedException;
}
