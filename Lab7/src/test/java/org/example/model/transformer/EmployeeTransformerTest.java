package org.example.model.transformer;

import org.example.extra.TestDataGenerator;
import org.example.model.Employee;
import org.example.model.dto.EmployeeRequestDTO;
import org.example.model.dto.EmployeeResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTransformerTest {

    @Test
    void toEmployee() {
        EmployeeTransformer transformer = new EmployeeTransformer();
        TestDataGenerator generator = new TestDataGenerator();
        Employee employee = generator.allDepartments().getEmployeeList().get(0);
        EmployeeResponseDTO employeeResponseDTO = transformer.toResponse(employee);
        assertEquals(employeeResponseDTO.getId(), employee.getId());
        assertEquals(employeeResponseDTO.getAge(), employee.getAge());
        assertEquals(employeeResponseDTO.getName(), employee.getName());
        assertEquals(employeeResponseDTO.getDepartmentId(), employee.getDepartmentId());
        assertEquals(employeeResponseDTO.getExperienceYears(), employee.getExperienceYears());
        assertEquals(employeeResponseDTO.getRole(), employee.getRole());
    }

    @Test
    void toResponse() {
        EmployeeTransformer transformer = new EmployeeTransformer();
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("John");
        dto.setRole("Manager");
        dto.setExperienceYears(1);
        dto.setAge(22);
        dto.setDepartmentId(1L);
        Employee employee = transformer.toEmployee(dto);
        assertEquals(dto.getAge(), employee.getAge());
        assertEquals(dto.getName(), employee.getName());
        assertEquals(dto.getDepartmentId(), employee.getDepartmentId());
        assertEquals(dto.getExperienceYears(), employee.getExperienceYears());
        assertEquals(dto.getRole(), employee.getRole());

    }
}