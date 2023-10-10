package org.example.soldier.model;

import org.example.soldier.barrier.Barrier;
import org.example.soldier.barrier.BarrierAutoRestart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum Direction {
    LEFT, RIGHT
}
public class SoldierLine {
    private Direction[] line;
    private final int N;
    private final int THREADS;
    public SoldierLine(int n, int threads) {
        N = n;
        THREADS = threads;
    }

    private void generate() {
        line = new Direction[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            int d = random.nextInt(2);
            line[i] = (d == 0) ? Direction.LEFT : Direction.RIGHT;
        }
    }

    private void simulate() {
        int perThread = N / THREADS;
        Barrier barrier = new BarrierAutoRestart(THREADS);
        Thread[] threads = new Thread[N];
        for (int i = 0; i < THREADS; i++) {
            int begin = i * perThread;
            int end;
            if (i == THREADS - 1) {
                end = N;
            } else {
                end = (i+1) * perThread;
            }
            LineFraction lineFraction = new LineFraction(begin, end, line, barrier);
            Thread thread = new Thread(lineFraction);
            thread.start();
            threads[i] = thread;
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private record LineFraction(int begin, int end, Direction[] line, Barrier barrier) implements Runnable {
        @Override
            public void run() {
                int N = line.length;
                List<Integer> rotateList = new ArrayList<>();
                for (int i = begin; i < end; i++) {
                    int neighbor = (line[i] == Direction.RIGHT) ? i + 1 : i - 1;
                    if (neighbor < begin) {
                        if (begin == 0)
                            continue;
                    } else if (neighbor >= end) {
                        if (end == N)
                            continue;
                    }
                    if (line[i] != line[neighbor]) {
                        rotateList.add(i);
                    }
                }
                try {
                    barrier.awaitDone();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                rotateAll(rotateList);
            }

            private void rotateAll(List<Integer> rotateList) {
                for (Integer index : rotateList) {
                    line[index] = rotate(line[index]);
                }
            }

            private Direction rotate(Direction direction) {
                return direction == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
            }
        }
}
