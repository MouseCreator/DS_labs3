package univ.lab.lab9servera.mapper;

import org.mapstruct.Mapper;
import univ.lab.lab9servera.config.MapperConfig;
import univ.lab.lab9servera.dto.EmployeeCreateDTO;
import univ.lab.lab9servera.dto.EmployeeResponseDTO;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.model.Employee;
@Mapper(config = MapperConfig.class)
public interface EmployeeMapper {

    EmployeeResponseDTO toResponse(Employee employee);
    Employee toEmployee(EmployeeCreateDTO createDTO);
    default Department getDepartmentFromId(Long id) {
        return id == null ? null : new Department(id);
    }
    default Long getDepartmentId(Department department) {
        return department == null ? null : department.getId();
    }
}
