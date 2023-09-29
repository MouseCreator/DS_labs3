package org.example.winnie.semaphore;

public class CustomSemaphoreImpl implements CustomSemaphore {
    private final int permits;
    private int permitsUsed;
    private final Object sync = new Object();
    public CustomSemaphoreImpl(int permits) {
        this.permits = permits;
        permitsUsed = 0;
    }
    @Override
    public void acquire() throws InterruptedException  {
        synchronized (sync) {
            while (permitsUsed >= permits) {
                sync.wait();
            }
            permitsUsed++;
        }
    }

    @Override
    public void release() {
        synchronized (sync) {
            permitsUsed--;
            sync.notifyAll();
        }
    }
}
