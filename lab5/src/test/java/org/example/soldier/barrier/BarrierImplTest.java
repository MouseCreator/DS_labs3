package org.example.soldier.barrier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class BarrierImplTest {
    private Barrier barrier;

    @BeforeEach
    void setUp() {
        barrier = new BarrierAutoRestart();
    }
    private static class MyTask implements Runnable {
        private final Barrier barrier;

        private final static Object sync = new Object();
        private final List<Integer> integerList;
        private final int N;

        private MyTask(Barrier barrier, List<Integer> integerList, int n) {
            this.barrier = barrier;
            this.integerList = integerList;
            this.N = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                try {
                    synchronized (sync) {
                        integerList.add(i);
                    }
                    barrier.awaitDone();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Test
    void testBarrierOk() {
        int tasks = 3;
        int N = 200;
        barrier.setSize(tasks);
        List<Integer> list = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            MyTask myTask = new MyTask(barrier, list, N);
            Thread thread = new Thread(myTask);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < tasks; j++) {
                Assertions.assertEquals(i, list.get(i*tasks+j));
            }
        }
    }
}