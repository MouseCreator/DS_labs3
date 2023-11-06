package org.example.service;

import org.example.model.Employee;

import java.util.List;

public class EmployeesServiceImpl implements EmployeesService{
    @Override
    public void create(Employee department) {

    }

    @Override
    public List<Employee> findAll() {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Employee department) {

    }

    @Override
    public boolean containsIgnoreId(Employee department) {
        return false;
    }

    @Override
    public boolean containsId(Long id) {
        return false;
    }

    @Override
    public List<Employee> findEmployeesFromDepartment(Long id) {
        return null;
    }

    @Override
    public List<Employee> findFiltered(String filter) {
        return null;
    }
}
