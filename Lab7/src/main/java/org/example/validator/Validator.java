package org.example.validator;

import org.xml.sax.SAXException;

import java.io.IOException;

public interface Validator {
    boolean isValid(String xsd, String xml);
    void validate(String xsd, String xml) throws SAXException, IOException;
}
