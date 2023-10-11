package org.example.soldier.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class StringSimulation {
    public void start(int nThreads) {
        CyclicBarrier barrier = new CyclicBarrier(nThreads);
        StringGenerator generator = new StringGenerator();
        StringAdvance stringAdvance = new StringAdvance(nThreads);

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            String generatedString = generator.generate(20);
            StringChanger changer = new StringChanger(barrier, generatedString, i, stringAdvance);
            Thread thread = new Thread(changer);
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
