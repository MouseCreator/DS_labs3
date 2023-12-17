package univ.lab.mapper.impl;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import univ.lab.dto.EmployeeResponseDTO;
import univ.lab.mapper.EmployeeMapper;
import univ.lab.model.Employee;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-17T23:45:24+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public Employee toEmployee(EmployeeResponseDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        if ( createDTO.getId() != null ) {
            employee.setId( createDTO.getId() );
        }
        if ( createDTO.getName() != null ) {
            employee.setName( createDTO.getName() );
        }
        if ( createDTO.getAge() != null ) {
            employee.setAge( createDTO.getAge() );
        }
        if ( createDTO.getRole() != null ) {
            employee.setRole( createDTO.getRole() );
        }
        if ( createDTO.getExperienceYears() != null ) {
            employee.setExperienceYears( createDTO.getExperienceYears() );
        }
        if ( createDTO.getDepartmentId() != null ) {
            employee.setDepartmentId( createDTO.getDepartmentId() );
        }

        return employee;
    }
}
