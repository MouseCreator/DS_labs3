package org.example.filebase.lock;


public interface CustomReadWriteLock {
       CustomLock readLock();
       CustomLock writeLock();
}
