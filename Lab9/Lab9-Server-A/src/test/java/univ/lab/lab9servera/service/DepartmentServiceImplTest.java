package univ.lab.lab9servera.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class DepartmentServiceImplTest {

    @Autowired
    private DepartmentServiceImpl departmentService;

    @Test
    void testSaveAndFindAll() {
        DepartmentCreateDTO department = new DepartmentCreateDTO();
        department.setName("IT");

        DepartmentCreateDTO department2 = new DepartmentCreateDTO();
        department2.setName("Sales");

        departmentService.save(department);
        departmentService.save(department2);
        List<DepartmentResponseDTO> departments = departmentService.findAll();

        assertNotNull(departments);
        assertFalse(departments.isEmpty());
        assertEquals(2, departments.size());

        List<String> names = departments.stream().map(DepartmentResponseDTO::getName).sorted(String::compareTo).toList();
        assertEquals("IT", names.get(0));
        assertEquals("Sales", names.get(1));

    }
}