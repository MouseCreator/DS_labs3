package org.example.writer;

import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.*;

public class DepartmentsWriter implements Writer<Departments> {
    @Override
    public void write(String filename, Departments instance) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("HumanResourceDepartment");
            doc.appendChild(rootElement);
            HashMap<Department, List<Employee>> departmentsMap = toMap(instance);
            for (Department department : departmentsMap.keySet()) {
                Element departmentElement =createDepartment(doc, department);
                for (Employee employee : departmentsMap.get(department)) {
                    departmentElement.appendChild(createEmployee(doc, employee));
                }
            }

            // Write the XML document to a file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private HashMap<Department, List<Employee>> toMap(Departments departments) {
        HashMap<Department, List<Employee>> departmentEmployeeMap = new HashMap<>();

        for (Department department : departments.getDepartmentList()) {
            departmentEmployeeMap.put(department, new ArrayList<>());
        }

        for (Employee employee : departments.getEmployeeList()) {
            Long departmentId = employee.getDepartmentId();
            Optional<Department> department = departments.getDepartmentList()
                    .stream()
                    .filter(d-> Objects.equals(d.getId(), departmentId))
                    .findFirst();

            if (department.isPresent()) {
                List<Employee> employeesInDepartment = departmentEmployeeMap.get(department.get());
                employeesInDepartment.add(employee);
            }
        }

        return departmentEmployeeMap;
    }
    private Element createDepartment(Document doc, Department department) {
        Element departmentElement = doc.createElement("Department");
        departmentElement.setAttribute("id", department.getId().toString());
        departmentElement.setAttribute("name", department.getName());
        return departmentElement;
    }
    private Element createEmployee(Document doc, Employee employee) {
        Element employeeElement = doc.createElement("Employee");
        employeeElement.setAttribute("id", employee.getId().toString());
        employeeElement.setAttribute("name", employee.getName());
        employeeElement.setAttribute("age", employee.getAge().toString());
        employeeElement.setAttribute("role", employee.getRole());
        employeeElement.setAttribute("experience", employee.getExperienceYears().toString());
        return employeeElement;
    }


}
