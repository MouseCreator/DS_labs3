package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
import org.example.manager.FileManagerImpl;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.parser.DepartmentStaxParser;
import org.example.writer.DepartmentsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class XMLEmployeesDaoTest extends AbstractCrudDaoTest<Departments, Employee> {

    private EmployeesDao crudDao;
    private XMLEmployeesDao createEmployeesDao() {
        if (writer==null) {
            throw new IllegalStateException("Writer is not initialized");
        }
        if (parser==null){
            throw new IllegalStateException("Parser is not initialized");
        }
        return new XMLEmployeesDao(TEST_XML, parser, writer);
    }

    @BeforeEach
    void setUp() {
        parser = new DepartmentStaxParser();
        writer = new DepartmentsWriter();
        fileManager = new FileManagerImpl();
        crudDao = createEmployeesDao();
        initializeAndRefresh(parser, writer, fileManager, crudDao);
    }

    @Override
    protected Employee newInstance() {
        Employee employee = new Employee();
        employee.setId(12L);
        employee.setName("Peter");
        employee.setRole("Creator");
        employee.setDepartmentId(1L);
        employee.setAge(22);
        employee.setExperienceYears(2);
        return employee;
    }
    @Override
    protected List<Employee> expectedData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        Departments departments = testDataGenerator.allDepartments();
        return departments.getEmployeeList();
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
    void findAllEmployeesOfDepartment() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        List<Department> departmentList = testDataGenerator.allDepartments().getDepartmentList();
        List<Employee> employeeList = testDataGenerator.allDepartments().getEmployeeList();
        int j = 0;
        for (long i = 1L; i <= 3L; i++) {
            final Long s = i;
            List<Employee> expected = employeeList.stream().filter(e -> e.getDepartmentId().equals(s)).toList();
            List<Employee> allEmployeesOfDepartment = crudDao.findAllEmployeesOfDepartment(departmentList.get(j++));
            TestHelper.compareList(expected, allEmployeesOfDepartment);
        }
    }
    @Test
    void findAllEmployeesOfDepartmentId() {
        List<Employee> employeeList = expectedData();
        for (long i = 1L; i <= 3L; i++) {
            final Long s = i;
            List<Employee> expected = employeeList.stream().filter(e -> e.getDepartmentId().equals(s)).toList();
            List<Employee> allEmployeesOfDepartment = crudDao.findAllEmployeesOfDepartment(i);
            TestHelper.compareList(expected, allEmployeesOfDepartment);
        }
    }

    @Test
    void filter() {
        List<Employee> employeeList = expectedData();
        List<Employee> filtered = employeeList.stream().filter(e -> e.getAge() > 20 && e.getAge() < 40).toList();
        String filter1 = "age > 20 & age < 40";
        List<Employee> byFilter1 = crudDao.findByFilter(filter1);
        TestHelper.compareList(filtered, byFilter1);
    }
}