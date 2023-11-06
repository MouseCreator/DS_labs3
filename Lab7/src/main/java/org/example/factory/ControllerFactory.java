package org.example.factory;

import org.example.controller.DepartmentController;
import org.example.controller.EmployeesController;

public interface ControllerFactory {
    DepartmentController initialDepartmentController();
    EmployeesController initialEmployeeController();
}
