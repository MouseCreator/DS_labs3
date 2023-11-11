package org.example.controller;

import java.io.*;

public class CommonController {

    private BufferedReader reader;
    private final PrintStream outputStream;
    public static CommonController consoleController() {
        return new CommonController(System.in, System.out);
    }
    public CommonController(InputStream in, PrintStream out) {
        setInputStream(in);
        this.outputStream = out;
    }

    private void setInputStream(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    private void startRequest(String request) {
        outputStream.println(request);
        outputStream.print("> ");
    }
    public boolean askBoolean(String request) {
        Boolean result = getBoolean(request);
        while (result == null){
            result = getBoolean("Boolean value is expected!");
        }
        return result;
    }

    private Boolean getBoolean(String request) {
        startRequest(request);
        String input;
        try {
            input = reader.readLine();
        } catch (IOException e) {
            return null;
        }
        return toBoolean(input);
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
                return Integer.parseInt(reader.readLine().trim());
            } catch (NumberFormatException | IOException e) {
                startRequest("Integer is expected!");
            }
        }
    }

    public long askLong(String request) {
        startRequest(request);
        while (true) {
            try {
                return Long.parseLong(reader.readLine().trim());
            } catch (NumberFormatException | IOException e) {
                startRequest("Long value is expected!");
            }
        }
    }

    public String askString(String request) {
        startRequest(request);
        String s;
        try {
            s = reader.readLine().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        while (s.isEmpty()) {
            startRequest("String is expected!");
            try {
                s = reader.readLine().trim();
            } catch (IOException e) {
                s = "";
            }
        }
        return s;
    }

    public void print(String s) {
        outputStream.println(s);
    }

    protected void supplyInput(BufferedReader reader) {
        this.reader = reader;
    }

    public String askStringOr(String request, String defaultValue) {
        startRequest(request);
        String s;
        try {
            s = reader.readLine().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (s.isEmpty()) {
            return defaultValue;
        }
        return s;
    }
    public int askIntegerOr(String request, int defaultValue) {
        startRequest(request);
        while (true) {
            try {
                String value = reader.readLine().trim();
                if (value.isEmpty())
                    return defaultValue;
                return Integer.parseInt(value);
            } catch (NumberFormatException | IOException e) {
                startRequest("Integer is expected!");
            }
        }
    }
    public long askLongOr(String request, long defaultValue) {
        startRequest(request);
        while (true) {
            try {
                String value = reader.readLine().trim();
                if (value.isEmpty())
                    return defaultValue;
                return Long.parseLong(value);
            } catch (NumberFormatException | IOException e) {
                startRequest("Long value is expected!");
            }
        }
    }
}
