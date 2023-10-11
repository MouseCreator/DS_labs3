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
    private final int THREADS;
    public SoldierLine(int n, int threads) {
        N = n;
        THREADS = threads;
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
        Thread[] threads = new Thread[N];
        for (int i = 0; i < THREADS; i++) {
            int begin = i * perThread;
            int end = (i == THREADS - 1) ? N : (i+1) * perThread;
            System.out.println(begin);
            System.out.println(end);
            LineFraction lineFraction = new LineFraction(begin, end, line, barrier, N);
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

    private record LineFraction(int begin, int end, Direction[] line, Barrier barrier, int N) implements Runnable {
        @Override
        public void run() {
            List<Integer> rotateList = new ArrayList<>();
            while (true) {
                checkIfRotate(rotateList, begin);
                checkIfRotate(rotateList, end - 1);
                barrier.begin();
                for (int i = begin + 1; i < end - 1; i++) {
                    checkIfRotate(rotateList, i);
                }
                try {
                    barrier.awaitDone();
                    rotateAll(rotateList);
                    rotateList.clear();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
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

    private record PrintRunnable(Direction[] line, Barrier barrier) implements Runnable {
        private static final int PRINT_FREQUENCY = 1;
        @Override
        public void run() {
            int current = 0;
            while (true) {
                current++;
                try {
                    barrier.begin();
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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
