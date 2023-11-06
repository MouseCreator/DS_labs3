package org.example.dao;

import org.example.filter.DepartmentParser;
import org.example.filter.FilterFactory;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.*;
import java.util.function.Predicate;

public class XMLDepartmentsDao extends AbstractXMLDao<Departments, Department> implements DepartmentsDAO {

    private final FilterFactory<Predicate<Department>> filterFactory = new DepartmentParser();

    public XMLDepartmentsDao(String inputFileXML, Parser<Departments> parser, Writer<Departments> writer) {
        super(inputFileXML, parser, writer);
    }

    @Override
    protected List<Department> toCollection(Departments container) {
        return container.getDepartmentList();
    }

    @Override
    public List<Department> findByFilter(String filterString) {
        Predicate<Department> predicate = filterFactory.parse(filterString);
        List<Department> all = findAll();
        return all.stream().filter(predicate).toList();
    }
}
