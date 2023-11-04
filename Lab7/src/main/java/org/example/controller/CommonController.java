package org.example.controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CommonController {

    private Scanner scanner;
    private final PrintStream outputStream;
    public static CommonController consoleController() {
        return new CommonController(System.in, System.out);
    }
    public CommonController(InputStream in, PrintStream out) {
        setInputStream(in);
        this.outputStream = out;
    }

    private void setInputStream(InputStream in) {
        this.scanner = new Scanner(in);
    }

    private void startRequest(String request) {
        outputStream.println(request);
        outputStream.print("> ");
    }
    public boolean askBoolean(String request) {
        startRequest(request);
        Boolean result = null;
        while (result == null){
            String input = scanner.nextLine();
            result = toBoolean(input);
        }
        return result;
    }

    private String formatInput(String input) {
        return input.trim().toLowerCase();
    }
    private Boolean toBoolean(String s) {
        String b = formatInput(s);
        return switch (b) {
            case "y", "yes", "true", "t" -> true;
            case "n", "no", "false", "f" -> false;
            default -> null;
        };
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
