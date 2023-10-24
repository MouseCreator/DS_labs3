package univ.module;

import java.util.ArrayList;
import java.util.List;

public class Parking {
    private static class ParkingSlots {
        private int spacesLeft;
        private ParkingSlots(int spacesLeft) {
            this.spacesLeft = spacesLeft;
        }
        public int getSpacesLeft() {
            return spacesLeft;
        }
        public void acquire() {
            spacesLeft--;
        }
        public void release() {
            spacesLeft++;
        }
    }
    private static class Car {

        private final int id;
        private static final Object sync = new Object();

        private Car(int id) {
            this.id = id;
        }

        public void acquire(ParkingSlots parking, long waitMillis, long acquireMillis) throws InterruptedException {
            synchronized (sync) {
                 if (parking.getSpacesLeft() > 0) {
                     acquire(parking);
                 } else {
                    long endTime = System.currentTimeMillis() + waitMillis;
                    sync.wait(waitMillis);
                    if (System.currentTimeMillis() < endTime) {
                        acquire(parking);
                    } else {
                        System.out.printf("Car %d didn't acquire parking spot\n", id);
                        return;
                    }
                 }
            }
            Thread.sleep(acquireMillis);
            synchronized (sync) {
                parking.release();
                sync.notifyAll();
                System.out.printf("Car %d released parking spot\n", id);
            }
        }

        private void acquire(ParkingSlots parking) {
            parking.acquire();
            System.out.printf("Car %d acquired parking spot\n", id);
        }
    }

    private static class CarThread extends Thread {
        private final Car car;
        private final long timeout;
        private final long acquire;
        private final ParkingSlots parkingSlots;

        private CarThread(Car car, long timeout, long acquire, ParkingSlots parkingSlots) {
            this.car = car;
            this.timeout = timeout;
            this.acquire = acquire;
            this.parkingSlots = parkingSlots;
        }
        @Override
        public void run() {
            try {
                car.acquire(parkingSlots, timeout, acquire);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        ParkingSlots parkingSlots = new ParkingSlots(5);
        for (int i = 0; i < 13; i++) {
            Car car = new Car(i+1);
            Thread thread = new CarThread(car, 3000, 7000, parkingSlots);
            threadList.add(thread);
            thread.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Model was interrupted");
            }
        }
    }
}