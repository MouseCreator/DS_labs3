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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void find() {
    }
}