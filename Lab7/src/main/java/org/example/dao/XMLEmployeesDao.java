package org.example.dao;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.List;

public class XMLEmployeesDao extends AbstractXMLDao<Departments, Employee> implements EmployeesDao {
    public XMLEmployeesDao(String inputFileXML, Parser<Departments> parser, Writer<Departments> writer) {
        super(inputFileXML, parser, writer);
    }

    @Override
    protected List<Employee> toCollection(Departments container) {
        return container.getEmployeeList();
    }

    public List<Employee> findAllEmployeesOfDepartment(Department department) {
        Long targetId = department.getId();
        return findAllEmployeesOfDepartment(targetId);
    }

    public List<Employee> findAllEmployeesOfDepartment(Long id) {
        return withReadWrite(container-> toCollection(container).stream().filter(e -> id.equals(e.getDepartmentId())).toList());
    }
}
