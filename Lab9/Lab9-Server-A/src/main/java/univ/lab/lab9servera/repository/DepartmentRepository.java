package univ.lab.lab9servera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lab.lab9servera.model.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByNameContainingIgnoreCase(String name);
}
