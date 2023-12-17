package univ.lab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import univ.lab.config.MapperConfig;
import univ.lab.dto.DepartmentCreateDTO;
import univ.lab.dto.DepartmentResponseDTO;
import univ.lab.dto.DepartmentUpdateDTO;
import univ.lab.model.Department;

@Mapper(config = MapperConfig.class)
public interface DepartmentMapper {
    Department toDepartment(DepartmentResponseDTO createDTO);
    @Named("departmentById")
    default Department getDepartmentById(Long id) {
        return id == null ? null : new Department(id);
    }
    @Named("departmentToId")
    default Long getDepartmentId(Department department) {
        return department == null ? null : department.getId();
    }
}
