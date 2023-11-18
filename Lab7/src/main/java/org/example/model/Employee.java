package org.example.model;

import lombok.Data;
import org.example.dao.IdIterable;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Employee implements IdIterable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
