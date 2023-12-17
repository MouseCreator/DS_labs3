package univ.lab.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentCreateDTO {
    private String name;

    public DepartmentCreateDTO(String name) {
        this.name = name;
    }
}
