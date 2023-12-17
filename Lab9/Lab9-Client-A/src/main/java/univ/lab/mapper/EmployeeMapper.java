package univ.lab.mapper;

import org.mapstruct.Mapper;
import univ.lab.config.MapperConfig;
import univ.lab.dto.EmployeeResponseDTO;
import univ.lab.model.Employee;

@Mapper(config = MapperConfig.class, uses = DepartmentMapper.class)
public interface EmployeeMapper {
    Employee toEmployee(EmployeeResponseDTO createDTO);

}
