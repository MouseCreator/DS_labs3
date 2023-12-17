package univ.lab.lab9_boot.repository;

import univ.lab.lab9_boot.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    Employee save(Employee employee);
    List<Employee> findAll();
}
