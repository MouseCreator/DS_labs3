package org.example.winnie.model;


public class Bee extends Thread{

    private final Pot pot;
    private final Integer id;

    public Bee(int id, Pot pot) {
        this.pot = pot;
        this.id = id;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (pot) {
                while (pot.isFull()) {
                    try {
                        pot.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println("Bee " + id + " added portion");
                pot.addPortion();
                if (pot.isFull()) {
                    pot.notify();
                }
            }
        }
    }
}
