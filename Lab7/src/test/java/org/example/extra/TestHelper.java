package org.example.extra;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHelper {
    public static <T> void compareList(List<T> expected, List<T> actual) {
        assertEquals(expected.size(), actual.size());
        int size = expected.size();
        for (int i = 0; i < size; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
