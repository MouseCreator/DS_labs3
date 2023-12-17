package univ.lab.lab9servera.mapper;

import org.mapstruct.Mapper;
import univ.lab.lab9servera.config.MapperConfig;
import univ.lab.lab9servera.dto.*;
import univ.lab.lab9servera.model.Department;

@Mapper(config = MapperConfig.class)
public interface DepartmentMapper {
    DepartmentResponseDTO toResponse(Department department);
    Department toDepartment(DepartmentCreateDTO createDTO);
    Department toDepartment(DepartmentUpdateDTO createDTO);
}
