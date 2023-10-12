package org.example.soldier.barrier;

public class BarrierAutoRestart implements Barrier {
    private int tasksToComplete;
    private int tasksToRestart;
    private int size;
    private final Object sync = new Object();
    private final Object restartSync = new Object();
    public BarrierAutoRestart(int size) {
        this.size = size;
        tasksToComplete = size;
    }
    public BarrierAutoRestart() {
        this.size = 1;
        tasksToComplete = 1;
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
        synchronized (restartSync) {
            tasksToRestart--;
            if (tasksToRestart == 0) {
                tasksToComplete = size;
                restartSync.notifyAll();
            }
        }
    }


    private void done() throws InterruptedException {
        synchronized (restartSync) {
            while (tasksToRestart > 0) {
                restartSync.wait();
            }
        }
        synchronized (sync) {
            if (tasksToComplete < 1) {
                throw new IllegalStateException("Task is done, but tasks to complete is zero");
            }
            tasksToComplete--;
            if (tasksToComplete == 0) {
                tasksToRestart = size;
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
