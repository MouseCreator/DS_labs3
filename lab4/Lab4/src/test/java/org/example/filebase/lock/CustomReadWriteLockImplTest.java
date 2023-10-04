package org.example.filebase.lock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class CustomReadWriteLockImplTest {

    @Test
    void testReadLocks() {
        CustomReadWriteLock lock = new CustomReadWriteLockImpl();
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                lock.readLock().lock();
                latch1.countDown();
                latch2.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.readLock().unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                lock.readLock().lock();
                latch2.countDown();
                latch1.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.readLock().unlock();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void testWritesBlocking() {
        CustomReadWriteLock lock = new CustomReadWriteLockImpl();
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger sum = new AtomicInteger(0);
        int N = 100;
        Thread writer = new Thread(() -> {
            try {
                lock.writeLock().lock();
                startLatch.countDown();
                for (int i = 0; i <= N; i++) {
                    sum.addAndGet(i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.writeLock().unlock();
            }
        });
        writer.start();
        ReaderThread reader1 = new ReaderThread(sum, startLatch, lock);
        ReaderThread reader2 = new ReaderThread(sum, startLatch, lock);

        reader1.start();
        reader2.start();


        try {
            writer.join();
            reader1.join();
            reader2.join();

            int expected = estimateSum(N);

            Assertions.assertEquals(expected, reader1.getReadValue());
            Assertions.assertEquals(expected, reader2.getReadValue());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static class CInteger {
        private int i;
        public void increment() {
            i++;
        }
        public int get() {
            return i;
        }
    }
    @Test
    void testWriterMutualBlocking() {
        CustomReadWriteLock lock = new CustomReadWriteLockImpl();
        CInteger cInteger = new CInteger();
        int N = 1000;

        Runnable r = () -> {
            try {
                lock.writeLock().lock();
                for (int i = 0; i < N; i++) {
                    cInteger.increment();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.writeLock().unlock();
            }
        };

        Thread writer = new Thread(r);
        writer.start();
        Thread writer2 = new Thread(r);
        writer2.start();

        try {
            writer.join();
            writer2.join();

            Assertions.assertEquals(2 * N, cInteger.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private int estimateSum(int n) {
        return n * (n+1) / 2;
    }

    private static class ReaderThread extends Thread {

        private int readValue;
        private final AtomicInteger sum;
        private final CountDownLatch startLatch;
        private final CustomReadWriteLock lock;

        private ReaderThread(AtomicInteger sum, CountDownLatch startLatch, CustomReadWriteLock lock) {
            this.sum = sum;
            this.startLatch = startLatch;
            this.lock = lock;
        }

        public void run() {
            try {
                startLatch.await();
                lock.readLock().lock();
                readValue = sum.intValue();
                lock.readLock().unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public int getReadValue() {
            return readValue;
        }
    }
}