package org.example.soldier.barrier;

public class BarrierImpl implements Barrier {
    private int tasksToComplete = 0;

    private final Object sync = new Object();
    @Override
    public void add(int tasks) {
        synchronized (sync) {
            tasksToComplete += tasks;
        }
    }

    @Override
    public void done() {
        synchronized (sync) {
            if (tasksToComplete < 1) {
                throw new IllegalStateException("Task is done, but tasks to complete is zero");
            }
            tasksToComplete--;
            if (tasksToComplete == 0) {
                sync.notifyAll();
            }
        }
    }

    @Override
    public void await() throws InterruptedException {
        synchronized (sync) {
            while (tasksToComplete > 0) {
                sync.wait();
            }
        }
    }
}
