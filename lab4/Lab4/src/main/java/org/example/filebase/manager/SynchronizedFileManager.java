package org.example.filebase.manager;

import org.example.filebase.lock.CustomReadWriteLock;

public class SynchronizedFileManager implements FileManager{

    private final CustomReadWriteLock customReadWriteLock;
    private final FileManager fileManager;

    public SynchronizedFileManager(FileManager fileManager, CustomReadWriteLock customReadWriteLock) {
        this.customReadWriteLock = customReadWriteLock;
        this.fileManager = fileManager;
    }

    @Override
    public String read(String filename) throws InterruptedException {
        try {
            customReadWriteLock.readLock().lock();
            return fileManager.read(filename);
        } finally {
            customReadWriteLock.readLock().unlock();
        }
    };

    @Override
    public void write(String filename, String content) throws InterruptedException {
        try {
            customReadWriteLock.writeLock().lock();
            fileManager.write(filename, content);
        } finally {
            customReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void append(String filename, String content) throws InterruptedException {
        try {
            customReadWriteLock.writeLock().lock();
            fileManager.append(filename, content);
        } finally {
            customReadWriteLock.writeLock().unlock();
        }
    }
}
