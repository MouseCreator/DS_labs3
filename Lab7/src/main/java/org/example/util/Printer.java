package org.example.util;

import org.example.controller.CommonController;

import java.util.List;

public class Printer {

    private final CommonController controller;

    public Printer(CommonController controller) {
        this.controller = controller;
    }

    public <T> void print(List<T> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (T i : list) {
            builder.append("\n").append(i);
        }
        builder.append("\n]");
        controller.print(builder.toString());
    }

    public <T> void print(T obj) {
       controller.print(obj.toString());
    }
}
