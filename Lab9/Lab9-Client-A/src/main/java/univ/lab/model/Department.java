package univ.lab.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Department {
    private Long id;
    private String name;

    public Department(Long id) {
        this.id = id;
    }
}
