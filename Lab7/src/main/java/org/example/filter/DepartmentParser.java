package org.example.filter;

import org.example.model.Department;
public class DepartmentParser extends PredicateParser<Department> {
    @Override
    protected Object getExpectedValue(Department object, String field) {
        return switch (field) {
            case "id" -> object.getId();
            case "name" -> object.getName();
            default -> throw new IllegalArgumentException("Unknown field for department: " + field);
        };
    }
}
