package org.example.factory;

import org.example.service.DepartmentsService;
import org.example.service.EmployeesService;

public interface ServiceFactory {
    DepartmentsService getDepartmentService();
    EmployeesService getEmployeesService();
}
