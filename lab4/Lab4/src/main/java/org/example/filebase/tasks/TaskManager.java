package org.example.filebase.tasks;

import org.example.filebase.lock.CustomReadWriteLockImpl;
import org.example.filebase.manager.FileManagerImpl;
import org.example.filebase.manager.SynchronizedFileManager;
import org.example.filebase.model.Person;
import org.example.filebase.model.PersonParser;
import org.example.filebase.pool.Pool;
import org.example.filebase.pool.PoolImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class TaskManager {
    private final Pool<String> namePool = new PoolImpl<>();
    private final PersonParser personParser = new PersonParser();
    private final static String FILENAME = "file/database.txt";
    private final Pool<String> phonePool = new PoolImpl<>();

    private final Random random = new Random();
    private final SynchronizedFileManager synchronizedFileManager =
            new SynchronizedFileManager(new FileManagerImpl(), new CustomReadWriteLockImpl());

    private List<Thread> threadList = new ArrayList<>(3);

    public void startThreads() {
    }

    public void stopThreads() {

    }

    private class NameFinderThread extends Thread {
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    String data = synchronizedFileManager.read(FILENAME);

                    Optional<String> targetName = namePool.get();
                    if (targetName.isEmpty()) {
                        System.out.println("NAME: Cannot select target name, because database is empty");
                        continue;
                    }
                    String name = targetName.get();
                    List<Person> personList = personParser.fromStringMulti(data);
                    List<Person> filtered = personList.stream().filter(t -> t.getName().equals(name)).collect(Collectors.toList());
                    System.out.println(toPrint(name, filtered));
                    Thread.sleep(500 + random.nextInt(500));
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        private String toPrint(String name, List<Person> filtered) {
            StringBuilder s = new StringBuilder("Phones of people with name " + name + ":\n");
            for (Person p : filtered) {
                s.append('\t').append(p.getPhone()).append('\n');
            }
            return s.toString();

        }
    }
}
