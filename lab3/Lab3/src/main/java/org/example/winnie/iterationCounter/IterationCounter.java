package org.example.winnie.iterationCounter;

public class IterationCounter {
    private int iterationsToGo;
    private final Object object = new Object();
    public IterationCounter(int iterationsToGo) {
        this.iterationsToGo = iterationsToGo;
    }

    public void await() throws InterruptedException {
        synchronized (object) {
            object.wait();
        }
    }

    public int toGo() {
        return iterationsToGo;
    }
    public void done() {
        synchronized (object) {
            iterationsToGo--;
            if (iterationsToGo == 0)
                object.notifyAll();
        }
    }

    public boolean isDone() {
        return toGo() == 0;
    }
}
