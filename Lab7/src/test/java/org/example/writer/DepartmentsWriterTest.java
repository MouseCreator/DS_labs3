package org.example.writer;

import org.example.extra.TestHelper;
import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.parser.Parser;
import org.example.extra.Paths;
import org.example.validator.Validator;
import org.example.validator.XSDValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        TestHelper.compareList(departmentsInput.getDepartmentList(), departmentsOutput.getDepartmentList());
        TestHelper.compareList(departmentsInput.getEmployeeList(), departmentsOutput.getEmployeeList());
    }


}