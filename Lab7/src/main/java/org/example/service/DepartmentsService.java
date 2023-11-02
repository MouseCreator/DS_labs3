package org.example.service;

import org.example.model.Department;

import java.util.List;

public interface DepartmentsService {
    void create(Department department);
    List<Department> findAll();
    void delete(Long id);
    void update(Department department);
}
