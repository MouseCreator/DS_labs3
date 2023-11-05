package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
import org.example.model.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    private List<Department> withTestData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        List<Department> departmentList = testDataGenerator.allDepartments().getDepartmentList();
        for (Department department : departmentList) {
            dao.create(department);
        }
        return departmentList;
    }
    @Test
    void findAll() {
        List<Department> testData = withTestData();
        List<Department> allDepartments = dao.findAll();
        TestHelper.compareList(testData, allDepartments);
    }

    @Test
    void findById() {
        List<Department> testData = withTestData();
        int j = 0;
        for (long i = 1L; i <= 3L; i++) {
            Department expected = testData.get(j++);
            Optional<Department> actual = dao.find(i);
            assertTrue(actual.isPresent());
            assertEquals(expected, actual.get());
        }
        assertTrue(dao.find(4L).isEmpty());
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