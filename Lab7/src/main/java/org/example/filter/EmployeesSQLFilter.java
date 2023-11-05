package org.example.filter;

public class EmployeesSQLFilter {
    private final SQLParser parser = new SQLParser();

    public String parseToSQLQuery(String input) {
        return parser.toSqlWithTable(input, "employee");
    }
}
