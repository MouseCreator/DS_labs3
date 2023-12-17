package univ.lab.lab9servera.service;

import univ.lab.lab9servera.model.Employee;

import java.util.List;


public interface EmployeeService {
    List<Employee> findAll();
    Employee save(Employee employee);
}
