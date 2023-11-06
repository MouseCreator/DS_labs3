package org.example.dao;

import org.example.factory.FilterManagerFactory;
import org.example.factory.FilterManagerFactoryImpl;
import org.example.filter.FilterManager;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.*;

public class XMLDepartmentsDao extends AbstractXMLDao<Departments, Department> implements DepartmentsDAO {

    private final FilterManager<Department> filterFactory;

    public XMLDepartmentsDao(String inputFileXML, Parser<Departments> parser, Writer<Departments> writer) {
        super(inputFileXML, parser, writer);

        FilterManagerFactory factory = new FilterManagerFactoryImpl();
        filterFactory = factory.getForDepartment();
    }

    @Override
    protected List<Department> toCollection(Departments container) {
        return container.getDepartmentList();
    }

    @Override
    public List<Department> findByFilter(String filterString) {
        return filterFactory.filter(findAll(), filterString);
    }
}
