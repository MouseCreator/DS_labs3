package org.example.controller;

import org.example.model.Department;
import org.example.service.DepartmentsService;

public class DepartmentControllerImpl {

    private CommonController controller;
    private DepartmentsService departmentsService;
    void create(Department department) {
        if(departmentsService.containsIgnoreId(department)) {
            boolean answer = controller.askBoolean("This department already exists. Do you want to create another one?");
            if (answer) {
                departmentsService.create(department);
            } else {
                return;
            }
        }
        controller.print("Department was created successfully!");
    }
}
