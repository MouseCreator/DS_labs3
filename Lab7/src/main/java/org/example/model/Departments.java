package org.example.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
public class Departments implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Department> departmentList;
    private List<Employee> employeeList;

    public Departments() {
        this.departmentList = new ArrayList<>();
        this.employeeList = new ArrayList<>();
    }
}
