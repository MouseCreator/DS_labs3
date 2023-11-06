package org.example.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentsSQLFilterTest {

    @Test
    void parseToSQLQuery() {
        DepartmentsSQLFilter filter = new DepartmentsSQLFilter();
        String toParse = "name = Bob | age > 20 & age < 24";
        String query = filter.parse(toParse);
        assertEquals("SELECT * FROM department WHERE ((name = Bob) OR (age > 20)) AND (age < 24);", query);
    }
}