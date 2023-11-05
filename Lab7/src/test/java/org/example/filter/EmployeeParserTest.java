package org.example.filter;

import org.example.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeParserTest {
    @Test
    void parsePredicate() {
        EmployeeParser employeeParser = new EmployeeParser();
        String expression = "role <> 'Editor' & age > 20";
        Predicate<Employee> predicate = employeeParser.parse(expression);

        Employee employee1 = sampleEditor();
        assertFalse(predicate.test(employee1));

        employee1.setRole("Editor2");
        assertTrue(predicate.test(employee1));

        employee1.setAge(10);
        assertFalse(predicate.test(employee1));
    }

    @Test
    void parsePredicateThrows() {
        EmployeeParser employeeParser = new EmployeeParser();
        Employee employee = sampleEditor();
        assertThrows(IllegalArgumentException.class,
                () -> employeeParser.parse("role <> 'Editor' - age > 20").test(employee));
        assertThrows(IllegalArgumentException.class,
                () -> employeeParser.parse("role > 'Editor' & age > 20").test(employee));


    }

    private static Employee sampleEditor() {
        Employee employee = new Employee();
        employee.setRole("Editor");
        employee.setAge(22);
        return employee;
    }
}