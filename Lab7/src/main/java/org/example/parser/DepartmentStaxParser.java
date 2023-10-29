package org.example.parser;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class DepartmentStaxParser implements Parser<Departments> {
    @Override
    public Departments parse(String filename) {
        try {
            InputStream inputStream = new FileInputStream(filename);
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
            return parseElements(reader);
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Departments parseElements(XMLStreamReader reader) throws XMLStreamException {
        Departments result = null;
        Departments HRDepartments = null;
        Department department = null;
        Employee employee = null;
        String elementName;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT -> {
                    elementName = reader.getLocalName();
                    switch (elementName) {
                        case "HumanResourceDepartment" -> {
                            HRDepartments = new Departments();
                            HRDepartments.setDepartmentList(new ArrayList<>());
                        }
                        case "Department" -> {
                            department = new Department();
                            department.setId(Long.parseLong(reader.getAttributeValue(null, "id")));
                            department.setName(reader.getAttributeValue(null, "name"));
                        }
                        case "Employee" -> {
                            employee = new Employee();
                            employee.setId(Long.parseLong(reader.getAttributeValue(null, "id")));
                            employee.setName(reader.getAttributeValue(null, "name"));
                            employee.setAge(Integer.parseInt(reader.getAttributeValue(null, "age")));
                            employee.setRole(reader.getAttributeValue(null, "role"));
                            employee.setExperienceYears(Integer.parseInt(reader.getAttributeValue(null, "experience")));
                        }
                    }
                }
                case XMLStreamConstants.CHARACTERS -> {
                }

                case XMLStreamConstants.END_ELEMENT -> {
                    String name = reader.getLocalName();
                    switch (name) {
                        case "HumanResourceDepartment" -> result = HRDepartments;
                        case "Department" -> {
                            if (HRDepartments == null || department == null)
                                throw new XMLStreamException();
                            HRDepartments.getDepartmentList().add(department);
                        }
                        case "Employee" -> {
                            if (employee == null || department == null)
                                throw new XMLStreamException();
                            employee.setDepartmentId(department.getId());
                        }
                    }
                }

            }
        }
        return result;
    }
}
