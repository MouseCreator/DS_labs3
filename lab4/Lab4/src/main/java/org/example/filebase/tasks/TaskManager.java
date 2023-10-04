package org.example.filebase.tasks;

import org.example.filebase.generator.DataGeneratorImpl;
import org.example.filebase.lock.CustomReadWriteLockImpl;
import org.example.filebase.manager.FileManagerImpl;
import org.example.filebase.manager.SynchronizedFileManager;
import org.example.filebase.model.Factory;
import org.example.filebase.model.Person;
import org.example.filebase.model.PersonFactory;
import org.example.filebase.model.PersonParser;
import org.example.filebase.pool.Pool;
import org.example.filebase.pool.PoolImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TaskManager {
    private final Pool<Person> personPool = new PoolImpl<>();
    private final PersonParser personParser = new PersonParser();
    private final Factory<Person> personFactory = new PersonFactory(new DataGeneratorImpl());
    private final static String FILENAME = "src/main/resources/file/database.txt";
    private final Random random = new Random();
    private final SynchronizedFileManager synchronizedFileManager =
            new SynchronizedFileManager(new FileManagerImpl(), new CustomReadWriteLockImpl());

    private final List<Thread> threadList = new ArrayList<>(3);

    public void startThreads() {
        PhoneFinderThread phoneFinderThread = new PhoneFinderThread();
        phoneFinderThread.start();
        threadList.add(phoneFinderThread);

        NameFinderThread nameFinderThread = new NameFinderThread();
        nameFinderThread.start();
        threadList.add(nameFinderThread);

        WriterThread writerThread = new WriterThread();
        writerThread.start();
        threadList.add(writerThread);
    }

    public void stopThreads() {
        for (Thread thread : threadList) {
            thread.interrupt();
        }
    }

    private class NameFinderThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                String data = synchronizedFileManager.read(FILENAME);

                Optional<Person> targetName = personPool.get();
                if (targetName.isEmpty()) {
                    System.out.println("NAME: Cannot select target name, because database is empty");
                    Thread.sleep(500 + random.nextInt(500));
                    continue;
                }
                String name = targetName.get().getName();
                List<Person> personList = personParser.fromStringMulti(data);
                List<Person> filtered = personList.stream().filter(t -> t.getName().equals(name)).collect(Collectors.toList());
                System.out.println(toPrint(name, filtered));
                Thread.sleep(500 + random.nextInt(500));
            } catch (InterruptedException e) {
                    return;
                }
            }
        }
        private String toPrint(String name, List<Person> filtered) {
            StringBuilder s = new StringBuilder("NAME: Phones of people with name " + name + ":\n");
            for (Person p : filtered) {
                s.append('\t').append(p.getPhone()).append('\n');
            }
            return s.toString();

        }
    }

    private class PhoneFinderThread extends Thread {
        @Override
        public void run() {

            while (!Thread.interrupted()) {
                try {
                    String data = synchronizedFileManager.read(FILENAME);

                    Optional<Person> targetPhone = personPool.get();
                    if (targetPhone.isEmpty()) {
                        System.out.println("PHONE: Cannot select target phone, because database is empty");
                        Thread.sleep(500 + random.nextInt(500));
                        continue;
                    }
                    String phone = targetPhone.get().getPhone();
                    List<Person> personList = personParser.fromStringMulti(data);
                    List<Person> filtered = personList.stream().filter(t -> t.getPhone().equals(phone)).collect(Collectors.toList());
                    System.out.println(toPrint(phone, filtered));
                    Thread.sleep(500 + random.nextInt(500));
                }
                catch (InterruptedException e) {
                    return;
                }

            }
        }
        private String toPrint(String phone, List<Person> filtered) {
            StringBuilder s = new StringBuilder("PHONE: Names of people with phone " + phone + ":\n");
            for (Person p : filtered) {
                s.append('\t').append(p.getName()).append('\n');
            }
            return s.toString();

        }
    }

    private class WriterThread extends Thread {
        @Override
        public void run() {

            boolean deletionInterested = false;
            double deletionChance = 0.3;
            int dontDeleteUntil = 10;
            AtomicInteger itemsCount = new AtomicInteger();
            while (!Thread.interrupted()) {
                try {
                    boolean delete = false;
                    if (deletionInterested) {
                        delete = random.nextDouble() < deletionChance;
                    }

                    if (delete) {
                        Optional<Person> remove = personPool.remove();
                        if (remove.isPresent()) {
                            String data = synchronizedFileManager.read(FILENAME);
                            List<Person> personList = personParser.fromStringMulti(data);
                            List<Person> collect = personList.stream().filter(t -> !t.equals(remove.get())).toList();
                            String updated = personParser.toStringMulti(collect);
                            synchronizedFileManager.write(FILENAME, updated);
                            itemsCount.decrementAndGet();
                            print("WRITE: Removed ", remove.get());
                        } else {
                            delete = false;
                        }
                    }
                    if (!delete) {
                        Person newPerson = personFactory.get();
                        String data = synchronizedFileManager.read(FILENAME);
                        List<Person> personList = personParser.fromStringMulti(data);
                        personList.add(newPerson);
                        String updated = personParser.toStringMulti(personList);
                        synchronizedFileManager.write(FILENAME, updated);
                        personPool.append(newPerson);
                        itemsCount.incrementAndGet();
                        print("WRITE: Added ", newPerson);
                    }


                    if (itemsCount.get() > dontDeleteUntil) {
                        deletionInterested = true;
                    }

                    Thread.sleep(1000 + random.nextInt(500));

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        private void print(String action, Person person) {
            System.out.println(action + " person " + personParser.toString(person));
        }
    }
}
