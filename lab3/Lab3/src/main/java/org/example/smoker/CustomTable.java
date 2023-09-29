package org.example.smoker;

import java.util.ArrayList;
import java.util.List;

public class CustomTable implements Table {
    private final List<String> items = new ArrayList<>();
    @Override
    public boolean hasItems() {
        return !items.isEmpty();
    }

    @Override
    public void setItems(List<String> items) {
        this.items.addAll(items);
    }

    @Override
    public List<String> getItems() {
        return items;
    }

    @Override
    public void collectItems() {
        items.clear();
    }
}
