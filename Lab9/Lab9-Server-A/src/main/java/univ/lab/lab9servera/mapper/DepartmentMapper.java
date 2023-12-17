package univ.lab.lab9servera.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import univ.lab.lab9servera.config.MapperConfig;
import univ.lab.lab9servera.dto.*;
import univ.lab.lab9servera.model.Department;

@Mapper(config = MapperConfig.class)
public interface DepartmentMapper {
    DepartmentResponseDTO toResponse(Department department);
    Department toDepartment(DepartmentCreateDTO createDTO);
    Department toDepartment(DepartmentUpdateDTO createDTO);

    @Named("departmentById")
    default Department getDepartmentById(Long id) {
        return id == null ? null : new Department(id);
    }
    @Named("departmentToId")
    default Long getDepartmentId(Department department) {
        return department == null ? null : department.getId();
    }
}
