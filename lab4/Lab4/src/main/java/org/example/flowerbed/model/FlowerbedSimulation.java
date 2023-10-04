package org.example.flowerbed.model;

import java.util.ArrayList;
import java.util.List;

public class FlowerbedSimulation implements Simulation {

    public FlowerbedSimulation(int n, int m) {
        flowerbedManager = new FlowerbedManager();
        flowerbedManager.init(n,m);
    }
    private final FlowerbedManager flowerbedManager;
    private final List<Thread> threadList = new ArrayList<>();

    private static final String FILENAME = "src/main/resources/file/flowers.txt";

    public void startThreads() {
        GardenerThread gardenerThread = new GardenerThread();
        gardenerThread.start();
        threadList.add(gardenerThread);

        NatureThread natureThread = new NatureThread();
        natureThread.start();
        threadList.add(natureThread);

        PrintThread printThread = new PrintThread();
        printThread.start();
        threadList.add(printThread);

        FileThread fileThread = new FileThread();
        fileThread.start();
        threadList.add(fileThread);
    }

    public void stopThreads() {
        for (Thread thread : threadList) {
            thread.interrupt();
        }
    }

    private class GardenerThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                flowerbedManager.waterFlowers();
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private class NatureThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                flowerbedManager.modifyFlowers();
                try {
                    Thread.sleep(1400);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private class PrintThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                flowerbedManager.printFlowers();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private class FileThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                flowerbedManager.writeFlowersToFile(FILENAME);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
