package org.example.controller;

import org.example.filter.DepartmentParser;
import org.example.filter.FilterManager;
import org.example.filter.FilterManagerImpl;
import org.example.model.Department;
import org.example.service.DepartmentsService;

import java.util.List;

public class DepartmentControllerImpl implements DepartmentController {
    private final CommonController controller;
    private final DepartmentsService departmentsService;
    private final FilterManager<Department> filterManager = new FilterManagerImpl<>(new DepartmentParser());
    public DepartmentControllerImpl(CommonController controller, DepartmentsService departmentsService) {
        this.controller = controller;
        this.departmentsService = departmentsService;
    }

    public void create(Department department) {
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

    @Override
    public List<Department> filter(String filterString) {
        return departmentsService.findFiltered(filterString);
    }

    @Override
    public Department get(Long id) {
        return departmentsService.get(id);
    }

    @Override
    public List<Department> getAll() {
        return departmentsService.findAll();
    }

    @Override
    public List<Department> filter(List<Department> result, String fString) {
        return filterManager.filter(result, fString);
    }

    @Override
    public void remove(Long ln) {
        if (departmentsService.delete(ln)) {
            controller.print("Removed " + ln + " successfully!");
        } else {
            controller.print("No element with id " + ln + " found!");
        }
    }

    public void update(Department department) {
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
