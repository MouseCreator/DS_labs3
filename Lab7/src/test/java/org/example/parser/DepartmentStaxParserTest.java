package org.example.parser;

import org.example.model.Departments;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentStaxParserTest {
    private static final String TEST_XML = "src/test/resources/test-departments.xml";
    @Test
    void parse() {
        Parser<Departments> parser = new DepartmentStaxParser();
        Departments departments = parser.parse(TEST_XML);
        assertEquals(3, departments.getDepartmentList().size());
    }
}