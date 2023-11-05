package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
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
    private void withTestData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        List<Department> departmentList = testDataGenerator.allDepartments().getDepartmentList();
        for (Department department : departmentList) {
            dao.create(department);
        }
    }

    private List<Department> getTestData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        return testDataGenerator.allDepartments().getDepartmentList();
    }
    @Test
    void findAll() {
        withTestData();
        List<Department> testData = getTestData();
        List<Department> allDepartments = dao.findAll();
        TestHelper.compareList(testData, allDepartments);
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