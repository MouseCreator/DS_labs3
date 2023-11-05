package org.example.dao;

import org.example.model.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBDepartmentsDAOTest {

    private InMemoryDatabaseConnectionProvider provider;
    private DBDepartmentsDAO dao;
    @BeforeEach
    void setUp() {
        provider = new InMemoryDatabaseConnectionProvider();
        provider.createDB();
        dao = new DBDepartmentsDAO(provider);
    }

    @AfterEach
    void tearDown(){
        try {
            provider.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAll() {

    }

    @Test
    void create() {
        Department department = new Department();
        department.setName("Info");
        dao.create(department);
        List<Department> all = dao.findAll();
        assertEquals(1, all.size());
        department.setId(1L);
        assertEquals(department, all.get(0));
        System.out.println();
    }
}