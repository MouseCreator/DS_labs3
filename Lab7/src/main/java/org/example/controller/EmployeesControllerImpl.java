package org.example.controller;

import org.example.model.Employee;
import org.example.service.EmployeesService;

import java.util.List;

public class EmployeesControllerImpl implements EmployeesController {

    private final EmployeesService service;
    private final CommonController controller;

    public EmployeesControllerImpl(EmployeesService service, CommonController controller) {
        this.service = service;
        this.controller = controller;
    }

    @Override
    public List<Employee> getAllEmployeesOfDepartment(Long id) {
        return null;
    }
}
