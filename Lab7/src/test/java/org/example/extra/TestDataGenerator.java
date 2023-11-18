package org.example.extra;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class TestDataGenerator {
    private List<Department> generateAllDepartments() {
        List<Department> departments = new ArrayList<>();

        departments.add(createDepartment(1L, "Info"));
        departments.add(createDepartment(2L, "Marketing"));
        departments.add(createDepartment(3L, "Sales"));

        return departments;
    }

    private Department createDepartment(long id, String name) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        return department;
    }

    private List<Employee> generateAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        employees.add(createEmployee(1L, "Ivanov", 20, "Copywriter", 2, 1L));
        employees.add(createEmployee(2L, "Petrov", 22, "Copywriter", 1, 1L));
        employees.add(createEmployee(3L, "Lemon", 40, "PR Manager", 12, 1L));
        employees.add(createEmployee(4L, "Ivanov", 35, "Copywriter", 5, 1L));

        employees.add(createEmployee(5L, "Smith", 28, "Marketing Manager", 7, 2L));
        employees.add(createEmployee(6L, "Johnson", 25, "Marketing Specialist", 3, 2L));
        employees.add(createEmployee(7L, "Brock", 25, "Marketing Specialist", 3, 2L));
        employees.add(createEmployee(8L, "Lit", 60, "Marketing Specialist", 40, 2L));

        employees.add(createEmployee(9L, "Brown", 30, "Sales Manager", 8, 3L));
        employees.add(createEmployee(10L, "Davis", 26, "Sales Representative", 4, 3L));
        employees.add(createEmployee(11L, "Stevenson", 44, "Sales Manager", 11, 3L));

        return employees;
    }

    private Employee createEmployee(long id, String name, int age, String role, int experienceYears, long departmentId) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setAge(age);
        employee.setRole(role);
        employee.setExperienceYears(experienceYears);
        employee.setDepartmentId(departmentId);
        return employee;
    }
    public Departments allDepartments() {
        Departments departments = new Departments();
        departments.setDepartmentList(generateAllDepartments());
        departments.setEmployeeList(generateAllEmployees());
        return departments;
    }
    public static TestDataGenerator get() {
        return new TestDataGenerator();
    }
}
