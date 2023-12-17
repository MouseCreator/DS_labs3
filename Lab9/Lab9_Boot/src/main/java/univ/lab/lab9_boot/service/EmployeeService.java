package univ.lab.lab9_boot.service;

import univ.lab.lab9_boot.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee employee);
    List<Employee> findAll();
}
