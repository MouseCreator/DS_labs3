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
        Employee employee1 = new Employee();
        employee1.setRole("Editor");
        employee1.setAge(22);
        assertFalse(predicate.test(employee1));
        employee1.setRole("Editor2");
        assertTrue(predicate.test(employee1));
        employee1.setAge(10);
        assertFalse(predicate.test(employee1));
    }
}