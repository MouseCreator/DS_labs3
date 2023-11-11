package org.example.controller;

import org.example.dao.DepartmentsDAO;
import org.example.dao.EmployeesDao;
import org.example.dao.IdIterable;
import org.example.factory.StaticControllerFactoryImpl;
import org.example.factory.XMLDAOFactory;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.util.Printer;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class ApplicationController implements AutoCloseable {
    private CommonController ioManager;
    private DepartmentController departmentController;
    private EmployeesController employeeController;
    private Printer printer;
    private StaticControllerFactoryImpl staticControllerFactory;
    public void start() {
        initialize();
        mainLoop();
    }
    private void initialize() {
        staticControllerFactory = StaticControllerFactoryImpl.get();
        employeeController = staticControllerFactory.initialEmployeeController();
        departmentController = staticControllerFactory.initialDepartmentController();
        ioManager = staticControllerFactory.getCommonController();
        printer = new Printer(ioManager);
        toSource("db");
    }
    private void mainLoop() {
        while (true) {
            String command = readCommand();
            if (command.trim().equals("close")) {
                break;
            }
            commandProcessor(command);
        }
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
            case "s", "source" -> changeSource(nextParts(parts));
            default -> ioManager.print("Unexpected datatype: " + dataType);
        }
    }

    private void changeSource(String[] source) {
        String src = source[0];
        toSource(src);
    }
    private void toSource(String src) {
        EmployeesDao employeesDatabaseDAO;
        DepartmentsDAO departmentsDatabaseDAO;
        if (src.equalsIgnoreCase("db")) {
            employeesDatabaseDAO = StaticControllerFactoryImpl.get().getEmployeesDatabaseDAO();
            departmentsDatabaseDAO = StaticControllerFactoryImpl.get().getDepartmentsDatabaseDAO();
        } else {
            employeesDatabaseDAO = XMLDAOFactory.get().getEmployeeXMLDAO(src);
            departmentsDatabaseDAO = XMLDAOFactory.get().getDepartmentXMLDAO(src);
        }
        employeeController.source(employeesDatabaseDAO);
        departmentController.source(departmentsDatabaseDAO);
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
        Long id = Long.parseLong(params[0]);
        Department department = new Department();
        department.setId(id);
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
        BiFunction<String, List<Department>, Void> saveFunction = (str, list) -> { saveToFile(str, list); return null;};
        commonGetter(params, departmentController, saveFunction);
    }

    private <T extends IdIterable> void commonGetter(String[] params, ModelController<T> controller,
                                                     BiFunction<String, List<T>, Void> save) {
        List<T> result;
        if (params.length == 0) {
            result = controller.getAll();
        } else {
            String[] parts = params[0].split(" ", 2);
            String cmd = params[0];
            switch (cmd) {
                case "a", "all" -> result = controller.getAll();
                case "f", "filter" -> result = controller.filter(parts[1]);
                case "id" -> {
                    Long id = Long.parseLong(params[1]);
                    try {
                        T model = controller.get(id);
                        printer.print(model.toString());
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
                    try {
                        save.apply(params[1], result);
                    } catch (Exception e) {
                        ioManager.print("Cannot save to file " + commands[1]);
                    }
                    return;
                }
                case "f", "filter" -> {
                    String fString = commands[1];
                    result = controller.filter(result, fString);
                    printer.print(result);
                }
                case "b", "break" -> {
                    return;
                }
                default -> ioManager.print("Illegal command! Expected save/filter/break!");
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

    private void saveEmployees(String filename, List<Employee> list) {
        Departments departmentObj = new Departments();
        departmentObj.getEmployeeList().addAll(list);
        List<Department> tempDepartmentList = new ArrayList<>();
        for (Employee employee : list) {
            tempDepartmentList.add(departmentController.get(employee.getDepartmentId()));
        }
        departmentObj.getDepartmentList().addAll(tempDepartmentList.stream().distinct().toList());
        save(filename, departmentObj);
    }

    private void save(String filename, Departments departmentsObj) {
        Writer<Departments> writer = new DepartmentsWriter();
        String absPath = "src/main/resources/IO/" + filename + ".xml";
        writer.write(absPath, departmentsObj);
    }

    private void processEmployees(String[] strings) {
        String command = strings[0];
        String[] params = nextParts(strings);
        switch (command) {
            case "add" -> addEmployee(params);
            case "get" -> getEmployee(params);
            case "remove" -> removeEmployee(params);
            case "update" -> updateEmployee(params);
        }
    }

    private void updateEmployee(String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        Long id = Long.parseLong(params[0]);
        try {
            Employee employee = employeeController.get(id);
            editEmployee(employee);
            employeeController.update(employee);
        } catch (NoSuchElementException e) {
            ioManager.print("Cannot find employee with id "+ id);
        }

    }

    private void removeEmployee(String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        Long ln = Long.parseLong(params[0]);
        employeeController.remove(ln);
    }

    private void getEmployee(String[] params) {
        BiFunction<String, List<Employee>, Void> saveFunction = (str, list) -> { saveEmployees(str, list); return null;};
        commonGetter(params, employeeController, saveFunction);
    }

    private void addEmployee(String[] params) {
        if (params.length>0) {
            ioManager.print("Unexpected number of parameters: " + params.length + ". Expected no parameters");
            return;
        }
        Employee employee = new Employee();
        initEmployee(employee);
        employeeController.create(employee);
    }

    private void initEmployee(Employee employee) {
        employee.setName(ioManager.askString("Enter name"));
        employee.setAge(ioManager.askInteger("Enter age"));
        employee.setRole(ioManager.askString("Enter role"));
        employee.setExperienceYears(ioManager.askInteger("Enter experience"));
        Long id = null;
        while (id == null) {
            Long temp = ioManager.askLong("Enter department id");
            try {
                departmentController.get(temp);
                id = temp;
            } catch (Exception e) {
                ioManager.print("Entered department does not exist!");
            }
        }
        employee.setDepartmentId(id);
    }
    private void editEmployee(Employee employee) {
        employee.setName(ioManager.askStringOr("Enter name", employee.getName()));
        employee.setAge(ioManager.askIntegerOr("Enter age", employee.getAge()));
        employee.setRole(ioManager.askStringOr("Enter role", employee.getRole()));
        employee.setExperienceYears(ioManager.askIntegerOr("Enter experience", employee.getExperienceYears()));
        Long id = null;
        while (id == null) {
            Long temp = ioManager.askLongOr("Enter department id", employee.getDepartmentId());
            try {
                departmentController.get(temp);
                id = temp;
            } catch (Exception e) {
                ioManager.print("Entered department does not exist!");
            }
        }
        employee.setDepartmentId(id);
    }


}
