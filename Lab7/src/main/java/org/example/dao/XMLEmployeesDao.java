package org.example.dao;

import org.example.filter.EmployeeParser;
import org.example.filter.FilterFactory;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.List;
import java.util.function.Predicate;

public class XMLEmployeesDao extends AbstractXMLDao<Departments, Employee> implements EmployeesDao {

    private final FilterFactory<Predicate<Employee>> filterFactory = new EmployeeParser();
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

    @Override
    public List<Employee> findByFilter(String filterString) {
        Predicate<Employee> predicate = filterFactory.parse(filterString);
        List<Employee> all = findAll();
        return all.stream().filter(predicate).toList();
    }
}
