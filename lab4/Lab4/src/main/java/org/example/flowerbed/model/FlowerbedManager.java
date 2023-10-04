package org.example.flowerbed.model;

import org.example.filebase.manager.FileManager;
import org.example.filebase.manager.FileManagerImpl;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FlowerbedManager {
    private static final double MODIFY_CHANCE = 0.3;
    private static final double SPAWN_CHANCE = 0.1;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Random random = new Random();

    private final Flowerbed flowerbed = new Flowerbed();

    private final FileManager fileManager = new FileManagerImpl();

    public void waterFlowers() {
        try {
            readWriteLock.writeLock().lock();

            for (Flower flower : flowerbed) {
                if (flower.getState().equals(Flower.State.WITHERED)) {
                    flower.setState(Flower.State.GROWING);
                }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void modifyFlowers() {
        try {
            readWriteLock.writeLock().lock();

            for (Flower flower : flowerbed) {
                if (random.nextDouble() >= MODIFY_CHANCE)
                    continue;
                if (flower.getState().equals(Flower.State.WITHERED)) {
                    flower.setState(Flower.State.DEAD);
                } else if (flower.getState().equals(Flower.State.DEAD)) {
                    if (random.nextDouble() < SPAWN_CHANCE) {
                        flower.setState(Flower.State.GROWING);
                    }
                } else {
                    flower.setState(Flower.State.WITHERED);
                }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    public void printFlowers() {
        try {
            readWriteLock.readLock().lock();
            String s = getFlowerStates();
            System.out.println(s);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void writeFlowersToFile(String file) {
        try {
            readWriteLock.readLock().lock();
            String s = getFlowerStates();
            fileManager.append(file, s);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private String getFlowerStates() {
        StringBuilder builder = new StringBuilder("\n===========\n");
        for (Flower[] flowerRow : flowerbed.getFlowers()) {
            for (Flower flower : flowerRow) {
                builder.append(flower.print()).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();

    }

    public void init(int n, int m) {
        flowerbed.initArray(n, m);
    }
}
