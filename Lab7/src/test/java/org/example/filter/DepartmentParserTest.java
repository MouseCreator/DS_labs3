package org.example.filter;

import org.example.model.Department;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentParserTest {

    @Test
    void parsePredicate() {
        DepartmentParser departmentParser = new DepartmentParser();
        String expression = "id = 3 & name = 'Info'";
        Predicate<Department> predicate = departmentParser.parse(expression);

        Department department = new Department();
        department.setId(3L);
        department.setName("Art");
        assertFalse(predicate.test(department));

        department.setName("Info");
        assertTrue(predicate.test(department));

        department.setId(2L);
        assertFalse(predicate.test(department));
    }

}