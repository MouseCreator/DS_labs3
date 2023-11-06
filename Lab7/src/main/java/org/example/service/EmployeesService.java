package org.example.service;

import org.example.model.Employee;

import java.util.List;

public interface EmployeesService {
    void create(Employee department);
    List<Employee> findAll();
    void delete(Long id);
    void update(Employee department);
    boolean containsIgnoreId(Employee department);
    boolean containsId(Long id);
    List<Employee> findEmployeesFromDepartment(Long id);
    List<Employee> findFiltered(String filter);
}
