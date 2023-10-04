package org.example.filebase.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

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
}