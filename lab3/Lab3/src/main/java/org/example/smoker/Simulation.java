package org.example.smoker;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public void start() {
        Provider provider = new ProviderImpl();
        List<Smoker> smokers = new ArrayList<>(3);
        smokers.set(0, new Smoker("Smoker with paper", "paper"));
        smokers.set(1, new Smoker("Smoker with tobacco", "tobacco"));
        smokers.set(2, new Smoker("Smoker with matches", "matches"));
    }

    private void onSmoke(Smoker smoker, List<String> items) {
        if (items.contains(smoker.getItem())) {
            return;
        }
        smoker.smoke();
    }
}
