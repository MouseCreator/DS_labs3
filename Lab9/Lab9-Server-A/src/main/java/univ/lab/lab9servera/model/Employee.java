package univ.lab.lab9servera.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String role;
    private Integer experienceYears;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Department department;

}
