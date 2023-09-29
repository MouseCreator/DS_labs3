package org.example.winnie.model;

public class Pot {
    private final int sizeLimit;
    private int sizeFilled;
    private int iterationCount;
    private final int numIterations;
    public Pot(int sizeLimit) {
        numIterations = 10;
        this.sizeLimit = sizeLimit;
        sizeFilled = 0;
        iterationCount = 0;
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
        iterationCount++;
    }

    public int iterationCount() {
        return iterationCount;
    }

}
