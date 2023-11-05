package org.example.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLParserTest {

    void testFromTo(String from, String convertTo) {
        SQLParser parser = new SQLParser();
        String conv = parser.toSql(from);
        assertEquals(convertTo, conv);
    }

    @Test
    void parse() {
        testFromTo( "a=0 | b<2 & c>5", "((a = 0) OR (b < 2)) AND (c > 5)");
    }

    @Test
    void parseWithBrackets() {
        testFromTo("a=0 & (b!=a | c<>a) & d>=4", "" +
                "(a = 0) AND (((b != a) OR (c <> a)) AND (d >= 4))");
    }

    @Test
    void parseWithNegativeSimple() {
        testFromTo("!!a>0 | !b=a", "NOT (NOT ((a > 0) OR (NOT (b = a))))");
    }

    @Test
    void parseBooleans() {
        testFromTo("!a=T & b<>F", "NOT ((a IS TRUE) AND (b IS TRUE))");
    }
    @Test
    void parseWithNegativeComplex() {
        testFromTo("!(!a<0 | b>c) & !d=T", "NOT ((NOT ((a < 0) OR (b > c))) AND (NOT (d IS TRUE)))");
    }

    @Test
    void parseWithTable() {
        String v = "a='Hello, world' | b<2 & c>5";
        SQLParser parser = new SQLParser();
        String s = parser.toSqlWithTable(v, "letters");
        assertEquals("SELECT * FROM letters WHERE ((a = 'Hello, world') OR (b < 2)) AND (c > 5);", s);
    }
}