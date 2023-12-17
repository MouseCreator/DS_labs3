package univ.lab.lab9servera.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import univ.lab.lab9servera.dto.*;
import univ.lab.lab9servera.exception.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private DepartmentService departmentService;

    @BeforeEach
    void initDepartments() {
        departmentService.save(new DepartmentCreateDTO("IT"));
        departmentService.save(new DepartmentCreateDTO("Sales"));
    }

    @Test
    void testSaveAndFindAll() {
        EmployeeCreateDTO employee = createEmployee();

        employeeService.save(employee);
        List<EmployeeResponseDTO> employees = employeeService.findAll();

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getName());
    }

    private EmployeeCreateDTO createEmployee() {
        EmployeeCreateDTO employee = new EmployeeCreateDTO();
        employee.setName("John");
        employee.setAge(30);
        employee.setRole("Developer");
        employee.setExperienceYears(5);
        employee.setDepartmentId(1L);
        return employee;
    }

    private EmployeeCreateDTO createEmployee2() {
        EmployeeCreateDTO employee = new EmployeeCreateDTO();
        employee.setName("Bob");
        employee.setAge(22);
        employee.setRole("Developer");
        employee.setExperienceYears(5);
        employee.setDepartmentId(2L);
        return employee;
    }

    private EmployeeUpdateDTO updateEmployee() {
        EmployeeUpdateDTO employee = new EmployeeUpdateDTO();
        employee.setId(1L);
        employee.setName("Alice");
        employee.setAge(30);
        employee.setRole("Developer");
        employee.setExperienceYears(5);
        return employee;
    }

    @Test
    void testUpdate() {
        EmployeeCreateDTO employee = createEmployee();

        employeeService.save(employee);

        EmployeeResponseDTO employeeResponseDTO = employeeService.findById(1L);
        assertEquals("John", employeeResponseDTO.getName());
        EmployeeUpdateDTO employeeUpdateDTO = updateEmployee();

        EmployeeResponseDTO updated = employeeService.update(employeeUpdateDTO);

        assertEquals("Alice", updated.getName());

        employeeResponseDTO = employeeService.findById(1L);
        assertEquals("Alice", employeeResponseDTO.getName());
    }

    @Test
    void testDelete() {
        EmployeeCreateDTO employee = createEmployee();
        employeeService.save(employee);
        EmployeeResponseDTO employeeResponseDTO = employeeService.findById(1L);
        assertEquals("John", employeeResponseDTO.getName());

        employeeService.delete(1L);

        assertThrows(EntityNotFoundException.class, ()->employeeService.findById(1L));
    }

    @Test
    void testSearchByDepartment() {

        EmployeeCreateDTO employee = createEmployee();
        employeeService.save(employee);
        EmployeeCreateDTO employee2 = createEmployee2();
        employeeService.save(employee2);

        List<EmployeeResponseDTO> inFirstDepartment = employeeService.getAllByDepartment(1L);
        assertEquals(1, inFirstDepartment.size());
        assertEquals("John", inFirstDepartment.get(0).getName());

        List<EmployeeResponseDTO> inSecondDepartment = employeeService.getAllByDepartment(2L);
        assertEquals(1, inSecondDepartment.size());
        assertEquals("Bob", inSecondDepartment.get(0).getName());

        List<EmployeeResponseDTO> inThirdDepartment = employeeService.getAllByDepartment(3L);
        assertEquals(0, inThirdDepartment.size());
    }

    @Test
    void testSearchByName() {

        EmployeeCreateDTO employee = createEmployee();
        employeeService.save(employee);
        EmployeeCreateDTO employee2 = createEmployee2();
        employeeService.save(employee2);

        List<EmployeeResponseDTO> allJohns = employeeService.getAllByName("John");
        assertEquals(1, allJohns.size());
        assertEquals("John", allJohns.get(0).getName());

        List<EmployeeResponseDTO> allBobs = employeeService.getAllByName("bob");
        assertEquals(1, allBobs.size());
        assertEquals("Bob", allBobs.get(0).getName());

        List<EmployeeResponseDTO> allWithO = employeeService.getAllByName("O");
        assertEquals(2, allWithO.size());

        List<EmployeeResponseDTO> allWithA = employeeService.getAllByName("A");
        assertEquals(0, allWithA.size());
    }
}