package org.example.model.dto;

import lombok.Data;

@Data
public class EmployeeRequestDTO {
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
