package org.example.model.transformer;

import org.example.model.Department;
import org.example.model.dto.DepartmentRequestDTO;
import org.example.model.dto.DepartmentResponseDTO;

public class DepartmentTransformer {
    public Department toDepartment(DepartmentRequestDTO dto) {
        Department department = new Department();
        department.setId(null);
        department.setName(dto.getName());
        return department;
    }

    public DepartmentResponseDTO toResponse(Department department) {
        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(department.getId());
        responseDTO.setName(department.getName());
        return responseDTO;
    }


}
