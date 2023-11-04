package org.example.dao;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.parser.Parser;
import org.example.writer.Writer;

import java.util.*;

public class XMLDepartmentsDao extends AbstractXMLDao<Departments, Department> {

    public XMLDepartmentsDao(String inputFileXML, Parser<Departments> parser, Writer<Departments> writer) {
        super(inputFileXML, parser, writer);
    }

    @Override
    protected List<Department> toCollection(Departments container) {
        return container.getDepartmentList();
    }
}
