package org.example.filebase.pool;

import org.example.filebase.lock.CustomLock;
import org.example.filebase.lock.CustomReadWriteLock;
import org.example.filebase.lock.CustomReadWriteLockImpl;
import org.example.filebase.lock.LockImpl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class PoolImpl<T> implements Pool<T> {
    private final ArrayList<T> list = new ArrayList<>();
    private final Random random = new Random();
    private final CustomReadWriteLock customLock = new CustomReadWriteLockImpl();
    @Override
    public void append(T value) throws InterruptedException {
        try {
            customLock.writeLock().lock();
            list.add(value);
        } finally {
            customLock.writeLock().unlock();
        }
    }

    @Override
    public Optional<T> get() throws InterruptedException {
        try {
            customLock.readLock().lock();
            if (list.size()==0) {
                return Optional.empty();
            }
            if (list.size()==1) {
                return Optional.of(list.get(0));
            }
            int index = random.nextInt(list.size());
            return Optional.of(list.get(index));

        } finally {
            customLock.readLock().unlock();
        }
    }

    @Override
    public Optional<T> remove() throws InterruptedException {
        try {
            customLock.writeLock().lock();
            if (list.size()==0) {
                return Optional.empty();
            }
            if (list.size()==1) {
                return Optional.of(list.remove(0));
            }

            int index = random.nextInt(list.size());
            return Optional.of(list.remove(index));

        } finally {
            customLock.writeLock().unlock();
        }
    }
}
