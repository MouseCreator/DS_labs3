package univ.lab.lab9servera.service;

import univ.lab.lab9servera.dto.EmployeeCreateDTO;
import univ.lab.lab9servera.dto.EmployeeResponseDTO;

import java.util.List;


public interface EmployeeService {
    List<EmployeeResponseDTO> findAll();
    EmployeeResponseDTO save(EmployeeCreateDTO employee);
    EmployeeResponseDTO findById(Long id);
    List<EmployeeResponseDTO> getAllByName(String name);
}
