package org.example.model;

import lombok.Data;

import java.util.List;
@Data
public class Departments {
    private List<Department> departmentList;
    private List<Employee> employeeList;
}
