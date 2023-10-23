package org.example.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingModel {

    public void start() {
        int slots = 3;
        Parking parking = new Parking(slots);
        AtomicInteger carsLeave = new AtomicInteger();

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i <= slots; i++) {
            Car car = new Car(String.format("AA 012%d BB", i));
            CarThread carThread = new CarThread(car, parking, carsLeave);
            carThread.start();
            threadList.add(carThread);
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("There are %d parking slots left. %d are parked and %d could not find a free slot.",
                parking.getFleeSlots(), parking.getCarsParked(), parking.getCarsMissed());

    }

    private static class CarThread extends Thread{
        private final Car car;
        private final Parking parking;
        private CarThread(Car car, Parking parking, AtomicInteger carsLeave) {
            this.car = car;
            this.parking = parking;
        }
        @Override
        public void run() {
            parking.parkCar(car);
        }
    }
}
