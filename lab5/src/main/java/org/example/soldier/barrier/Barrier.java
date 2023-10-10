package org.example.soldier.barrier;

public interface Barrier {
    void add(int i);
    void done();
    void await() throws InterruptedException;
}
