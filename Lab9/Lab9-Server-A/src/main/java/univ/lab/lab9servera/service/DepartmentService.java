package univ.lab.lab9servera.service;

import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;
import univ.lab.lab9servera.dto.DepartmentUpdateDTO;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponseDTO> findAll();
    DepartmentResponseDTO save(DepartmentCreateDTO department);
    DepartmentResponseDTO findById(Long id);
    DepartmentResponseDTO update(DepartmentUpdateDTO departmentUpdateDTO);
    void delete(Long id);
    List<DepartmentResponseDTO> getAllByName(String name);
}
