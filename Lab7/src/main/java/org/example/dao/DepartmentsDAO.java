package org.example.dao;

import org.example.model.Department;

import java.util.List;


public interface DepartmentsDAO extends GenericCrudDao<Department> {
    List<Department> findByFilter(String filterString);
}
