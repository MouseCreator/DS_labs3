package org.example.cashbox;

import java.util.*;

public class RegisterManager {
    private final List<CashRegister> cashRegisters;
    public RegisterManager(int size) {
        cashRegisters = new ArrayList<>(size);
        addRegisters(size);
    }

    private void addRegisters(int size) {
        for (int i = 0; i < size; i++) {
            cashRegisters.add(new CashRegister(i));
        }
    }
    private CashRegister getMinLengthRegister() {
        Optional<CashRegister> min = cashRegisters.stream().min(Comparator.comparingInt(CashRegister::length));
        if (min.isEmpty()) {
            throw new IllegalStateException("No min register");
        }
        return min.get();
    }
    public CashRegister addCustomer(Customer customer) {
        CashRegister register = getMinLengthRegister();
        register.enqueue(customer);
        return register;
    }

    public boolean isServing(Customer customer) {
        for (CashRegister cashRegister : cashRegisters) {
            Optional<Customer> peek = cashRegister.peek();
            if (peek.isEmpty())
                continue;
            if (peek.get().equals(customer))
                return true;
        }
        return false;
    }

    public void complete(Customer customer) {
        for (CashRegister cashRegister : cashRegisters) {
            Optional<Customer> peek = cashRegister.peek();
            if (peek.isEmpty())
                continue;
            if (peek.get().equals(customer)) {
                cashRegister.serveCustomer();
                return;
            }
        }
        throw new IllegalStateException("Completed not serving customer");
    }

    public void serve(Customer customer) {
        System.out.println("Serving " + customer);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public CashRegister trySwap(CashRegister cashRegister, Customer customer) {
        CashRegister minRegister = getMinLengthRegister();
        if (minRegister == cashRegister)
            return cashRegister;
        int customerPos = cashRegister.position(customer);
        if (minRegister.length() < customerPos) {
            cashRegister.remove(customer);
            minRegister.enqueue(customer);
            return minRegister;
        }
        return cashRegister;
    }
}
