package org.example.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentsSQLFilterTest {

    @Test
    void parseToSQLQuery() {
        DepartmentsSQLFilter filter = new DepartmentsSQLFilter();
        String toParse = "name = Bob | age > 20 & age < 24";
        String query = filter.parseToSQLQuery(toParse);
        System.out.println(query);
    }
}