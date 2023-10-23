package org.example.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Parking {
    private final Semaphore semaphore;
    private final List<Car> cars;
    private int carsMissed = 0;
    public Parking(int slots) {
        this.semaphore = new Semaphore(slots);
        cars = new ArrayList<>(slots);
    }
    public boolean parkCar(Car car) {
        if(semaphore.tryAcquire()) {
            synchronized (cars) {
                cars.add(car);
            }
            return true;
        }
        carsMissed++;
        return false;
    }

    public boolean freeSlot(Car car) {
        if (!cars.contains(car)) {
            return false;
        }
        synchronized (cars) {
            cars.remove(car);
        }
        semaphore.release();
        return true;
    }

    public int getFleeSlots() {
        return semaphore.availablePermits();
    }

    public int getCarsParked() {
        return cars.size();
    }

    public int getCarsMissed() {
        return carsMissed;
    }
}
