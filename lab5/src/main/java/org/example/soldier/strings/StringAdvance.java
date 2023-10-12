package org.example.soldier.strings;

public class StringAdvance {
    private final int[] stringTotals;

    public StringAdvance(int n) {
        stringTotals = new int[n];
    }

    public void put(int id, int num) {
        stringTotals[id] = num;
    }
    public boolean stopAdvance() {
        return isDone();
    }

    public boolean isDone() {
        int n = stringTotals.length;
        if (n < 2)
            return true;
        if (n < 3)
            return stringTotals[0] == stringTotals[1];

        if (stringTotals[0] == stringTotals[1]) {
            int count = 2;
            for (int i = 2; i < n; i++) {
                if (stringTotals[i] == stringTotals[1])
                    count++;
            }
            return count >= n-1;
        } else {
            if (stringTotals[0] == stringTotals[2]) {
                for (int i = 3; i < n; i++) {
                    if (stringTotals[i] != stringTotals[0])
                        return false;
                }
                System.out.println("A");
                return true;
            }
            if (stringTotals[1] == stringTotals[2]) {
                for (int i = 3; i < n; i++) {
                    if (stringTotals[i] != stringTotals[1])
                        return false;
                }
                return true;
            }
            return false;
        }
    }
}
