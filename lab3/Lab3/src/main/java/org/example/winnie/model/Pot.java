package org.example.winnie.model;

public class Pot {
    private final int sizeLimit;
    private int sizeFilled;
    public Pot(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        sizeFilled = 0;
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
    }
}
