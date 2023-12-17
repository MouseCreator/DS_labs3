package univ.lab.lab9servera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.lab.lab9servera.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByNameContainingIgnoreCase(String name);
}
