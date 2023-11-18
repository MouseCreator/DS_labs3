package org.example.model.transformer;

import org.example.model.Employee;
import org.example.model.dto.EmployeeRequestDTO;
import org.example.model.dto.EmployeeResponseDTO;

public class EmployeeTransformer {
    public Employee toEmployee(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setId(null);
        employee.setName(dto.getName());
        employee.setAge(dto.getAge());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setExperienceYears(dto.getExperienceYears());
        employee.setRole(dto.getRole());
        return employee;
    }

    public EmployeeResponseDTO toResponse(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setAge(employee.getAge());
        dto.setDepartmentId(employee.getDepartmentId());
        dto.setExperienceYears(employee.getExperienceYears());
        dto.setRole(employee.getRole());
        return dto;
    }
}
