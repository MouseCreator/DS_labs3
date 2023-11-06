package org.example.dao;

import org.example.extra.TestDataGenerator;
import org.example.extra.TestHelper;
import org.example.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DBEmployeesDaoTest {
    private InMemoryDatabaseConnectionProvider provider;
    private DBEmployeesDao dao;
    @BeforeEach
    void setUp() {
        provider = new InMemoryDatabaseConnectionProvider();
        provider.createDB();
        dao = new DBEmployeesDao(provider);
    }

    @AfterEach
    void tearDown(){
        try {
            provider.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Employee> withTestData() {
        TestDataGenerator testDataGenerator = new TestDataGenerator();
        List<Employee> employeeList = testDataGenerator.allDepartments().getEmployeeList();
        for (Employee employee : employeeList) {
            dao.create(employee);
        }
        return employeeList;
    }
    @Test
    void findAllEmployeesOfDepartment() {
        List<Employee> allEmployees = withTestData();
        List<Employee> depEmployees = allEmployees.stream().filter(e -> e.getDepartmentId() == 1L).toList();
        List<Employee> allEmployeesOfDepartment = dao.findAllEmployeesOfDepartment(1L);
        TestHelper.compareList(depEmployees, allEmployeesOfDepartment);
    }

    @Test
    void findAll() {
        List<Employee> testData = withTestData();
        List<Employee> allDepartments = dao.findAll();
        TestHelper.compareList(testData, allDepartments);
    }

    @Test
    void findById() {
        List<Employee> testData = withTestData();
        int j = 0;
        for (long i = 1L; i <= testData.size(); i++) {
            Employee expected = testData.get(j++);
            Optional<Employee> actual = dao.find(i);
            assertTrue(actual.isPresent());
            assertEquals(expected, actual.get());
        }
        assertTrue(dao.find(testData.size()+1L).isEmpty());
    }

    @Test
    void create() {
        TestDataGenerator generator = new TestDataGenerator();
        Employee employee = generator.allDepartments().getEmployeeList().get(0);
        dao.create(employee);
        List<Employee> all = dao.findAll();
        assertEquals(1, all.size());
        employee.setId(1L);
        assertEquals(employee, all.get(0));
    }

    @Test
    void update() {
        List<Employee> employeeList = withTestData();

        Employee employee = employeeList.get(0);
        employee.setName("Rudolf");
        dao.update(employee);
        Optional<Employee> departmentOpt = dao.find(1L);
        assertTrue(departmentOpt.isPresent());
        assertEquals("Rudolf", departmentOpt.get().getName());
    }

    @Test
    void delete() {
        withTestData();
        Optional<Employee> d = dao.find(2L);
        assertTrue(d.isPresent());
        assertTrue(dao.delete(2L));
        deletionConfirm(d.get());
    }

    @Test
    void deleteObj() {
        withTestData();
        Optional<Employee> d = dao.find(2L);
        assertTrue(d.isPresent());
        assertDoesNotThrow(() -> dao.delete(d.get()));
        deletionConfirm(d.get());
    }

    private void deletionConfirm(Employee employee) {
        List<Employee> all = dao.findAll();
        assertEquals(10, all.size());
        assertFalse(all.contains(employee));
    }

    @Test
    void filter() {
        List<Employee> employeeList = withTestData();
        List<Employee> filtered = employeeList.stream().filter(e -> e.getAge() > 20 && e.getAge() < 40).toList();
        String filter1 = "age > 20 & age < 40";
        List<Employee> byFilter1 = dao.findByFilter(filter1);
        TestHelper.compareList(filtered, byFilter1);
    }
}