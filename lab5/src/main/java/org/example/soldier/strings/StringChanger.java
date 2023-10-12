package org.example.soldier.strings;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class StringChanger implements Runnable {
    private final CyclicBarrier barrier;
    private final Random random = new Random();
    private String string;

    private final int id;
    private final StringAdvance stringAdvance;

    public StringChanger(CyclicBarrier barrier, String initalString, int id, StringAdvance stringAdvance) {
        this.barrier = barrier;
        string = initalString;
        this.id = id;
        this.stringAdvance = stringAdvance;
    }

    public void run() {
        while (true) {
            string = changeString(string);
            int aCount = countChar(string, 'A');
            int bCount = countChar(string, 'B');
            int total = aCount+bCount;
            stringAdvance.put(id, total);
            try {
                barrier.await();
                System.out.println("STRING" + id + ": " + string + ": " + total);
                if (stringAdvance.stopAdvance()) {
                    return;
                }
                barrier.await();
                if (id==0) {
                    System.out.println();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int countChar(String string, char targetChar) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == targetChar) {
                count++;
            }
        }
        return count;
    }

    private String changeString(String string) {
        boolean change = random.nextBoolean();
        if (!change)
            return string;
        int charId = random.nextInt(string.length());
        char[] chars = string.toCharArray();
        char at = chars[charId];
        if (at == 'A') {
            chars[charId] = 'C';
        } else if (at == 'B') {
            chars[charId] = 'D';
        } else if (at == 'C') {
            chars[charId] = 'A';
        } else if (at == 'D') {
            chars[charId] = 'B';
        }
        return new String(chars);
    }
}
