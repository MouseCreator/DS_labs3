package org.example.controller;

import org.example.model.Employee;

import java.util.List;

public interface EmployeesController {
    List<Employee> getAllEmployeesOfDepartment(Long id);
}
