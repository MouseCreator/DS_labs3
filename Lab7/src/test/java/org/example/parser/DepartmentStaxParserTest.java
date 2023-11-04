package org.example.parser;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.paths.Paths;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentStaxParserTest {
    private static final String TEST_XML = Paths.TEST_DEPARTMENTS_XML;

    private Department generateTestDepartment() {
        Department department1 = new Department();
        department1.setId(1L);
        department1.setName("Info");
        return department1;
    }
    private List<Employee> generateTestEmployees() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Ivanov");
        employee1.setAge(20);
        employee1.setRole("Copywriter");
        employee1.setExperienceYears(2);
        employee1.setDepartmentId(1L);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Petrov");
        employee2.setAge(22);
        employee2.setRole("Copywriter");
        employee2.setExperienceYears(1);
        employee2.setDepartmentId(1L);

        Employee employee3 = new Employee();
        employee3.setId(3L);
        employee3.setName("Lemon");
        employee3.setAge(40);
        employee3.setRole("PR Manager");
        employee3.setExperienceYears(12);
        employee3.setDepartmentId(1L);

        Employee employee4 = new Employee();
        employee4.setId(4L);
        employee4.setName("Ivanov");
        employee4.setAge(35);
        employee4.setRole("Copywriter");
        employee4.setExperienceYears(5);
        employee4.setDepartmentId(1L);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);
        employeeList.add(employee4);
        return employeeList;
    }
    @Test
    void parse() {
        Parser<Departments> parser = new DepartmentStaxParser();
        Departments departments = parser.parse(TEST_XML);
        assertEquals(3, departments.getDepartmentList().size());
        Department actualDepartment = departments.getDepartmentList().get(0);
        Department expectedDepartment = generateTestDepartment();
        List<Employee> employeeList = departments.getEmployeeList().stream().filter(employee -> employee.getDepartmentId()==1).toList();
        List<Employee> expectedEmployees = generateTestEmployees();

        assertEquals(expectedDepartment, actualDepartment);
        assertEquals(employeeList.size(), expectedEmployees.size());
        for (int i = 0; i <employeeList.size(); ++i) {
            assertEquals(expectedEmployees.get(i), employeeList.get(i), "Employees are not equal: " + i);
        }
    }
}