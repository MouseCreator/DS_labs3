package univ.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Extra task
 */
public class Airline {
    private static class Airplane {
        private final int id;
        private final int capacity;
        private final int range;

        private Airplane(int id, int capacity, int range) {
            this.id = id;
            this.capacity = capacity;
            this.range = range;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getRange() {
            return range;
        }

        public int getId() {
            return id;
        }
    }

    private static class Communication {
        private final Semaphore terminalSemaphore;
        private final Semaphore trapSemaphore;
        private Communication(int terminalCapacity, int trapCapacity) {
            terminalSemaphore = new Semaphore(terminalCapacity);
            trapSemaphore = new Semaphore(trapCapacity);
        }

        public void communicateTrap() {
            communicate(trapSemaphore);
        }
        public void communicateTerminal() {
            communicate(terminalSemaphore);
        }

        public void communicate(Semaphore semaphore) {
            try {
                semaphore.acquire();
                Thread.sleep(1000);
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static class Destination {
        int passengersToTransfer;
        int id;
        private final Communication communication;

        private boolean done = false;
        private final Object sync = new Object();
        private final CountDownLatch latch;
        public Destination(int id, int passengersToTransfer, Communication communication, CountDownLatch latch) {
            this.passengersToTransfer = passengersToTransfer;
            this.id = id;
            this.communication = communication;
            this.latch = latch;
        }

        private void serveAirplane(Airplane airplane) {
            communication.communicateTrap();
            if (done) {
                return;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            addPassengers(airplane);

            communication.communicateTerminal();
        }

        private void addPassengers(Airplane airplane) {
            synchronized (sync) {
                int toServe = Math.min(passengersToTransfer, airplane.getCapacity());
                passengersToTransfer -= toServe;
                if (passengersToTransfer == 0) {
                    done = true;
                    latch.countDown();
                }
                System.out.println("Plane " + airplane.getId() + " got passengers in destination " + id + ". Passengers left: " + passengersToTransfer);
            }
        }
    }

    private static class PlaneRunnable implements Runnable {

        private final Airplane airplane;
        private final List<Destination> destinationList;
        private final CountDownLatch latch;
        private PlaneRunnable(Airplane airplane, List<Destination> destinationList, CountDownLatch latch) {
            this.airplane = airplane;
            this.destinationList = destinationList;
            this.latch = latch;
        }

        @Override
        public void run() {
            int step = airplane.getRange();
            int currentAirline = 0;
            while (latch.getCount()>0) {
                Destination destination = destinationList.get(currentAirline);
                destination.serveAirplane(airplane);
                currentAirline = (currentAirline + step) % destinationList.size();
            }
        }

    }

    public static void main(String[] args) {
        Random random = new Random();
        List<Destination> destinationList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(7);
        for (int i = 0; i < 7; i++) {
            Communication communication = new Communication(2, 2);
            destinationList.add(new Destination(i, random.nextInt(500, 800), communication, countDownLatch));
        }

        for (int i = 0; i < 4; i++) {
            Airplane airplane = new Airplane(i, random.nextInt(100,200), i+1);
            PlaneRunnable r = new PlaneRunnable(airplane, destinationList, countDownLatch);
            Thread thread1 = new Thread(r);
            thread1.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
