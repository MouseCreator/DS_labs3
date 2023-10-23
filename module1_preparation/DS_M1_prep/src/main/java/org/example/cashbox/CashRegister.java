package org.example.cashbox;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;

public class CashRegister {
    public Deque<Customer> registerQueue = new LinkedBlockingDeque<>();
    public Optional<Customer> serveCustomer() {
        return Optional.ofNullable(registerQueue.pollFirst());
    }
    public void enqueue(Customer customer) {
        registerQueue.addLast(customer);
    }
}
