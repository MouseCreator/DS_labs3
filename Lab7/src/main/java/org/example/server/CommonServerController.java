package org.example.server;

import org.example.controller.DepartmentController;
import org.example.controller.EmployeesController;
import org.example.model.Department;
import org.example.model.Departments;
import org.example.model.Employee;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.model.dto.Status;

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
                //get
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
}
