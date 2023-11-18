package org.example.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolImpl implements ThreadPool{
    private final WorkerThread[] workers;
    private final BlockingQueue<Runnable> taskQueue;
    public ThreadPoolImpl(int poolSize) {
        this.workers = new WorkerThread[poolSize];
        this.taskQueue = new LinkedBlockingQueue<>();
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread();
            workers[i].start();
        }
    }

    public void submit(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        for (WorkerThread worker : workers) {
            worker.stopThread();
        }
    }
    private class WorkerThread extends Thread {
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void stopThread() {
            running = false;
            interrupt();
        }
    }

}
