package univ.lab.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lab.dto.DepartmentCreateDTO;
import univ.lab.dto.DepartmentUpdateDTO;
import univ.lab.dto.EmployeeCreateDTO;
import univ.lab.dto.EmployeeUpdateDTO;
import univ.lab.mapper.DepartmentMapper;
import univ.lab.mapper.EmployeeMapper;
import univ.lab.model.Department;
import univ.lab.model.Employee;

import java.util.List;

@Service
public class ClientConnector {
    private final DepartmentClientHandler departmentClientHandler;
    private final EmployeeClientHandler employeeClientHandler;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;
    @Autowired
    public ClientConnector(DepartmentClientHandler handler, EmployeeClientHandler employeeClientHandler,
                           DepartmentMapper departmentMapper, EmployeeMapper employeeMapper) {
        this.departmentClientHandler = handler;
        this.employeeClientHandler = employeeClientHandler;
        this.departmentMapper = departmentMapper;
        this.employeeMapper = employeeMapper;
    }

    public List<Employee> getAllEmployees() {
        return this.employeeClientHandler.findAllEmployees().
                toStream()
                .map(employeeMapper::toEmployee)
                .toList();
    }
    public List<Department> getAllDepartments() {
        return this.departmentClientHandler.findAllDepartments().
                toStream()
                .map(departmentMapper::toDepartment)
                .toList();
    }

    public Employee getEmployeeById(Long id) {
        return employeeMapper.toEmployee(this.employeeClientHandler.getEmployeeById(id).block());
    }

    public List<Employee> getEmployeesByDepartment(Long id) {
        return employeeClientHandler.findEmployeeByDepartment(id)
                .toStream()
                .map(employeeMapper::toEmployee)
                .toList();
    }

    public Employee updateEmployee(EmployeeUpdateDTO updateDTO) {
        return employeeMapper.toEmployee(this.employeeClientHandler.updateEmployee(updateDTO).block());
    }

    public Employee createEmployee(EmployeeCreateDTO createDTO) {
        return employeeMapper.toEmployee(this.employeeClientHandler.saveEmployee(createDTO).block());
    }

    public void deleteEmployee(Long id) {
        this.employeeClientHandler.deleteEmployee(id).block();
    }

    public List<Employee> getEmployeesByName(String name) {
        return employeeClientHandler.findEmployeeByName(name)
                .toStream()
                .map(employeeMapper::toEmployee)
                .toList();
    }

    public List<Department> getDepartmentsByName(String name) {
        return departmentClientHandler.findDepartmentByName(name)
                .toStream()
                .map(departmentMapper::toDepartment)
                .toList();
    }

    public Department getDepartmentById(Long id) {
        return departmentMapper.toDepartment(this.departmentClientHandler.getDepartmentById(id).block());
    }

    public Department updateDepartment(DepartmentUpdateDTO departmentUpdateDTO) {
        return departmentMapper.toDepartment(this.departmentClientHandler.update(departmentUpdateDTO).block());
    }

    public Department createDepartment(DepartmentCreateDTO departmentCreateDTO) {
        return departmentMapper.toDepartment(this.departmentClientHandler.save(departmentCreateDTO).block());
    }

    public void deleteDepartment(Long id) {
        this.departmentClientHandler.deleteDepartment(id).block();
    }
}
