package org.example.controller;

public interface CommonController {
    boolean askBoolean(String request);
    int askInteger(String request);
    long askLong(String request);
    String askString(String request);
    void print(String s);
    String askStringOr(String request, String defaultValue);
    int askIntegerOr(String request, int defaultValue);
    long askLongOr(String request, long defaultValue);

}
