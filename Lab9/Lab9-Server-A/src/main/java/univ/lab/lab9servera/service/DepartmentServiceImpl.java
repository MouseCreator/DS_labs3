package univ.lab.lab9servera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.lab.lab9servera.dto.DepartmentCreateDTO;
import univ.lab.lab9servera.dto.DepartmentResponseDTO;
import univ.lab.lab9servera.mapper.DepartmentMapper;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.repository.DepartmentRepository;

import java.util.List;
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
}
