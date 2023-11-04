package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
import org.example.manager.FileManager;
import org.example.manager.FileManagerImpl;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.parser.Parser;
import org.example.extra.Paths;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCrudDaoTest {
    private static final String TEST_XML = Paths.TEST_DEPARTMENTS_TEMP;
    private static final String ORIGIN_XML = Paths.TEST_DEPARTMENTS_XML;
    private Parser<Departments> parser;
    private Writer<Departments> writer;
    private FileManager fileManager;
    private DepartmentsDAO departmentsDAO;

    private void refresh() {
        fileManager.copyXML(ORIGIN_XML, TEST_XML);
    }

    @BeforeEach
    void setUp() {
        parser = new DepartmentStaxParser();
        writer = new DepartmentsWriter();
        fileManager = new FileManagerImpl();
        refresh();
        departmentsDAO = createDepartmentsDao();
    }
    private XMLDepartmentsDao createDepartmentsDao() {
        if (writer==null) {
            throw new IllegalStateException("Writer is not initialized");
        }
        if (parser==null){
            throw new IllegalStateException("Parser is not initialized");
        }
        return new XMLDepartmentsDao(TEST_XML, parser, writer);
    }

    @Test
    void findAll() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        Departments departments = testDataGenerator.allDepartments();
        List<Department> expectedList = departments.getDepartmentList();
        List<Department> actualList = departmentsDAO.findAll();
        TestHelper.compareList(expectedList, actualList);
    }

    @Test
    void create() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        Departments departments = testDataGenerator.allDepartments();
        List<Department> expectedList = departments.getDepartmentList();

        expectedList.add(newDepartmentWithId());
        Department updatedDepartment = departmentsDAO.create(newDepartment());
        assertEquals(4L, updatedDepartment.getId());

        List<Department> updatedList = departmentsDAO.findAll();
        TestHelper.compareList(expectedList, updatedList);
    }

    private Department newDepartment() {
        Department department = new Department();
        department.setName("Fashion");
        return department;
    }
    private Department newDepartmentWithId() {
        Department department = new Department();
        department.setName("Fashion");
        return department;
    }

    @Test
    void update() {
        Department updated = newDepartment();
        updated.setId(1L);
        departmentsDAO.update(updated);
        Optional<Department> department = departmentsDAO.find(1L);
        assertTrue(department.isPresent());
        assertEquals(updated, department.get());
    }

    @Test
    void delete() {
        Optional<Department> departmentOptional = departmentsDAO.find(1L);
        int initialSize = departmentsDAO.findAll().size();
        assertTrue(departmentOptional.isPresent());
        Department department = departmentOptional.get();
        departmentsDAO.delete(department);
        assertThrows(NoSuchElementException.class, () -> departmentsDAO.delete(department));
        assertTrue(departmentsDAO.find(1L).isEmpty());
        int finalSize = departmentsDAO.findAll().size();
        assertEquals(initialSize-1, finalSize);
    }

    @Test
    void testDelete() {
        Optional<Department> departmentOptional = departmentsDAO.find(1L);
        int initialSize = departmentsDAO.findAll().size();
        assertTrue(departmentOptional.isPresent());
        assertTrue(departmentsDAO.delete(1L));
        assertFalse(() -> departmentsDAO.delete(1L));
        assertTrue(departmentsDAO.find(1L).isEmpty());
        int finalSize = departmentsDAO.findAll().size();
        assertEquals(initialSize-1, finalSize);
    }

    @Test
    void find() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        List<Department> departmentList = testDataGenerator.allDepartments().getDepartmentList();
        long size = departmentList.size();
        for (int i = 1; i <= size; i++) {
            Optional<Department> departmentOptional = departmentsDAO.find((long) i);
            assertTrue(departmentOptional.isPresent());
            assertEquals(departmentOptional.get(), departmentList.get(i-1));
        }
        assertTrue(departmentsDAO.find(size+1).isEmpty());
    }
}