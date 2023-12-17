package univ.lab.mapper;

import org.mapstruct.Mapper;
import univ.lab.config.MapperConfig;
import univ.lab.dto.DepartmentResponseDTO;
import univ.lab.model.Department;

@Mapper(config = MapperConfig.class)
public interface DepartmentMapper {
    Department toDepartment(DepartmentResponseDTO createDTO);

}
