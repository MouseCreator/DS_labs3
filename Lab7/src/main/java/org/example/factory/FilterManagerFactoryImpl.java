package org.example.factory;

import org.example.filter.*;
import org.example.model.Department;
import org.example.model.Employee;

import java.util.function.Predicate;

public class FilterManagerFactoryImpl implements FilterManagerFactory {
    @Override
    public FilterManager<Department> getForDepartment() {
        FilterFactory<Predicate<Department>> filterFactory = new DepartmentParser();
        return new FilterManagerImpl<>(filterFactory);
    }

    @Override
    public FilterManager<Employee> getForEmployee() {
        FilterFactory<Predicate<Employee>> filterFactory = new EmployeeParser();
        return new FilterManagerImpl<>(filterFactory);
    }
}
