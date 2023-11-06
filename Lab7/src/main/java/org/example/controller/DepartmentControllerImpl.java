package org.example.controller;

import org.example.model.Department;
import org.example.service.DepartmentsService;

public class DepartmentControllerImpl {
    private final CommonController controller;
    private final DepartmentsService departmentsService;

    public DepartmentControllerImpl(CommonController controller, DepartmentsService departmentsService) {
        this.controller = controller;
        this.departmentsService = departmentsService;
    }

    void create(Department department) {
        if (departmentsService.containsId(department.getId())) {
            boolean answer = controller.askBoolean("Department with this ID is already in the database! Do you want to override it?");
            if (answer) {
                update(department);
            } else {
                answer = controller.askBoolean("Create a new one with next ID?");
                if (answer) {
                    department.setId(null);
                } else {
                    return;
                }
            }
        }

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

    void update(Department department) {
        if (departmentsService.containsId(department.getId())) {
            departmentsService.update(department);
            return;
        }
        boolean answer = controller.askBoolean("No department with this id was found! Do you want it to be created?");
        if (answer) {
            departmentsService.create(department);
        }
    }
}
