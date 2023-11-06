package org.example.filter;

public class EmployeesSQLFilter implements FilterFactory<String> {
    private final SQLParser parser = new SQLParser();
    public String parse(String input) {
        return parser.toSqlWithTable(input, "employees");
    }
}
