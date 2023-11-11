package org.example;

import org.example.controller.ApplicationController;

public class Main {
    public static void main(String[] args) {
        try(ApplicationController controller = new ApplicationController()) {
            controller.start();
        }

    }
}