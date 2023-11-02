package org.example.service;

import org.example.model.Department;
import org.example.model.Departments;

import java.util.List;
import java.util.Objects;

public class XMLDepartmentsService implements DepartmentsService{
    private final Departments departments;
    public XMLDepartmentsService(Departments departments) {
        this.departments = departments;
    }

    @Override
    public void create(Department department) {
        departments.getDepartmentList().add(department);
    }

    @Override
    public List<Department> findAll() {
        return departments.getDepartmentList();
    }

    @Override
    public void delete(Long id) {
        departments.getDepartmentList().removeIf(d -> Objects.equals(d.getId(), id));
    }

    @Override
    public void update(Department department) {
        for (int i = 0; i < departments.getDepartmentList().size(); i++) {
            if (departments.getDepartmentList().get(i).getId().equals(department.getId())) {
                departments.getDepartmentList().set(i, department);
                return;
            }
        }
    }
}
