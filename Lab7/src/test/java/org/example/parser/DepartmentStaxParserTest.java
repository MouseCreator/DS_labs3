package org.example.parser;

import org.example.extra.TestDataGenerator;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.extra.Paths;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentStaxParserTest {
    private static final String TEST_XML = Paths.TEST_DEPARTMENTS_XML;
    @Test
    void parse() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        Parser<Departments> parser = new DepartmentStaxParser();
        Departments departments = parser.parse(TEST_XML);
        assertEquals(3, departments.getDepartmentList().size());
        List<Department> actualDepartments = departments.getDepartmentList();
        Departments departmentsObj = testDataGenerator.allDepartments();
        List<Department> expectedDepartments = departmentsObj.getDepartmentList();
        List<Employee> employeeList = departments.getEmployeeList();
        List<Employee> expectedEmployees = departmentsObj.getEmployeeList();

        assertEquals(expectedDepartments.size(), actualDepartments.size());
        for (int i = 0; i < expectedDepartments.size(); i++) {
            assertEquals(expectedDepartments.get(i), actualDepartments.get(i));
        }
        assertEquals(employeeList.size(), expectedEmployees.size());
        for (int i = 0; i <employeeList.size(); ++i) {
            assertEquals(expectedEmployees.get(i), employeeList.get(i), "Employees are not equal: " + i);
        }
    }
}