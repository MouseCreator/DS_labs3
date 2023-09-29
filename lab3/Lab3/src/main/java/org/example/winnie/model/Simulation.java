package org.example.winnie.model;

import org.example.winnie.iterationCounter.IterationCounter;

public class Simulation {

    private static final Object sync = new Object();
    public void start() {
        IterationCounter iterationCounter = new IterationCounter(10);
        Pot pot = new Pot(20, iterationCounter);
        Bear bear = new Bear(pot);
        Bee[] bees = new Bee[4];
        for (int i = 0; i < bees.length; i++) {
            bees[i] = new Bee(i+1, pot);
            bees[i].start();
        }
        bear.start();


        try {
            iterationCounter.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        bear.interrupt();
        for (Bee bee : bees) {
            bee.interrupt();
        }
    }
}
