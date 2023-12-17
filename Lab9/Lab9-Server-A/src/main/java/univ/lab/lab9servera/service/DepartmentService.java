package univ.lab.lab9servera.service;

import univ.lab.lab9servera.model.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    Department save(Department department);
}
