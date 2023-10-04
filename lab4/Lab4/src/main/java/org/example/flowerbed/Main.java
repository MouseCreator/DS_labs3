package org.example.flowerbed;

import org.example.filebase.manager.FileManager;
import org.example.filebase.manager.FileManagerImpl;
import org.example.flowerbed.model.FlowerbedSimulation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Threads will run until ENTER is pressed");
        FlowerbedSimulation flowerbedSimulation = new FlowerbedSimulation(10, 10);
        refreshFlowerFile();
        flowerbedSimulation.startThreads();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        flowerbedSimulation.stopThreads();
        scanner.close();
    }

    private static void refreshFlowerFile() {
        String filename = "src/main/resources/file/flowers.txt";
        FileManager fileManager = new FileManagerImpl();
        fileManager.write(filename, "");
    }
}
