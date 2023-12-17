package univ.lab.lab9servera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.lab.lab9servera.dto.EmployeeCreateDTO;
import univ.lab.lab9servera.dto.EmployeeResponseDTO;
import univ.lab.lab9servera.exception.EntityNotFoundException;
import univ.lab.lab9servera.mapper.EmployeeMapper;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.model.Employee;
import univ.lab.lab9servera.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    @Override
    public List<EmployeeResponseDTO> findAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponseDTO save(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = employeeMapper.toEmployee(employeeCreateDTO);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDTO findById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return employeeMapper.toResponse(optionalEmployee.get());
        }
        throw new EntityNotFoundException("Cannot find employee by id " + id);
    }

    @Override
    public List<EmployeeResponseDTO> getAllByName(String name) {
        return employeeRepository.findAllByNameContainingIgnoreCase(name).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponseDTO> getAllByDepartment(Long departmentId) {
        Department department = (departmentId == null) ? null : new Department(departmentId);
        return employeeRepository.findAllByDepartment(department).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}
