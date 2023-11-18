package org.example.server;

import org.example.controller.DepartmentController;
import org.example.controller.EmployeesController;
import org.example.controller.ModelController;
import org.example.dao.IdIterable;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.model.dto.Status;

import java.util.ArrayList;
import java.util.List;

public class CommonServerController {
    private DepartmentController departmentController;
    private EmployeesController employeesController;
    public void init(DepartmentController departmentController, EmployeesController employeesController) {
        this.departmentController = departmentController;
        this.employeesController = employeesController;
    }
    public Response get(Request request) {
        return processRequest(request);
    }
    private Response processRequest(Request request) {
        Response response = new Response();
        String method = request.getMethod().toUpperCase();
        switch (method) {
            case "POST" -> {
                postMethod(request, response);
            }
            case "GET" -> {
                getMethod(request, response);
            }
            case "DELETE" -> {
                //delete
            }
            case "UPDATE" -> {
                //update
            }
            default -> {
                response.setStatus(Status.CLIENT_ERROR);
                response.setDetailsString("Unknown method: " + method);
            }
        }
        return response;
    }

    private void postMethod(Request request, Response response) {
        StringBuilder builder = new StringBuilder();
        Departments departments = request.getDepartments();
        for (Department department : departments.getDepartmentList()) {
            try {
                departmentController.create(department);
            } catch (Exception e) {
                builder.append("Could not create department ").append(department).append(": ").append(e.getMessage());
            }
        }
        for (Employee employee : departments.getEmployeeList()) {
            try {
                employeesController.create(employee);
            } catch (Exception e) {
                builder.append("Could not create employee ").append(employee).append(": ").append(e.getMessage());
            }
        }
        String details = builder.toString();
        if (details.isEmpty()) {
            details = "Creation success!";
            response.setStatus(Status.SUCCESS);
        } else {
            response.setStatus(Status.WARNING);
        }
        response.setDetailsString(details);
    }
    private <T extends IdIterable> List<T> getModel(ModelController<T> controller, String request) {
        List<T> result = new ArrayList<>();
        if (request.isEmpty()) {
            result = controller.getAll();
        } else {
            request = request.replace(" +", " ");
            String[] parts = request.split(" ", 2);
            String cmd = formatCommand(parts[0]);
            switch (cmd) {
                case "a", "all" -> result = controller.getAll();
                case "f", "filter" -> {
                    result = controller.filter(parts[1]);
                }
                case "id" -> {
                    Long id = Long.parseLong(parts[1]);
                    T model = controller.get(id);
                    result.add(model);
                }
                default -> {
                    throw new IllegalArgumentException("Cannot resolve " + cmd);
                }
            }
        }
        return result;
    }

    private String formatCommand(String part) {
        return part.trim().toLowerCase();
    }

    private void getMethod(Request request, Response response) {
        Departments departments = new Departments();
        response.setDepartments(departments);
        String details = request.getDetails();
        if (request.getTarget().equals("Department")) {
            try {
                List<Department> modelList = getModel(departmentController, details);
                departments.setDepartmentList(modelList);
                response.setStatus(Status.SUCCESS);
            } catch (Exception e) {
                response.setDetailsString(e.getMessage());
                response.setStatus(Status.SERVER_ERROR);
            }
        } else if (request.getTarget().equals("Employee")) {
            try {
                List<Employee> modelList = getModel(employeesController, details);
                departments.setEmployeeList(modelList);
                response.setStatus(Status.SUCCESS);
            } catch (Exception e) {
                response.setDetailsString(e.getMessage());
                response.setStatus(Status.SERVER_ERROR);
            }
        } else {
            response.setDetailsString("Unknown target: " + request.getTarget());
            response.setStatus(Status.CLIENT_ERROR);
        }
    }
}
