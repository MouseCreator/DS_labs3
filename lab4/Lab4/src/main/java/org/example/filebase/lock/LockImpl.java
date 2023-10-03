package org.example.filebase.lock;

public class LockImpl implements CustomLock{

    private final Object sync = new Object();

    private boolean locked = false;

    @Override
    public void lock() throws InterruptedException {
        synchronized (sync) {
            while (locked)
                sync.wait();
            locked = true;
        }
    }

    @Override
    public void unlock() {
        synchronized (sync) {
            locked = false;
            sync.notifyAll();
        }
    }
}
