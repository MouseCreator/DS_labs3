package org.example.model.transformer;

import org.example.extra.TestDataGenerator;
import org.example.model.Department;
import org.example.model.dto.DepartmentRequestDTO;
import org.example.model.dto.DepartmentResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTransformerTest {

    @Test
    void toDepartment() {
        DepartmentTransformer transformer = new DepartmentTransformer();
        Department department = TestDataGenerator.get().allDepartments().getDepartmentList().get(0);
        DepartmentResponseDTO responseDTO = transformer.toResponse(department);
        assertEquals(responseDTO.getId(), department.getId());
        assertEquals(responseDTO.getName(), department.getName());
    }

    @Test
    void toResponse() {
        DepartmentTransformer transformer = new DepartmentTransformer();
        DepartmentRequestDTO dto = new DepartmentRequestDTO();
        dto.setName("Info");
        Department department = transformer.toDepartment(dto);
        assertEquals(dto.getName(), department.getName());
    }
}