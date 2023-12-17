package univ.lab.mapper.impl;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import univ.lab.dto.DepartmentResponseDTO;
import univ.lab.mapper.DepartmentMapper;
import univ.lab.model.Department;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-17T23:45:24+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 19 (Oracle Corporation)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public Department toDepartment(DepartmentResponseDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Department department = new Department();

        if ( createDTO.getId() != null ) {
            department.setId( createDTO.getId() );
        }
        if ( createDTO.getName() != null ) {
            department.setName( createDTO.getName() );
        }

        return department;
    }
}
