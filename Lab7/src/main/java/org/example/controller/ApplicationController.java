package org.example.controller;

import org.example.factory.StaticControllerFactoryImpl;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.util.Printer;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class ApplicationController implements AutoCloseable {
    private CommonController ioManager;
    private DepartmentController departmentController;
    private EmployeesController employeeController;
    private Printer printer;
    private StaticControllerFactoryImpl staticControllerFactory;
    private void initialize() {
        staticControllerFactory = StaticControllerFactoryImpl.get();
        employeeController = staticControllerFactory.initialEmployeeController();
        departmentController = staticControllerFactory.initialDepartmentController();
        ioManager = staticControllerFactory.getCommonController();
        printer = new Printer(ioManager);
    }
    private String readCommand() {
        return ioManager.askString("Enter command");
    }

    public void close() {
        try {
            staticControllerFactory.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String formatCommand(String command) {
        return command.toLowerCase().replace(" +", " ").trim();
    }
    private String[] nextParts(String[] originalArray) {
        return Arrays.copyOfRange(originalArray, 1, originalArray.length);
    }
    private void commandProcessor(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length < 2) {
            ioManager.print("Cannot execute command! Expected input format: [data type] [command] [parameters]");
            return;
        }
        String dataType = formatCommand(parts[0]);
        switch (dataType) {
            case "d", "departments" -> processDepartments(nextParts(parts));
            case "e", "employees" -> processEmployees(nextParts(parts));
            default -> ioManager.print("Unexpected datatype: " + dataType);
        }
    }

    private void processDepartments(String[] strings) {
        String command = strings[0];
        String[] params = nextParts(strings);
        switch (command) {
            case "add" -> addDepartment(params);
            case "get" -> getDepartment(params);
            case "remove" -> removeDepartment(params);
            case "update" -> updateDepartment(params);
        }
    }

    private void updateDepartment(String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        Long ln = Long.parseLong(params[0]);
        Department department = new Department();
        department.setId(ln);
        initDepartment(department);
        departmentController.update(department);
    }

    private void removeDepartment(String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        Long ln = Long.parseLong(params[0]);
        departmentController.remove(ln);
    }

    private void addDepartment(String[] params) {
        if (params.length>0) {
            ioManager.print("Unexpected number of parameters: " + params.length + ". Expected no parameters");
            return;
        }
        Department department = new Department();
        initDepartment(department);
        departmentController.create(department);
    }

    private void initDepartment(Department department) {
        department.setName(ioManager.askString("Enter department name"));
    }

    private void getDepartment(String[] params) {
        List<Department> result;
        if (params.length == 0) {
            result = departmentController.getAll();
        } else {
            String[] parts = params[0].split(" ", 2);
            String cmd = params[0];
            switch (cmd) {
                case "a", "all" -> {
                    result = departmentController.getAll();
                }
                case "f", "filter" -> result = departmentController.filter(parts[1]);
                case "id" -> {
                    Long id = Long.parseLong(params[1]);
                    try {
                        Department department = departmentController.get(id);
                        printer.print(department.toString());
                    } catch (NoSuchElementException e) {
                        ioManager.print("No element found with id " + id);
                    }
                    return;
                }
                default -> {
                    ioManager.print("Cannot resolve " + cmd);
                    return;
                }
            }
        }
        printer.print(result);
        while (true) {
            String answer = ioManager.askString("How to process the result?");
            String[] commands = answer.split(" ", 2);
            String ans = formatCommand(commands[0]);
            switch (ans) {
                case "s", "save" -> {
                    saveToFile(commands[1], result);
                    return;
                }
                case "f", "filter" -> {
                    String fString = commands[1];
                    result = departmentController.filter(result, fString);
                    printer.print(result);
                }
                case "b", "break" -> {
                    return;
                }
            }
        }
    }

    private void saveToFile(String filename, List<Department> departments) {
        Departments departmentsObj = new Departments();
        departmentsObj.setDepartmentList(departments);
        for (Department department : departments) {
            List<Employee> employeeList = employeeController.getAllEmployeesOfDepartment(department.getId());
            departmentsObj.getEmployeeList().addAll(employeeList);
        }
        save(filename, departmentsObj);
    }

    private void save(String filename, Departments departmentsObj) {
        Writer<Departments> writer = new DepartmentsWriter();
        writer.write(filename, departmentsObj);
    }

    private void processEmployees(String[] strings) {

    }
}
