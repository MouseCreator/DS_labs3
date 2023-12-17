package univ.lab.lab9servera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import univ.lab.lab9servera.model.Employee;
import univ.lab.lab9servera.repository.EmployeeRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
