package org.example.winnie.semaphore;

public interface CustomSemaphore {
    void acquire() throws InterruptedException ;
    void release();
}