package org.example.writer;

import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.parser.Parser;
import org.example.extra.Paths;
import org.example.validator.Validator;
import org.example.validator.XSDValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentsWriterTest {

    private Validator validator;
    private Writer<Departments> writer;
    private Parser<Departments> parser;
    private static final String XSD = Paths.TEST_DEPARTMENTS_XSD;
    private static final String XML_OUTPUT = Paths.TEST_DEPARTMENTS_OUTPUT;
    private static final String XML_INPUT = Paths.TEST_DEPARTMENTS_XML;
    @BeforeEach
    void setUp() {
        writer = new DepartmentsWriter();
        validator = new XSDValidator();
        parser = new DepartmentStaxParser();
    }
    @Test
    void write() {
        Departments departmentsInput = parser.parse(XML_INPUT);
        writer.write(XML_OUTPUT, departmentsInput);
        assertTrue(validator.isValid(XSD, XML_OUTPUT));

        Departments departmentsOutput = parser.parse(XML_OUTPUT);
        compareList(departmentsInput.getDepartmentList(), departmentsOutput.getDepartmentList());
        compareList(departmentsInput.getEmployeeList(), departmentsOutput.getEmployeeList());
    }

    <T> void compareList(List<T> list1, List<T> list2) {
        assertEquals(list1.size(), list2.size());
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }
}