package org.example.smoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProviderImpl implements Provider  {
    private final Random r = new Random();
    @Override
    public List<String> provide() {
        List<String> items = new ArrayList<>();
        int v = r.nextInt(3);
        switch (v) {
            case 0 -> {
                items.add("paper");
                items.add("matches");
            }
            case 1 -> {
                items.add("tobacco");
                items.add("matches");
            }
            case 2 -> {
                items.add("paper");
                items.add("tobacco");
            }
            default -> throw new IllegalStateException("generated invalid output");
        }
        return items;
    }
}
