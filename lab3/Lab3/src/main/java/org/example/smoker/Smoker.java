package org.example.smoker;

public class Smoker {
    private final String myItem;
    private final String name;
    public Smoker(String name, String myItem) {
        this.myItem = myItem;
        this.name = name;
    }

    public String getItem() {
        return myItem;
    }

    public String name() {
        return name;
    }

    public void smoke() {
        System.out.println(name() + " smokes a cigarette");
    }
}
