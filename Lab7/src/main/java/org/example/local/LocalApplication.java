package org.example.local;

import org.example.controller.ApplicationController;

public class LocalApplication {
    public static void main(String[] args) {
        try(ApplicationController controller = new ApplicationController()) {
            controller.start();
        }

    }
}