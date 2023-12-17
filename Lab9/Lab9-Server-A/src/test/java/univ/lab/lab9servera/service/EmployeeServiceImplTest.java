package univ.lab.lab9servera.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import univ.lab.lab9servera.model.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    void testSaveAndFindAll() {
        Employee employee = new Employee();
        employee.setName("John");
        employee.setAge(30);
        employee.setRole("Developer");
        employee.setExperienceYears(5);

        employeeService.save(employee);
        List<Employee> employees = employeeService.findAll();

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getName());
    }
}