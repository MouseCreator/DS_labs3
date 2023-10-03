package org.example.filebase.lock;

public class CustomReadWriteLockImpl implements CustomReadWriteLock {

    private final ReadLock readLock = new ReadLock();
    private final WriteLock writeLock = new WriteLock();
    @Override
    public CustomLock readLock() {
        return readLock;
    }

    @Override
    public CustomLock writeLock() {
        return writeLock;
    }

    private final Object writeSync = new Object();
    private final Object readSync = new Object();
    private int readEntered = 0;
    private int writeEntered = 0;
    private boolean writeLocked = false;
    private class ReadLock implements CustomLock {

        @Override
        public void lock() throws InterruptedException {
            synchronized (readSync) {
                ++readEntered;
            }
            synchronized (writeSync) {
                while (writeLocked && writeEntered > 0)
                    writeSync.wait();
                writeLocked = true;
            }
        }

        @Override
        public void unlock() {
            synchronized (readSync) {
                readEntered--;
                synchronized (writeSync) {
                    if (readEntered == 0) {
                        writeSync.notifyAll();
                    }
                }
            }
        }
    }

    private class WriteLock implements CustomLock {

        @Override
        public void lock() throws InterruptedException {
            synchronized (writeSync) {
                writeEntered++;
                while (writeLocked)
                    writeSync.wait();
                writeLocked = true;
            }
        }

        @Override
        public void unlock() {
            synchronized (writeSync) {
                writeLocked = false;
                writeEntered--;
                writeSync.notifyAll();
            }
        }
    }
}
