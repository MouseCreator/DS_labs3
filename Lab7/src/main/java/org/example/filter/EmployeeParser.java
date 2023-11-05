package org.example.filter;

import org.example.model.Employee;

public class EmployeeParser extends PredicateParser<Employee> {
    @Override
    protected Object getExpectedValue(Employee object, String field) {
        return switch (field) {
            case "id" -> object.getId();
            case "age" -> object.getAge();
            case "name" -> object.getName();
            case "experience" -> object.getExperienceYears();
            case "role" -> object.getRole();
            case "department" -> object.getDepartmentId();
            default -> throw new IllegalArgumentException("Unknown field for employee: " + field);

        };
    }
}
