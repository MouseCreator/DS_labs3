package org.example.client;

import org.example.communicator.ClientCommunicator;
import org.example.controller.*;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.model.dto.Status;
import org.example.util.Printer;
import org.example.writer.DepartmentsWriter;
import org.example.writer.Writer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class CommonClientController implements Client {
    private final ClientCommunicator communicator;
    public CommonClientController(ClientCommunicator communicator) {
        this.communicator = communicator;
        ioManager = LocalController.consoleController();
        printer = new Printer(ioManager);
    }
    private final CommonController ioManager;
    private final Printer printer;
    public void start() {
        mainLoop();
    }
    private void mainLoop() {
        while (true) {
            String command = readCommand();
            if (command.trim().equals("close")) {
                break;
            }
            try {
                commandProcessor(command);
            } catch (Exception e) {
                ioManager.print("Error: " + e.getMessage());
            }
        }
    }

    private String readCommand() {
        return ioManager.askString("Enter command");
    }

    public void close() {
        communicator.close();
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
        String dataType = formatCommand(safeParam(parts));
        Request request = new Request();
        switch (dataType) {
            case "d", "departments" -> processDepartments(request, nextParts(parts));
            case "e", "employees" -> processEmployees(request, nextParts(parts));
            default -> ioManager.print("Unexpected datatype: " + dataType);
        }
    }

    private void processDepartments(Request request, String[] strings) {
        request.setTarget("Department");
        String command = safeParam(strings);
        String[] params = nextParts(strings);
        switch (command) {
            case "add" -> addDepartment(request, params);
            case "get" -> getDepartment(request, params);
            case "remove" -> removeDepartment(request, params);
            case "update" -> updateDepartment(request, params);
            default -> { ioManager.print("Unknown command " + command); return; }
        }
        sendAndProcess(request);
    }

    private void sendAndProcess(Request request) {
        Response response = communicator.sendAndReceive(request);
        int status = response.getStatus();
        switch (status) {
            case Status.SUCCESS, Status.WARNING -> ioManager.print(response.getDetailsString());
            case Status.COLLECTION -> {
                printResultCollection(response);
                processSave(request, response);
            } default -> throw new IllegalArgumentException("Status unknown: " + status);
        }
    }

    private void printResultCollection(Response response) {
        printer.print(response.getDepartments());
    }

    private void processSave(Request request, Response response) {
        boolean isSave = ioManager.askBoolean("Save the result collection?");
        if (!isSave) {
            return;
        }
        String filename = ioManager.askString("Enter filename");
        if (request.getTarget().equals("Department")) {
            saveToFile(filename, response.getDepartments().getDepartmentList());
        } else if (request.getTarget().equals("Employee")) {
            saveEmployees(filename, response.getDepartments().getEmployeeList());
        } else {
            ioManager.print("Unknown target!");
        }
    }

    private Departments initDepartments(Department department) {
        Departments departments = new Departments();
        departments.getDepartmentList().add(department);
        return departments;
    }
    private Departments initDepartments(Employee employee) {
        Departments departments = new Departments();
        departments.getEmployeeList().add(employee);
        return departments;
    }

    private void updateDepartment(Request request, String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
            return;
        }
        request.setMethod("update");
        Long id = Long.parseLong(safeParam(params));
        Department department = findDepartmentById(id);
        editDepartment(department);
        Departments departments = initDepartments(department);
        request.setDepartments(departments);
    }

    private void removeDepartment(Request request, String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
            return;
        }
        request.setDetails(safeParam(params));
    }

    private void addDepartment(Request request, String[] params) {
        if (params.length>0) {
            ioManager.print("Unexpected number of parameters: " + params.length + ". Expected no parameters");
            return;
        }
        request.setMethod("post");
        Department department = new Department();
        initDepartment(department);
        request.setDepartments(initDepartments(department));
    }

    private void initDepartment(Department department) {
        department.setName(ioManager.askString("Enter department name"));
    }

    private void getDepartment(Request request, String[] params) {
        request.setDetails(safeParam(params));
        request.setMethod("get");
    }

    private static String safeParam(String[] params) {
        return params.length == 0 ? "" : params[0];
    }

    private void saveToFile(String filename, List<Department> departments) {
        Departments departmentsObj = new Departments();
        departmentsObj.setDepartmentList(departments);
        for (Department department : departments) {
           List<Employee> employeeList = allEmployeesOfDepartment(department.getId());
           departmentsObj.getEmployeeList().addAll(employeeList);
        }
        save(filename, departmentsObj);
    }

    private void saveEmployees(String filename, List<Employee> list) {
        Departments departmentObj = new Departments();
        departmentObj.getEmployeeList().addAll(list);
        List<Department> tempDepartmentList = new ArrayList<>();
        for (Employee employee : list) {
            tempDepartmentList.add(findDepartmentById(employee.getDepartmentId()));
        }
        departmentObj.getDepartmentList().addAll(tempDepartmentList.stream().distinct().toList());
        save(filename, departmentObj);
    }

    private List<Employee> allEmployeesOfDepartment(Long id) {
        Request request = new Request();
        request.setTarget("Employee");
        request.setMethod("get");
        request.setDetails("f department="+id);
        Response result = communicator.sendAndReceive(request);
        return result.getDepartments().getEmployeeList();
    }
    private void save(String filename, Departments departmentsObj) {
        Writer<Departments> writer = new DepartmentsWriter();
        String absPath = toFilename(filename);
        try {
            writer.write(absPath, departmentsObj);
        } catch (Exception e) {
            ioManager.print("Could not save the file");
        }
    }

    private String toFilename(String filename) {
        String IO_Directory = "src/main/resources/IO/";
        return IO_Directory + filename.replace(".xml", "") + ".xml";
    }

    private void processEmployees(Request request, String[] strings) {
        request.setTarget("Employee");
        String command = safeParam(strings);
        String[] params = nextParts(strings);
        switch (command) {
            case "add" -> addEmployee(request, params);
            case "get" -> getEmployee(request, params);
            case "remove" -> removeEmployee(request, params);
            case "update" -> updateEmployee(request, params);
            default -> ioManager.print("Unknown command " + command);
        }
        sendAndProcess(request);
    }

    private void updateEmployee(Request request, String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        request.setMethod("update");
        Long id = Long.parseLong(safeParam(params));
        try {
            Employee employee = findEmployeeById(id);
            printer.print(employee);
            editEmployee(employee);
            request.setDepartments(initDepartments(employee));
        } catch (NoSuchElementException e) {
            ioManager.print("Cannot find employee with id "+ id);
        }
    }

    private Employee findEmployeeById(Long id) {
        Request request = new Request();
        request.setTarget("Employee");
        request.setMethod("get");
        request.setDetails("id " + id);
        Response result = communicator.sendAndReceive(request);
        if (result.getDepartments().getEmployeeList().isEmpty())
            throw new NoSuchElementException("Cannot find employee with id " + id);
        return result.getDepartments().getEmployeeList().get(0);
    }

    private Department findDepartmentById(Long id) {
        Request request = new Request();
        request.setTarget("Department");
        request.setMethod("get");
        request.setDetails("id " + id);
        Response result = communicator.sendAndReceive(request);
        if (result.getDepartments().getDepartmentList().isEmpty())
            throw new NoSuchElementException("Cannot find department with id " + id);
        return result.getDepartments().getDepartmentList().get(0);
    }

    private void removeEmployee(Request request, String[] params) {
        if (params.length!=1) {
            ioManager.print("Expected one parameter!");
        }
        request.setDetails(safeParam(params));
        request.setMethod("delete");
    }

    private void getEmployee(Request request, String[] params) {
        if (params.length == 0)
            request.setDetails("");
        else
            request.setDetails(safeParam(params));
        request.setMethod("get");
    }

    private void addEmployee(Request request, String[] params) {
        if (params.length>0) {
            ioManager.print("Unexpected number of parameters: " + params.length + ". Expected no parameters");
            return;
        }
        Employee employee = new Employee();
        initEmployee(employee);
        request.setMethod("post");
        request.setDepartments(initDepartments(employee));
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
                findDepartmentById(temp);
                id = temp;
            } catch (NoSuchElementException e) {
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
                findDepartmentById(temp);
                id = temp;
            } catch (Exception e) {
                ioManager.print("Entered department does not exist!");
            }
        }
        employee.setDepartmentId(id);
    }

    private void editDepartment(Department department) {
        department.setName(ioManager.askStringOr("Enter name", department.getName()));
    }
}
