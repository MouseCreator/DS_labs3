package org.example.service;

import org.example.dao.DepartmentsDAO;
import org.example.model.Department;

import java.util.List;

public interface DepartmentsService {
    void create(Department department);
    List<Department> findAll();
    boolean delete(Long id);
    void update(Department department);
    boolean containsIgnoreId(Department department);
    boolean containsId(Long id);
    void changeSource(DepartmentsDAO dao);
    List<Department> findFiltered(String filter);
    Department get(Long id);
}
