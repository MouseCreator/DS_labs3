package univ.lab.dto;

import lombok.Data;

@Data
public class EmployeeCreateDTO {
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    private Long departmentId;
}
