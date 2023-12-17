package univ.lab.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import univ.lab.client.ClientConnector;
import univ.lab.dto.DepartmentCreateDTO;
import univ.lab.dto.DepartmentUpdateDTO;
import univ.lab.dto.EmployeeCreateDTO;
import univ.lab.dto.EmployeeUpdateDTO;
import univ.lab.model.Department;
import univ.lab.model.Departments;
import univ.lab.model.Employee;
import univ.lab.writer.DepartmentsWriter;
import univ.lab.writer.Writer;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCommunicator {
    private ClientConnector clientCommunicator;
    private EmployeeCreator employeeCreator;
    private DepartmentCreator departmentCreator;
    private CommonController ioManager;

    @Autowired
    public void setClientCommunicator(ClientConnector clientCommunicator) {
        this.clientCommunicator = clientCommunicator;
    }


    private String readCommand() {
        return ioManager.askString("Enter command");
    }
    public void mainLoop() {
        initIoManager();
        while (true) {
            String command = readCommand();
            if (command.trim().equals("close")) {
                break;
            }
            try {
                commandProcessor(CommandStack.create(command));
            } catch (Exception e) {
                ioManager.print(e.getMessage());
            }
        }
    }

    private void initIoManager() {
        ioManager = LocalController.consoleController();
        employeeCreator = new EmployeeCreator(ioManager);
        departmentCreator = new DepartmentCreator(ioManager);
    }

    private void commandProcessor(CommandStack commandStack) {
        String root = commandStack.getNextCommand();
        switch (root) {
            case "d", "departments" -> processDepartments(commandStack);
            case "e", "employees" -> processEmployees(commandStack);
            default -> ioManager.print("Unexpected datatype: " + root);
        }
    }

    private void processEmployees(CommandStack commandStack) {
        String command = commandStack.getNextCommand();
        switch (command) {
            case "add" -> addEmployee(commandStack);
            case "get" -> getEmployee(commandStack);
            case "remove" -> removeEmployee(commandStack);
            case "update" -> updateEmployee(commandStack);
            default -> ioManager.print("Unknown command " + command);
        }
    }

    private void updateEmployee(CommandStack commandStack) {
        Long id = Long.parseLong(commandStack.getNextValue());
        Employee origin = clientCommunicator.getEmployeeById(id);
        EmployeeUpdateDTO employeeUpdateDTO = employeeCreator.update(origin);
        clientCommunicator.updateEmployee(employeeUpdateDTO);
    }

    private void removeEmployee(CommandStack commandStack) {
        Long id = Long.parseLong(commandStack.getNextValue());
        clientCommunicator.deleteDepartment(id);
        ioManager.print("Deletion success!");
    }

    private List<Employee> employeeGetter(CommandStack commandStack) {
        String requestType = commandStack.getNextCommand();
        switch (requestType) {
            case "id" -> {
                Long id = Long.parseLong(commandStack.getNextValue());
                return List.of(clientCommunicator.getEmployeeById(id));
            }
            case "name" -> {
                String name = commandStack.getNextValue();
                return clientCommunicator.getEmployeesByName(name);
            }
            case "department" -> {
                Long id = Long.parseLong(commandStack.getNextValue());
                return clientCommunicator.getEmployeesByDepartment(id);
            }
            case "all" -> {
                return clientCommunicator.getAllEmployees();
            }
            default -> {
                ioManager.print("Unknown get request type");
                return List.of();
            }
        }
    }
    private void getEmployee(CommandStack commandStack) {
        if (commandStack.isEmpty()) {
            ioManager.print("Unexpected number of parameters");
            return;
        }

        List<Employee> employees = employeeGetter(commandStack);
        ioManager.print(employees.toString());

        if(ioManager.askBoolean("Save to file?")) {
            String filename = ioManager.askString("Enter filename:");
            saveEmployees(filename, employees);
        }
    }
    private void saveEmployees(String filename, List<Employee> list) {
        Departments departmentObj = new Departments();
        departmentObj.getEmployeeList().addAll(list);
        List<Department> tempDepartmentList = new ArrayList<>();
        for (Employee employee : list) {
            tempDepartmentList.add(clientCommunicator.getDepartmentById(employee.getDepartmentId()));
        }
        departmentObj.getDepartmentList().addAll(tempDepartmentList.stream().distinct().toList());
        save(filename, departmentObj);
    }
    private void saveDepartments(String filename, List<Department> departments) {
        Departments departmentsObj = new Departments();
        departmentsObj.setDepartmentList(departments);
        for (Department department : departments) {
            List<Employee> employeeList = clientCommunicator.getEmployeesByDepartment(department.getId());
            departmentsObj.getEmployeeList().addAll(employeeList);
        }
        save(filename, departmentsObj);
    }
    private String toFilename(String src) {
        String IO_Directory = "src/main/resources/IO/";
        return IO_Directory + src.replace(".xml", "") + ".xml";
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

    private void addEmployee(CommandStack commandStack) {
        if (commandStack.hasCommand()) {
            ioManager.print("Unexpected number of parameters");
            return;
        }
        EmployeeCreateDTO employeeCreateDTO = employeeCreator.create();
        Employee employee = clientCommunicator.createEmployee(employeeCreateDTO);
        ioManager.print("Successfully Created Employee:\n" + employee.toString());
    }

    private void processDepartments(CommandStack commandStack) {
        String command = commandStack.getNextCommand();
        switch (command) {
            case "add" -> addDepartment(commandStack);
            case "get" -> getDepartment(commandStack);
            case "remove" -> removeDepartment(commandStack);
            case "update" -> updateDepartment(commandStack);
            default -> ioManager.print("Unknown command " + command);
        }
    }

    private void removeDepartment(CommandStack commandStack) {
        Long id = Long.parseLong(commandStack.getNextValue());
        clientCommunicator.deleteDepartment(id);
        ioManager.print("Deletion success!");
    }

    private void updateDepartment(CommandStack commandStack) {
        Long id = Long.parseLong(commandStack.getNextValue());
        Department origin = clientCommunicator.getDepartmentById(id);
        DepartmentUpdateDTO departmentUpdateDTO = departmentCreator.update(origin);
        clientCommunicator.updateDepartment(departmentUpdateDTO);
    }

    private List<Department> departmentGetter(CommandStack commandStack) {
        String requestType = commandStack.getNextCommand();
        switch (requestType) {
            case "id" -> {
                Long id = Long.parseLong(commandStack.getNextValue());
                return List.of(clientCommunicator.getDepartmentById(id));
            }
            case "name" -> {
                String name = commandStack.getNextValue();
                return clientCommunicator.getDepartmentsByName(name);
            }
            case "all" -> {
                return clientCommunicator.getAllDepartments();
            }
            default -> {
                ioManager.print("Unknown get request type");
                return List.of();
            }
        }
    }
    private void getDepartment(CommandStack commandStack) {
        if (commandStack.isEmpty()) {
            ioManager.print("Unexpected number of parameters");
            return;
        }

        List<Department> departments = departmentGetter(commandStack);
        ioManager.print(departments.toString());

        if(ioManager.askBoolean("Save to file?")) {
            String filename = ioManager.askString("Enter filename:");
            saveDepartments(filename, departments);
        }
    }

    private void addDepartment(CommandStack commandStack) {
        if (commandStack.hasCommand()) {
            ioManager.print("Unexpected number of parameters");
            return;
        }
        DepartmentCreateDTO departmentCreateDTO = departmentCreator.create();
        Department department = clientCommunicator.createDepartment(departmentCreateDTO);
        ioManager.print("Successfully Created Employee:\n" + department.toString());
    }
}
