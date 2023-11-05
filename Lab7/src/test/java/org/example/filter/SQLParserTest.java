package org.example.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLParserTest {

    @Test
    void parse() {
        String v = "a=0 | b<2 & c>5";
        SQLParser parser = new SQLParser();
        String s = parser.toSql(v);
        assertEquals("((a = 0) OR (b < 2)) AND (c > 5)", s);
    }

    @Test
    void parseWithTable() {
        String v = "a='Hello, world' | b<2 & c>5";
        SQLParser parser = new SQLParser();
        String s = parser.toSqlWithTable(v, "letters");
        assertEquals("SELECT * FROM letters WHERE ((a = 'Hello, world') OR (b < 2)) AND (c > 5)", s);
    }
}