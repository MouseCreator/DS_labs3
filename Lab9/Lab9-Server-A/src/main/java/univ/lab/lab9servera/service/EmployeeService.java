package univ.lab.lab9servera.service;

import univ.lab.lab9servera.dto.EmployeeCreateDTO;
import univ.lab.lab9servera.dto.EmployeeResponseDTO;
import univ.lab.lab9servera.dto.EmployeeUpdateDTO;

import java.util.List;


public interface EmployeeService {
    List<EmployeeResponseDTO> findAll();
    EmployeeResponseDTO save(EmployeeCreateDTO employee);
    EmployeeResponseDTO findById(Long id);
    List<EmployeeResponseDTO> getAllByName(String name);
    List<EmployeeResponseDTO> getAllByDepartment(Long departmentId);
    EmployeeResponseDTO update(EmployeeUpdateDTO employeeUpdateDTO);
    void delete(Long id);
}
