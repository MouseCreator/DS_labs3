package org.example.controller;

import org.example.dao.DepartmentsDAO;
import org.example.model.Department;

public interface DepartmentController extends ModelController<Department> {
    void source(DepartmentsDAO dao);
}
