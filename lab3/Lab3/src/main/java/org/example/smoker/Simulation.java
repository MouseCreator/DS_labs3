package org.example.smoker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Simulation {
    public void start(int nIterations) {
        Provider provider = new ProviderImpl();
        Table table = new CustomTable();
        Semaphore semaphore = new Semaphore(1, true);

        List<Smoker> smokers = new ArrayList<>(3);
        smokers.add(new Smoker("Smoker with paper", "paper"));
        smokers.add(new Smoker("Smoker with tobacco", "tobacco"));
        smokers.add(new Smoker("Smoker with matches", "matches"));

        List<Thread> threads = new LinkedList<>();
        CountDownLatch countDownLatch = new CountDownLatch(nIterations);
        threads.add(startProvider(provider, table, semaphore, nIterations));
        threads.add(startSmoker(smokers.get(0), table, semaphore, countDownLatch));
        threads.add(startSmoker(smokers.get(1), table, semaphore, countDownLatch));
        threads.add(startSmoker(smokers.get(2), table, semaphore, countDownLatch));

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Thread thread : threads) {
            thread.interrupt();
        }

    }
    private Thread startSmoker(Smoker smoker, Table table, Semaphore semaphore, CountDownLatch countDownLatch) {
        Thread thread = new Thread(()->{
            while (!Thread.interrupted()) {
                try {
                    semaphore.acquire();
                    if (table.hasItems()) {
                        if (table.getItems().contains(smoker.getItem())) {
                            continue;
                        }
                        table.collectItems();
                        smoker.smoke();
                        countDownLatch.countDown();
                    }
                }
                catch (InterruptedException e) {
                    return;
                } finally {
                    semaphore.release();
                }
            }
        });
        thread.start();
        return thread;
    }
    private Thread startProvider(Provider provider, Table table, Semaphore semaphore, int iterations) {
        Thread thread = new Thread(()->{
            int iteration = 0;
            while (iteration < iterations && !Thread.interrupted()) {
                try {
                    semaphore.acquire();
                    if (!table.hasItems()) {
                        List<String> providedItems = provider.provide();
                        System.out.println("Provider provides " + providedItems);
                        table.setItems(providedItems);
                        iteration++;
                    }
                }
                catch (InterruptedException e) {
                    return;
                } finally {
                    semaphore.release();
                }
            }
        });
        thread.start();
        return thread;
    }
}
