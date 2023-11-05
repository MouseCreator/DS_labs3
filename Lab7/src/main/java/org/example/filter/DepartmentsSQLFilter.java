package org.example.filter;

public class DepartmentsSQLFilter implements FilterFactory<String> {
    private final SQLParser parser = new SQLParser();
    public String parse(String input) {
       return parser.toSqlWithTable(input, "department");
    }
}
