package org.example.filter;

public class DepartmentsSQLFilter {
    private final SQLParser parser = new SQLParser();
    public String parseToSQLQuery(String input) {
       return parser.toSqlWithTable(input, "department");
    }
}
