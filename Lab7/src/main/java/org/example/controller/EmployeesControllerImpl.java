package org.example.controller;

import org.example.dao.EmployeesDao;
import org.example.filter.EmployeeParser;
import org.example.filter.FilterManager;
import org.example.filter.FilterManagerImpl;
import org.example.model.Employee;
import org.example.service.EmployeesService;

import java.util.List;

public class EmployeesControllerImpl implements EmployeesController {

    private final EmployeesService service;
    private final CommonController controller;
    private final FilterManager<Employee> filterManager = new FilterManagerImpl<>(new EmployeeParser());

    public EmployeesControllerImpl(EmployeesService service, CommonController controller) {
        this.service = service;
        this.controller = controller;
    }

    @Override
    public List<Employee> getAllEmployeesOfDepartment(Long id) {
        return service.findEmployeesFromDepartment(id);
    }

    @Override
    public void create(Employee employee) {
        if (service.containsId(employee.getId())) {
            boolean answer = controller.askBoolean("Employee with this ID is already in the database! Do you want to override it?");
            if (answer) {
                update(employee);
            } else {
                answer = controller.askBoolean("Create a new one with next ID?");
                if (answer) {
                    employee.setId(null);
                } else {
                    return;
                }
            }
        }

        if(service.containsIgnoreId(employee)) {
            boolean answer = controller.askBoolean("This employee already exists. Do you want to create another one?");
            if (answer) {
                service.create(employee);
            } else {
                return;
            }
        }
        controller.print("Employee was created successfully!");
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
