package org.example.model;

import lombok.Data;
import org.example.dao.IdIterable;

@Data
public class Employee implements IdIterable {
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
