package univ.lab.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Departments {
    private List<Department> departmentList;
    private List<Employee> employeeList;

    public Departments() {
        this.departmentList = new ArrayList<>();
        this.employeeList = new ArrayList<>();
    }
}
