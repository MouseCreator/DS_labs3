package org.example.controller;

import org.example.dao.DepartmentsDAO;
import org.example.filter.DepartmentParser;
import org.example.filter.FilterManager;
import org.example.filter.FilterManagerImpl;
import org.example.model.Department;
import org.example.service.DepartmentsService;

import java.util.List;
import java.util.NoSuchElementException;

public class SimpleDepartmentController implements DepartmentController{
    private final DepartmentsService departmentsService;
    private final FilterManager<Department> filterManager = new FilterManagerImpl<>(new DepartmentParser());
    public SimpleDepartmentController(DepartmentsService departmentsService) {
        this.departmentsService = departmentsService;
    }

    public void create(Department department) {
        department.setId(null);
        departmentsService.create(department);
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
    public void source(DepartmentsDAO dao) {
        departmentsService.changeSource(dao);
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
        if (!departmentsService.delete(ln)) {
            throw new NoSuchElementException("No department with id " + ln);
        }
    }

    public void update(Department department) {
        if (departmentsService.containsId(department.getId())) {
            departmentsService.update(department);
            return;
        }
        throw new NoSuchElementException("Cannot update department!");
    }
}
