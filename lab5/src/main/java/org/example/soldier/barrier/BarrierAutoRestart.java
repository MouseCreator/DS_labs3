package org.example.soldier.barrier;

public class BarrierAutoRestart implements Barrier {
    private int tasksToComplete = 0;
    private int size;
    private final Object sync = new Object();
    private final Object restart = new Object();
    private int tasksToRestart = 0;
    public BarrierAutoRestart(int size) {
        this.size = size;
    }
    public BarrierAutoRestart() {
        this.size = 1;
        tasksToComplete = size;
    }

    @Override
    public void setSize(int tasks) {
        this.size = tasks;
        tasksToComplete = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void awaitDone() throws InterruptedException {
        done();
        await();
        restart();
    }

    private void restart() {
        synchronized (restart) {
            tasksToRestart++;
            if (tasksToRestart == size) {
                tasksToComplete = size;
                tasksToRestart = 0;
                restart.notifyAll();
            }
        }
    }

    private void done() throws InterruptedException {
        synchronized (restart) {
            while (tasksToRestart != 0) {
                restart.wait();
            }
        }
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

    private void await() throws InterruptedException {
        synchronized (sync) {
            while (tasksToComplete > 0) {
                sync.wait();
            }
        }
    }

}
