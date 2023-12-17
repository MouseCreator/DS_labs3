package univ.lab.lab9servera.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import univ.lab.lab9servera.model.Department;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class DepartmentServiceImplTest {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Test
    void testSaveAndFindAll() {
        Department department = new Department();
        department.setName("IT");

        Department department2 = new Department();
        department2.setName("Sales");

        departmentService.save(department);
        departmentService.save(department2);
        List<Department> departments = departmentService.findAll();

        assertNotNull(departments);
        assertFalse(departments.isEmpty());
        assertEquals(2, departments.size());

        List<String> names = departments.stream().map(Department::getName).sorted(String::compareTo).toList();
        assertEquals("IT", names.get(0));
        assertEquals("Sales", names.get(1));

    }
}