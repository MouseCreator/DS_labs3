package univ.lab.model;

import lombok.Data;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Employee {
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;

}
