package univ.lab.lab9servera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.lab.lab9servera.model.Department;
import univ.lab.lab9servera.repository.DepartmentRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
    }
}
