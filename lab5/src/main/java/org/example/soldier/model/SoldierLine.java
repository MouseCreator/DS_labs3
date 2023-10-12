package org.example.soldier.model;

import org.example.soldier.barrier.Barrier;
import org.example.soldier.barrier.BarrierAutoRestart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SoldierLine {

    enum Direction {
        LEFT, RIGHT
    }
    private Direction[] line;
    private final int N;

    private final RotationCounter rotationCounter;
    private final int THREADS;
    public SoldierLine(int n, int threads) {
        N = n;
        THREADS = threads;
        rotationCounter = new RotationCounter(N);
    }

    public void start() {
        generate();
        simulate();
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
        Barrier barrier = new BarrierAutoRestart(THREADS+1);
        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; i++) {
            int begin = i * perThread;
            int end = (i == THREADS - 1) ? N : (i+1) * perThread;
            LineFraction lineFraction = new LineFraction(begin, end, barrier, i);
            Thread thread = new Thread(lineFraction);
            threads[i] = thread;
            thread.start();
        }
        Thread printerThread = new Thread(new PrintRunnable(line, barrier));
        printerThread.start();
        try {
            for (Thread thread : threads) {
                thread.join();
            }
            printerThread.join();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final class LineFraction implements Runnable {
        private final int begin;
        private final int end;
        private final Barrier barrier;
        private final int id;

        private LineFraction(int begin, int end, Barrier barrier, int id) {
            this.begin = begin;
            this.end = end;
            this.barrier = barrier;
            this.id = id;
        }

        @Override
        public void run() {
            List<Integer> rotateList = new ArrayList<>();
            while (true) {
                prepareRotations(rotateList);
                try {
                    barrier.awaitDone();
                    int rotations = rotateList.size();
                    rotationCounter.put(id, rotations);
                    rotate(rotateList);
                    barrier.awaitDone();
                    if (rotationCounter.finished())
                        return;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private void prepareRotations(List<Integer> rotateList) {
            checkIfRotate(rotateList, begin);
            checkIfRotate(rotateList, end - 1);
            for (int i = begin + 1; i < end - 1; i++) {
                checkIfRotate(rotateList, i);
            }
        }

        private void rotate(List<Integer> rotateList) {
            rotateAll(rotateList);
            rotateList.clear();
        }

        private void checkIfRotate(List<Integer> rotateList, int i) {
            int neighbor = (line[i] == Direction.RIGHT) ? i + 1 : i - 1;
            if (neighbor < 0 || neighbor >= N)
                return;
            if (line[i] != line[neighbor]) {
                rotateList.add(i);
            }
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

    private final class PrintRunnable implements Runnable {
        private static final int PRINT_FREQUENCY = 1;
        private final Direction[] line;
        private final Barrier barrier;

        private PrintRunnable(Direction[] line, Barrier barrier) {
            this.line = line;
            this.barrier = barrier;
        }

        @Override
            public void run() {
                int current = 0;
                while (true) {
                    current++;
                    try {
                        StringBuilder builder = new StringBuilder();
                        for (Direction direction : line) {
                            String s = direction == Direction.LEFT ? "<" : ">";
                            builder.append(s);
                        }
                        if (current == PRINT_FREQUENCY) {
                            System.out.println(builder);
                            current = 0;
                        }
                        barrier.awaitDone();
                        barrier.awaitDone();
                        if (rotationCounter.finished())
                            return;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
}
