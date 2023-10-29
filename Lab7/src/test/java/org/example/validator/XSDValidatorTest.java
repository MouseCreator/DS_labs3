package org.example.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import static org.junit.jupiter.api.Assertions.*;

class XSDValidatorTest {
    private Validator validator;
    private static final String XSD = "src/test/resources/test-departments.xsd";
    private static final String XML_CORRECT = "src/test/resources/test-departments.xml";
    private static final String XML_WRONG = "src/test/resources/test-invalid-departments.xml";

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