package org.example.flowerbed.model;

public class Flower {
    public State getState() {
        return state;
    }

    public Flower(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        GROWING, WITHERED, DEAD
    }

    private State state;

    public String print() {
        return switch (state) {
            case GROWING -> "&";
            case WITHERED -> "%";
            case DEAD -> "#";
        };
    }
}
