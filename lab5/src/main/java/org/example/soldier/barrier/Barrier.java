package org.example.soldier.barrier;

public interface Barrier {
    void setSize(int tasks);
    int getSize();
    void awaitDone() throws InterruptedException;
}
