package univ.lab.ui;

import univ.lab.dto.EmployeeCreateDTO;
import univ.lab.dto.EmployeeUpdateDTO;
import univ.lab.model.Employee;

public class EmployeeCreator {
    private final CommonController ioManager;
    public EmployeeCreator(CommonController ioManager) {
        this.ioManager = ioManager;
    }

    public EmployeeCreateDTO create() {
        EmployeeCreateDTO employee = new EmployeeCreateDTO();
        employee.setName(ioManager.askString("Enter name"));
        employee.setAge(ioManager.askInteger("Enter age"));
        employee.setRole(ioManager.askString("Enter role"));
        employee.setExperienceYears(ioManager.askInteger("Enter experience"));
        employee.setDepartmentId(ioManager.askLong("Enter department id"));
        return employee;
    }

    public EmployeeUpdateDTO update(Employee employee) {
        EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO();
        employeeUpdateDTO.setName(ioManager.askStringOr("Enter name", employee.getName()));
        employeeUpdateDTO.setAge(ioManager.askIntegerOr("Enter age", employee.getAge()));
        employeeUpdateDTO.setRole(ioManager.askStringOr("Enter role", employee.getRole()));
        employeeUpdateDTO.setExperienceYears(ioManager.askIntegerOr("Enter experience", employee.getExperienceYears()));
        employeeUpdateDTO.setDepartmentId(ioManager.askLongOr("Enter department id", employee.getDepartmentId()));
        return employeeUpdateDTO;
    }
}
