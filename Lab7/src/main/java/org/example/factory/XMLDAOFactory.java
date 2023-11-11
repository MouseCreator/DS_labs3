package org.example.factory;

import org.example.dao.DepartmentsDAO;
import org.example.dao.EmployeesDao;
import org.example.dao.XMLDepartmentsDao;
import org.example.dao.XMLEmployeesDao;
import org.example.model.Departments;
import org.example.parser.DepartmentStaxParser;
import org.example.parser.Parser;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;

public class XMLDAOFactory {

    private final Writer<Departments> writer;
    private final Parser<Departments> parser;
    private static XMLDAOFactory factory;

    private XMLDAOFactory() {
        writer = new DepartmentsWriter();
        parser = new DepartmentStaxParser();
    }
    public static XMLDAOFactory get() {
        if (factory == null)
            factory = new XMLDAOFactory();
        return factory;
    }
    public EmployeesDao getEmployeeXMLDAO(String src) {
        return new XMLEmployeesDao(src, parser, writer);
    }
    public DepartmentsDAO getDepartmentXMLDAO(String src) {
        return new XMLDepartmentsDao(src, parser, writer);
    }
}
