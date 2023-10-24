package org.example.cashbox;

import org.example.util.Modelling;

public class RegisterModel implements Modelling {

    public void start() {
        RegisterManager registerManager = new RegisterManager(3);
        int customers = 20;
        for (int i = 1; i <= customers; ++i) {
            registerManager.addCustomer(new Customer(i));
        }
        registerManager.run();
        try {
            registerManager.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
