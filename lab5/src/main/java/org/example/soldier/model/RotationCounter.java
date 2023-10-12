package org.example.soldier.model;

public class RotationCounter {
    private final int[] counts;

    public RotationCounter(int n) {
        this.counts = new int[n];
    }

    public void put(int id, int count) {
        counts[id] = count;
    }
    public boolean finished() {
        for (int count : counts) {
            if (count > 0)
                return false;
        }
        return true;
    }
}
