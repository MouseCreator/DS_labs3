package org.example.soldier.strings;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class StringChanger implements Runnable {
    private final CyclicBarrier barrier;
    private final Random random = new Random();
    private String string;

    private int id;
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
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            if (stringAdvance.stopAdvance())
                return;
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
        int charId = random.nextInt(string.length());
        char[] chars = string.toCharArray();
        char at = chars[charId];
        if (at == 'A') {
            chars[charId] = 'B';
        } else if (at == 'B') {
            chars[charId] = 'A';
        } else if (at == 'C') {
            chars[charId] = 'C';
        } else if (at == 'D') {
            chars[charId] = 'D';
        }
        return new String(chars);
    }
}
