package org.example.winnie.model;

import org.example.winnie.iterationCounter.IterationCounter;

public class Pot {
    private final int sizeLimit;
    private int sizeFilled;

    public IterationCounter getIterationCounter() {
        return iterationCounter;
    }

    private final IterationCounter iterationCounter;
    public Pot(int sizeLimit, IterationCounter iterationCounter) {
        this.sizeLimit = sizeLimit;
        sizeFilled = 0;
        this.iterationCounter = iterationCounter;
    }

    public void addPortion() {
        if (isFull()) {
            throw new IllegalStateException("Pot overflow!");
        }
        sizeFilled++;
    }

    public boolean isFull() {
        return sizeFilled == sizeLimit;
    }

    public void toEmpty() {
        sizeFilled = 0;
        iterationCounter.done();
    }

}
