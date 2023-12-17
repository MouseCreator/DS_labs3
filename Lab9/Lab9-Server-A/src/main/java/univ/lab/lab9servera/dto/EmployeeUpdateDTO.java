package univ.lab.lab9servera.dto;

import lombok.Data;

@Data
public class EmployeeUpdateDTO {
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
