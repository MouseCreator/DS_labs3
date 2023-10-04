package org.example.filebase.tasks;

import org.example.filebase.manager.FileManager;
import org.example.filebase.manager.FileManagerImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Threads will run until ENTER is pressed");
        clearFile();
        TaskManager taskManager = new TaskManager();
        taskManager.startThreads();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        taskManager.stopThreads();
        scanner.close();
    }

    private static void clearFile() {
        FileManager fileManager = new FileManagerImpl();
        fileManager.write("src/main/resources/file/database.txt", "");
    }
}