package org.example.service;

import org.example.dao.DepartmentsDAO;
import org.example.model.Department;

import java.util.List;

public class DepartmentsServiceImpl implements DepartmentsService{
    private DepartmentsDAO departmentsDAO;
    public DepartmentsServiceImpl(DepartmentsDAO departmentsDAO) {
        this.departmentsDAO = departmentsDAO;
    }

    public void changeSource(DepartmentsDAO dao) {
        this.departmentsDAO = dao;
    }
    @Override
    public void create(Department department) {
        departmentsDAO.create(department);
    }

    @Override
    public List<Department> findAll() {
        return departmentsDAO.findAll();
    }

    @Override
    public void delete(Long id) {
        departmentsDAO.delete(id);
    }

    @Override
    public void update(Department department) {
        departmentsDAO.update(department);
    }

    @Override
    public boolean containsIgnoreId(Department department) {
        List<Department> departments = departmentsDAO.findAll();
        Long prevId = department.getId();
        try {
            for (Department current : departments) {
                department.setId(current.getId());
                if (department.equals(current)) {
                    return true;
                }
            }
            return false;
        } finally {
            department.setId(prevId);
        }
    }

    @Override
    public boolean containsId(Long id) {
        if (id == null)
            return false;
        return departmentsDAO.find(id).isPresent();
    }
}
