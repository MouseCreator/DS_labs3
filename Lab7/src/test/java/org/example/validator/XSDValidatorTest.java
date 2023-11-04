package org.example.validator;

import org.example.extra.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import static org.junit.jupiter.api.Assertions.*;

class XSDValidatorTest {
    private Validator validator;
    private static final String XSD = Paths.TEST_DEPARTMENTS_XSD;
    private static final String XML_CORRECT = Paths.TEST_DEPARTMENTS_XML;
    private static final String XML_WRONG = Paths.TEST_DEPARTMENTS_INVALID;

    @BeforeEach
    void setUp() {
        validator = new XSDValidator();
    }

    @Test
    void isValid() {
        assertTrue(validator.isValid(XSD, XML_CORRECT));
        assertFalse(validator.isValid(XSD, XML_WRONG));
    }

    @Test
    void validate() {
        assertDoesNotThrow(()->validator.validate(XSD,XML_CORRECT));
        assertThrows(SAXParseException.class, ()->validator.validate(XSD, XML_WRONG));
    }
}