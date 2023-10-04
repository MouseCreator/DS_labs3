package org.example.filebase.tasks;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Threads will run until ENTER is pressed");
        TaskManager taskManager = new TaskManager();
        taskManager.startThreads();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        taskManager.stopThreads();
        scanner.close();
    }
}