package org.example.cashbox;

import org.example.util.Modelling;

import java.util.ArrayList;
import java.util.List;

public class RegisterModel implements Modelling {

    private final Object object = new Object();

    public void start() {
        RegisterManager registerManager = new RegisterManager(3);
        List<Thread> threadList = new ArrayList<>();
        int customers = 10;
        for (int i = 0; i < customers; ++i) {
            CustomerThread customerThread = new CustomerThread(registerManager, new Customer(i + 1));
            threadList.add(customerThread);
            customerThread.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class CustomerThread extends Thread {

        private final RegisterManager registerManager;
        private final Customer customer;

        private CustomerThread(RegisterManager registerManager, Customer customer) {
            this.registerManager = registerManager;
            this.customer = customer;
        }

        public void start() {
            try {
                synchronized (object) {
                    CashRegister cashRegister = registerManager.addCustomer(customer);
                    while (true) {
                        object.wait();
                        if (registerManager.isServing(customer)) {
                            registerManager.serve(customer);
                            registerManager.complete(customer);
                            object.notifyAll();
                            return;
                        }
                        cashRegister = registerManager.trySwap(cashRegister, customer);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
