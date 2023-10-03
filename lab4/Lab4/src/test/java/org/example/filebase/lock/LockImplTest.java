package org.example.filebase.lock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LockImplTest {
    @Test
    void testSynchronized() {
        LockImpl lock = new LockImpl();
        StringBuilder actual = new StringBuilder();
        Runnable appendString = ()->{
            try {
                lock.lock();
                for (int i = 'A'; i <= 'Z'; i++) {
                    actual.append((char) i);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        };
        Thread thread1 = new Thread(appendString);
        thread1.start();

        Thread thread2 = new Thread(appendString);
        thread2.start();


        int NThreads = 2;
        String expected = getExpected(NThreads);

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String actualStr = actual.toString();

        assertEquals(expected, actualStr);
    }

    private String getExpected(int n) {
        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < n; m++) {
            for (int i = 'A'; i <= 'Z'; i++) {
                builder.append((char) i);
            }
        }
        return builder.toString();
    }
}