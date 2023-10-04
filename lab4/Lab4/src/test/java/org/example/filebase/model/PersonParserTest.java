package org.example.filebase.model;

import org.example.filebase.generator.DataGeneratorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonParserTest {

    @Test
    void testToString() {
        PersonFactory personFactory = new PersonFactory(new DataGeneratorImpl());
        Person person = personFactory.get();

        PersonParser personParser = new PersonParser();

        String s = personParser.toString(person);

        Person fromString = personParser.fromString(s);

        assertEquals(person, fromString);
    }
}