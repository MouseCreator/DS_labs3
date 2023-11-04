package org.example.manager;

import org.example.extra.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerImplTest {

    private FileManagerImpl fileManager;

    @BeforeEach
    void setUp() {
        fileManager = new FileManagerImpl();
    }
    @Test
    void copyXML() {
        fileManager.copyXML(Paths.TEST_DEPARTMENTS_XML, Paths.TEST_DEPARTMENTS_TEMP);
        String s1 = fileManager.readRaw(Paths.TEST_DEPARTMENTS_XML);
        String s2 = fileManager.readRaw(Paths.TEST_DEPARTMENTS_TEMP);
        assertEquals(s1, s2);
    }

    @Test
    void clearXML() {
        fileManager.copyXML(Paths.TEST_DEPARTMENTS_XML, Paths.TEST_DEPARTMENTS_TEMP);
        fileManager.clearXML(Paths.TEST_DEPARTMENTS_TEMP);
        String s = fileManager.readRaw(Paths.TEST_DEPARTMENTS_TEMP);
        assertTrue(s.isEmpty());
    }

    @Test
    void readRaw() {
        String rawXML = fileManager.readRaw(Paths.TEST_DEPARTMENTS_XML);
        assertFalse(rawXML.isEmpty());
        assertTrue(rawXML.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"));
    }
}