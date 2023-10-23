package org.example.cashbox;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;

public class CashRegister {
    public Deque<Customer> registerQueue = new LinkedBlockingDeque<>();
    private final int id;
    public CashRegister(int id) {
        this.id = id;
    }

    public void serveCustomer() {
        registerQueue.pollFirst();
    }
    public void enqueue(Customer customer) {
        registerQueue.addLast(customer);
    }

    public int length() {
        return registerQueue.size();
    }

    public Optional<Customer> peek() {
        return Optional.ofNullable(registerQueue.peek());
    }

    @Override
    public String toString() {
        return "CashRegister{" +
                "registerQueue=" + registerQueue +
                ", id=" + id +
                '}';
    }

    public int position(Customer customer) {
        int i = 0;
        for (Customer c : registerQueue) {
            if (c.equals(customer))
                return i;
        }
        throw new NoSuchElementException(customer + " is not in queue");
    }

    public void remove(Customer customer) {
        registerQueue.remove(customer);
    }
}
