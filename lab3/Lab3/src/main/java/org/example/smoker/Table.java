package org.example.smoker;

import java.util.List;

public interface Table {
    boolean hasItems();
    void setItems(List<String> items);
    List<String> getItems();
    void collectItems();
}
