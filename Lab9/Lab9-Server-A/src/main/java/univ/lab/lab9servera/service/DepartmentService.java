package univ.lab.lab9servera.service;

import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponseDTO> findAll();
    DepartmentResponseDTO save(DepartmentCreateDTO department);
}
