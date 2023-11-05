package org.example.filter;

import java.util.HashMap;
import java.util.Map;

public class DepartmentsSQLFilter {
    public String parseToSQLQuery(String input) {
        input = input.replaceAll("\\|", " OR ").replaceAll("&", " AND ");
        String[] conditions = input.split("\\|");
        Map<String, String> operatorMap = new HashMap<>();
        operatorMap.put("=", "=");
        operatorMap.put(">", ">");
        operatorMap.put("<", "<");

        StringBuilder sqlQuery = new StringBuilder("SELECT * FROM departments WHERE ");

        for (int i = 0; i < conditions.length; i++) {
            String condition = conditions[i].trim();
            String[] parts = condition.split("\\s+");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid condition: " + condition);
            }

            String field = parts[0];
            String operator = parts[1];
            String value = parts[2];

            if (!operatorMap.containsKey(operator)) {
                throw new IllegalArgumentException("Invalid operator: " + operator);
            }

            if (i > 0) {
                sqlQuery.append(" OR ");
            }

            sqlQuery.append(field).append(operatorMap.get(operator)).append("'").append(value).append("'");
        }

        return sqlQuery.toString().replace("+ ", " ");
    }
}
