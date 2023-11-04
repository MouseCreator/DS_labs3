package org.example.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CommonController {

    private final Scanner scanner;
    private final PrintStream outputStream;
    public static CommonController consoleController() {
        return new CommonController(System.in, System.out);
    }
    public CommonController(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.outputStream = out;
    }

    private void startRequest(String request) {
        outputStream.println(request);
        outputStream.print("> ");
    }
    public boolean askBoolean(String request) {
        startRequest(request);
        String input = scanner.nextLine().trim().toLowerCase();
        while (!input.equals("true") && !input.equals("false")) {
            startRequest("Boolean value (true/false) is expected!");
            input = scanner.nextLine().trim().toLowerCase();
        }
        return Boolean.parseBoolean(input);
    }

    public int askInteger(String request) {
        startRequest(request);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                startRequest("Integer is expected!");
            }
        }
    }

    public long askLong(String request) {
        startRequest(request);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                startRequest("Long integer is expected!");
            }
        }
    }

    public String askString(String request) {
        startRequest(request);
        return scanner.nextLine().trim();
    }

    public void print(String s) {
        outputStream.println(s);
    }
}
