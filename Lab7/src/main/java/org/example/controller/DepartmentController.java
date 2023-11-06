package org.example.controller;

import org.example.model.Department;

import java.util.List;

public interface DepartmentController {
    List<Department> getAll();
    List<Department> filter(List<Department> result, String fString);
    void remove(Long ln);
    void update(Department department);
    void create(Department department);
    List<Department> filter(String filterString);
    Department get(Long id);
}
