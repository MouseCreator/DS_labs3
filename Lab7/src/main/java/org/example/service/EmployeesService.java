package org.example.service;

import org.example.dao.EmployeesDao;
import org.example.model.Employee;

import java.util.List;

public interface EmployeesService {
    void create(Employee employee);
    List<Employee> findAll();
    void delete(Long id);
    void update(Employee employee);
    boolean containsIgnoreId(Employee employee);
    boolean containsId(Long id);
    List<Employee> findEmployeesFromDepartment(Long id);
    List<Employee> findFiltered(String filter);
    void changeSource(EmployeesDao dao);
    Employee find(Long id);
}
