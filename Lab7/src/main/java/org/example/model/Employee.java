package org.example.model;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
