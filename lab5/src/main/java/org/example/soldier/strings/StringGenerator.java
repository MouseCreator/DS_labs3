package org.example.soldier.strings;

import java.util.Random;

public class StringGenerator {
    private final Random random = new Random();
    public String generate(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int symbolId = random.nextInt(4);
            String s = switch (symbolId) {
                case 0 -> "A";
                case 1 -> "B";
                case 2 -> "C";
                case 3 -> "D";
                default -> throw new IllegalStateException("Unexpected value: " + symbolId);
            };
            builder.append(s);
        }
        return builder.toString();
    }
}
