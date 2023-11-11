package org.example.controller;

import org.example.dao.EmployeesDao;
import org.example.model.Employee;

import java.util.List;

public interface EmployeesController extends ModelController<Employee> {
    List<Employee> getAllEmployeesOfDepartment(Long id);
    void source(EmployeesDao employeesDatabaseDAO);
}
