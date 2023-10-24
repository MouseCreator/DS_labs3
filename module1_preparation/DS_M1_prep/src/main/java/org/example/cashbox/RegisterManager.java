package org.example.cashbox;

import java.util.*;

public class RegisterManager {
    private final List<CashRegister> cashRegisters;
    private final List<Thread> workerThreads = new ArrayList<>();
    public RegisterManager(int size) {
        cashRegisters = new ArrayList<>(size);
        addRegisters(size);
    }

    private void addRegisters(int size) {
        for (int i = 0; i < size; i++) {
            cashRegisters.add(new CashRegister(i, i * 500L + 500L));
        }
    }
    private CashRegister getMinLengthRegister() {
        Optional<CashRegister> min = cashRegisters.stream().min(Comparator.comparingInt(CashRegister::length));
        if (min.isEmpty()) {
            throw new IllegalStateException("No min register");
        }
        return min.get();
    }
    public void addCustomer(Customer customer) {
        CashRegister register = getMinLengthRegister();
        register.enqueue(customer);
    }

    private void trySwap(CashRegister cashRegister) {
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (CashRegister register : cashRegisters) {
                if (register.length() > cashRegister.length()) {
                    moveFromTo(register, cashRegister);
                }
            }
        }
    }

    private void moveFromTo(CashRegister register1, CashRegister register2) {
        Optional<Customer> c = register1.pollEnd();
        if (c.isEmpty()) {
            return;
        }
        System.out.printf("Moved customer %d from %d to %d\n", c.get().id(), register1.id(), register2.id());
        register2.enqueue(c.get());
    }
    public void run() {
        for (CashRegister cashRegister : cashRegisters) {
            RegisterThread registerThread = new RegisterThread(cashRegister, this);
            workerThreads.add(registerThread);
            registerThread.start();
        }
    }

    public void await() throws InterruptedException {
        for (Thread thread : workerThreads) {
            thread.join();
        }
    }

    private static class RegisterThread extends Thread {
        private final CashRegister register;
        private final RegisterManager manager;
        private RegisterThread(CashRegister register, RegisterManager manager) {
            this.register = register;
            this.manager = manager;
        }
        @Override
        public void run() {
            System.out.println("Register " + register.id() + " started");
            while (register.working()) {
                register.serveCustomer();
                manager.trySwap(register);
            }
            System.out.println("Register " + register.id() + " finished");
        }
    }
}
