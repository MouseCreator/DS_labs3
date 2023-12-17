package univ.lab.lab9servera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;
import univ.lab.lab9servera.dto.DepartmentUpdateDTO;
import univ.lab.lab9servera.exception.EntityNotFoundException;
import univ.lab.lab9servera.mapper.DepartmentMapper;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    @Override
    public List<DepartmentResponseDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Override
    public DepartmentResponseDTO save(DepartmentCreateDTO departmentCreateDTO) {
        Department department = departmentMapper.toDepartment(departmentCreateDTO);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public DepartmentResponseDTO findById(Long id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()){
            return departmentMapper.toResponse(departmentOptional.get());
        }
        throw new EntityNotFoundException("Cannot find department by id " + id);
    }

    @Override
    public List<DepartmentResponseDTO> getAllByName(String name) {
        return departmentRepository.findAllByNameContainingIgnoreCase(name).stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @Override
    public DepartmentResponseDTO update(DepartmentUpdateDTO departmentUpdateDTO) {
        Department department = departmentMapper.toDepartment(departmentUpdateDTO);
        return departmentMapper.toResponse(departmentRepository.save(department));
    }

    @Override
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
