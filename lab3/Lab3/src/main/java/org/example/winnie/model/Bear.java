package org.example.winnie.model;

public class Bear extends Thread{
    private final Pot pot;
    public Bear(Pot pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (pot) {
                while (!pot.isFull()) {
                    try {
                        pot.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println("Bear eats the honey");
                pot.toEmpty();
                pot.notifyAll();
            }
        }
    }
}
