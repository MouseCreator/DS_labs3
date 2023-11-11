package org.example.service;

import org.example.dao.EmployeesDao;
import org.example.model.Employee;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EmployeesServiceImpl implements EmployeesService{

    private EmployeesDao employeesDao;

    public EmployeesServiceImpl(EmployeesDao employeesDao) {
        this.employeesDao = employeesDao;
    }

    @Override
    public void create(Employee employee) {
        employeesDao.create(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeesDao.findAll();
    }

    @Override
    public void delete(Long id) {
        employeesDao.delete(id);
    }

    @Override
    public void update(Employee employee) {
        employeesDao.update(employee);
    }

    @Override
    public boolean containsIgnoreId(Employee employee) {
        List<Employee> employees = employeesDao.findAll();
        Long prevId = employee.getId();
        try {
            for (Employee current : employees) {
                employee.setId(current.getId());
                if (employee.equals(current)) {
                    return true;
                }
            }
            return false;
        } finally {
            employee.setId(prevId);
        }
    }

    @Override
    public boolean containsId(Long id) {
        if (id == null)
            return false;
        return employeesDao.find(id).isPresent();
    }

    @Override
    public List<Employee> findEmployeesFromDepartment(Long id) {
        return employeesDao.findAllEmployeesOfDepartment(id);
    }

    @Override
    public List<Employee> findFiltered(String filter) {
        return employeesDao.findByFilter(filter);
    }

    @Override
    public void changeSource(EmployeesDao dao) {
        this.employeesDao = dao;
    }

    @Override
    public Employee find(Long id) {
        Optional<Employee> employee = employeesDao.find(id);
        if (employee.isPresent())
            return employee.get();
        throw new NoSuchElementException("Cannot find employee with id " + id);
    }
}
