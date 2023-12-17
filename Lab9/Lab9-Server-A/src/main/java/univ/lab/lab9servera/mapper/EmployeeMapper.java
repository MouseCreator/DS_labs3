package univ.lab.lab9servera.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import univ.lab.lab9servera.config.MapperConfig;
import univ.lab.lab9servera.dto.EmployeeCreateDTO;
import univ.lab.lab9servera.dto.EmployeeResponseDTO;
import univ.lab.lab9servera.dto.EmployeeUpdateDTO;
import univ.lab.lab9servera.model.Employee;
@Mapper(config = MapperConfig.class, uses = DepartmentMapper.class)
public interface EmployeeMapper {
    @Mapping(target = "departmentId", source = "department", qualifiedByName = "departmentToId")
    EmployeeResponseDTO toResponse(Employee employee);
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentById")
    Employee toEmployee(EmployeeCreateDTO createDTO);
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentById")
    Employee toEmployee(EmployeeUpdateDTO createDTO);

}
