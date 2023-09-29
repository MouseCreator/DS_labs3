package org.example.winnie.model;

import org.example.winnie.iterationCounter.IterationCounter;

public class Simulation {


    public void start() {
        int nIterations = 10;
        IterationCounter iterationCounter = new IterationCounter(nIterations);
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
