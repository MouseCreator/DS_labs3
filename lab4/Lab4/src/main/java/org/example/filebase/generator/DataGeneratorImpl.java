package org.example.filebase.generator;

import java.util.Random;

public class DataGeneratorImpl implements DataGenerator {
    private final Random random = new Random();
    @Override
    public String generatePhone() {
        return "+38 " + generateDigits(1000) + " " + generateDigits(1000) +
                " " + generateDigits(100) + " " + generateDigits(100);
    }

    private String generateDigits(int bound) {
        StringBuilder s = new StringBuilder(String.valueOf(random.nextInt(bound)));
        int len = String.valueOf(bound).length() - s.length();
        for (int i = 1; i < len; i++) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    @Override
    public String generateName() {
        return capitalize(generatePart()) + " " + capitalize(generatePart()) + " " + capitalize(generatePart());
    }

   private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private String generatePart() {
        int length = random.nextInt(3, 6);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(generateCombination());
        }
        return builder.toString();
    }
    private String generateCombination() {
        String result = "";
        double startsWithVowelChance = 0.2;
        if (random.nextDouble() < startsWithVowelChance) {
            result += randomVowel();
        }
        result += randomConsonant();
        double endsWithVowelChance = 0.8;
        if (random.nextDouble() < endsWithVowelChance) {
            result += randomVowel();
        }
        return result;
    }
    private static final char[] VOWELS = {'a', 'e', 'i', 'o', 'u'};
    private static final char[] CONSONANTS = {
            'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n',
            'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'
    };
    private String randomVowel() {
        return String.valueOf(VOWELS[random.nextInt(VOWELS.length)]);
    }
    private String randomConsonant() {
        return String.valueOf(CONSONANTS[random.nextInt(CONSONANTS.length)]);
    }
}
