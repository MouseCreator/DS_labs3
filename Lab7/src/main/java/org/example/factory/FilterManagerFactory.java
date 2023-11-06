package org.example.factory;

import org.example.filter.FilterManager;
import org.example.model.Department;
import org.example.model.Employee;

public interface FilterManagerFactory {
    FilterManager<Department> getForDepartment();
    FilterManager<Employee> getForEmployee();
}
