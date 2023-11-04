package org.example.dao;

import org.example.model.Department;
import org.example.model.Employee;

import java.util.List;

public interface EmployeesDao extends GenericCrudDao<Employee> {
    List<Employee> findAllEmployeesOfDepartment(Department department);

    List<Employee> findAllEmployeesOfDepartment(Long id);
}
