package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
import org.example.manager.FileManagerImpl;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.writer.DepartmentsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class XMLDepartmentsDaoTest extends AbstractCrudDaoTest<Departments, Department> {

    private DepartmentsDAO crudDao;
    private XMLDepartmentsDao createDepartmentsDao() {
        if (writer==null) {
            throw new IllegalStateException("Writer is not initialized");
        }
        if (parser==null){
            throw new IllegalStateException("Parser is not initialized");
        }
        return new XMLDepartmentsDao(TEST_XML, parser, writer);
    }

    @BeforeEach
    void setUp() {
        parser = new DepartmentStaxParser();
        writer = new DepartmentsWriter();
        fileManager = new FileManagerImpl();
        crudDao = createDepartmentsDao();
        initializeAndRefresh(parser, writer, fileManager, crudDao);
    }

    @Override
    protected Department newInstance() {
        Department department = new Department();
        department.setId(4L);
        department.setName("Fashion");
        return department;
    }
    @Override
    protected List<Department> expectedData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        Departments departments = testDataGenerator.allDepartments();
        return departments.getDepartmentList();
    }

    @Test
    void findAll() {
        super.findAll();
    }

    @Test
    void create() {
        super.create();
    }


    @Test
    void update() {
        super.update();
    }

    @Test
    void delete() {
        super.delete();
    }

    @Test
    void testDelete() {
        super.testDelete();
    }

    @Test
    void find() {
        super.find();
    }

    @Test
    void filter() {
        String filterString = "name = 'Info' | name = 'Marketing'";
        List<Department> expected = expectedData().stream().filter(d -> d.getName().equals("Info") || d.getName().equals("Marketing")).toList();
        List<Department> actual = crudDao.findByFilter(filterString);
        TestHelper.compareList(expected, actual);
    }
}