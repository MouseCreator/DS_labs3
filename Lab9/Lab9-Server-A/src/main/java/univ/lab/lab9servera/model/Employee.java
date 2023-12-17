package univ.lab.lab9servera.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="employees")
public class Employee {
    @Id
    private Long id;

    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    @ManyToOne
    private Department department;

}
