package org.example.controller;

import org.example.dao.EmployeesDao;
import org.example.filter.EmployeeParser;
import org.example.filter.FilterManager;
import org.example.filter.FilterManagerImpl;
import org.example.model.Employee;
import org.example.service.EmployeesService;

import java.util.List;

public class SimpleEmployeeController implements EmployeesController {
    private final EmployeesService service;
    private final FilterManager<Employee> filterManager = new FilterManagerImpl<>(new EmployeeParser());
    public SimpleEmployeeController(EmployeesService service) {
        this.service = service;
    }

    @Override
    public List<Employee> getAllEmployeesOfDepartment(Long id) {
        return service.findEmployeesFromDepartment(id);
    }

    @Override
    public void create(Employee employee) {
        employee.setId(null);
        service.create(employee);
    }

    @Override
    public List<Employee> filter(String filterString) {
        return service.findFiltered(filterString);
    }

    @Override
    public Employee get(Long id) {
        return service.find(id);
    }

    @Override
    public void source(EmployeesDao employeesDatabaseDAO) {
        service.changeSource(employeesDatabaseDAO);
    }

    @Override
    public void update(Employee employee) {
        service.update(employee);
    }

    @Override
    public List<Employee> getAll() {
        return service.findAll();
    }

    @Override
    public List<Employee> filter(List<Employee> result, String fString) {
        return filterManager.filter(result, fString);
    }
    @Override
    public void remove(Long ln) {
        service.delete(ln);
    }

}
