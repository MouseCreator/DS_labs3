package org.example.service;

import org.example.dao.DepartmentsDAO;
import org.example.extra.TestDataGenerator;
import org.example.model.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentsServiceImplTest {
    private DepartmentsService service;
    private TestDataGenerator generator;
    @BeforeEach
    void beforeEach() {
        DepartmentsDAO departmentDAO = Mockito.mock(DepartmentsDAO.class);
        generator = new TestDataGenerator();
        Mockito.when(departmentDAO.findAll()).thenReturn(generator.allDepartments().getDepartmentList());
        Department department = new Department();
        Mockito.when(departmentDAO.find(1L)).thenReturn(Optional.of(department));
        Mockito.when(departmentDAO.find(5L)).thenReturn(Optional.empty());
        service = new DepartmentsServiceImpl(departmentDAO);
    }
    @Test
    void containsIgnoreId() {
        int s = generator.allDepartments().getEmployeeList().size();
        List<Department> dList = generator.allDepartments().getDepartmentList();
        assert s > 1;
        Department department = new Department();
        department.setName("SOMEONE");
        department.setId(1L);
        assertTrue(service.containsIgnoreId(dList.get(1)));
        assertTrue(service.containsIgnoreId(dList.get(2)));
        assertFalse(service.containsIgnoreId(department));
    }

    @Test
    void containsId() {
        assertTrue(service.containsId(1L));
        assertFalse(service.containsId(5L));
    }
}