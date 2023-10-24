package org.example.cashbox;

import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CashRegister {
    private final long workTime;
    private final Deque<Customer> registerQueue = new LinkedBlockingDeque<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final int id;
    public CashRegister(int id, long workTime) {
        this.id = id;
        this.workTime = workTime;
    }

    public void serveCustomer() {
        readWriteLock.readLock().lock();
        try {
            Customer customer = registerQueue.peek();
            if (customer == null)
                return;
            serve(customer);
            registerQueue.pollFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private void serve(Customer customer) {
        try {
            System.out.println("Register "  + id +  " serving " + customer.id());
            Thread.sleep(workTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void enqueue(Customer customer) {
        readWriteLock.writeLock().lock();
        try {
            registerQueue.addLast(customer);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public int length() {
        return registerQueue.size();
    }
    @Override
    public String toString() {
        return "CashRegister{" +
                "registerQueue=" + registerQueue +
                ", id=" + id +
                '}';
    }

    public int id() {
        return id;
    }

    public Optional<Customer> pollEnd() {
        readWriteLock.writeLock().lock();
        try {
            return Optional.ofNullable(registerQueue.pollLast());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public boolean working() {
        readWriteLock.readLock().lock();
        try {
            return !registerQueue.isEmpty();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


}
