package org.example.filebase.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorImplTest {

    @Test
    void testPhone() {
        int ITERATIONS = 100;
        DataGenerator dataGenerator = new DataGeneratorImpl();
        String phone;
        for (int i = 0; i < ITERATIONS; i++) {
            phone = dataGenerator.generatePhone();
            assertTrue(phone.matches("\\+38 \\d{3} \\d{3} \\d{2} \\d{2}"), "Phone " + phone + "does not match the pattern");
        }
    }

    @Test
    void testName() {
        int ITERATIONS = 5;
        DataGenerator dataGenerator = new DataGeneratorImpl();
        String name;
        for (int i = 0; i < ITERATIONS; i++) {
            name = dataGenerator.generateName();
            System.out.println(name);
            String[] parts = name.split(" ");
            assertEquals(3, parts.length);
            for (int j = 0; j < 3; j++) {
                assertTrue(Character.isUpperCase(parts[j].charAt(0)));
            }
        }
    }

}