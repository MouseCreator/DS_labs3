package univ.lab.lab9servera.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import univ.lab.lab9servera.dto.*;
import univ.lab.lab9servera.exception.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    void testUpdate() {
        DepartmentCreateDTO department = new DepartmentCreateDTO();
        department.setName("IT");

        departmentService.save(department);

        DepartmentResponseDTO departmentResponse = departmentService.findById(1L);
        assertEquals("IT", departmentResponse.getName());
        DepartmentUpdateDTO departmentUpdateDTO = new DepartmentUpdateDTO();
        departmentUpdateDTO.setId(1L);
        departmentUpdateDTO.setName("Sales");

        DepartmentResponseDTO updated = departmentService.update(departmentUpdateDTO);

        assertEquals("Sales", updated.getName());

        departmentResponse = departmentService.findById(1L);
        assertEquals("Sales", departmentResponse.getName());
    }

    @Test
    void testDelete() {
        DepartmentCreateDTO department = new DepartmentCreateDTO();
        department.setName("IT");
        departmentService.save(department);
        DepartmentResponseDTO departmentResponse = departmentService.findById(1L);
        assertEquals("IT", departmentResponse.getName());

        departmentService.delete(1L);

        assertThrows(EntityNotFoundException.class, ()->departmentService.findById(1L));
    }

    @Test
    void testSearchByName() {
        DepartmentCreateDTO department = new DepartmentCreateDTO();
        department.setName("IT");
        departmentService.save(department);

        DepartmentCreateDTO department2= new DepartmentCreateDTO();
        department2.setName("Culture");
        departmentService.save(department2);
        List<DepartmentResponseDTO> namesWithT = departmentService.getAllByName("T");
        assertEquals(2, namesWithT.size());

        List<DepartmentResponseDTO> namesWithI = departmentService.getAllByName("I");
        assertEquals(1, namesWithI.size());

        List<DepartmentResponseDTO> namesWithO = departmentService.getAllByName("O");
        assertEquals(0, namesWithO.size());
    }
}