package org.example.rmi.server;

import org.example.controller.DepartmentController;
import org.example.controller.EmployeesController;
import org.example.controller.SimpleDepartmentController;
import org.example.controller.SimpleEmployeeController;
import org.example.dao.DBDepartmentsDAO;
import org.example.dao.DBEmployeesDao;
import org.example.dao.DepartmentsDAO;
import org.example.dao.EmployeesDao;
import org.example.server.CommonServerController;
import org.example.service.DepartmentsService;
import org.example.service.DepartmentsServiceImpl;
import org.example.service.EmployeesService;
import org.example.service.EmployeesServiceImpl;
import org.example.util.ConnectionPool;

public class ControllerInitializer {
    public CommonServerController simpleDepartmentController() {
        CommonServerController serverController = new CommonServerController();
        DepartmentController departmentController = getDepartmentController();
        EmployeesController employeesController = getEmployeeController();
        serverController.init(departmentController, employeesController);
        return serverController;
    }

    private EmployeesController getEmployeeController() {
        EmployeesDao dao = new DBEmployeesDao(ConnectionPool.commonPool(10));
        EmployeesService service = new EmployeesServiceImpl(dao);
        return new SimpleEmployeeController(service);
    }

    private DepartmentController getDepartmentController() {
        DepartmentsDAO dao = new DBDepartmentsDAO(ConnectionPool.commonPool(10));
        DepartmentsService service = new DepartmentsServiceImpl(dao);
        return new SimpleDepartmentController(service);
    }
}
