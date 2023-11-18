package org.example.factory;

import org.example.controller.*;
import org.example.dao.*;
import org.example.service.DepartmentsService;
import org.example.service.DepartmentsServiceImpl;
import org.example.service.EmployeesServiceImpl;
import org.example.util.ConnectionPool;
import org.example.util.ConnectionProvider;


public class StaticControllerFactoryImpl implements ControllerFactory, AutoCloseable {
    private ConnectionProvider provider;
    private DepartmentsDAO departmentsDatabaseDAO;
    private EmployeesDao employeesDatabaseDAO;
    private CommonController commonController;
    private StaticControllerFactoryImpl() {

    }
    public static StaticControllerFactoryImpl get() {
        StaticControllerFactoryImpl staticControllerFactory = new StaticControllerFactoryImpl();
        staticControllerFactory.init();
        return staticControllerFactory;
    }

    private void init() {
        provider = ConnectionPool.commonPool(16);
        departmentsDatabaseDAO = new DBDepartmentsDAO(provider);
        employeesDatabaseDAO = new DBEmployeesDao(provider);
        commonController = LocalController.consoleController();
    }

    @Override
    public DepartmentController initialDepartmentController() {
        DepartmentsService service = new DepartmentsServiceImpl(departmentsDatabaseDAO);
        return new DepartmentControllerImpl(commonController, service);
    }

    @Override
    public EmployeesController initialEmployeeController() {
        EmployeesServiceImpl service = new EmployeesServiceImpl(employeesDatabaseDAO);
        return new EmployeesControllerImpl(service, commonController);
    }

    @Override
    public void close() throws Exception {
        provider.close();
    }

    public DepartmentsDAO getDepartmentsDatabaseDAO() {
        return departmentsDatabaseDAO;
    }

    public EmployeesDao getEmployeesDatabaseDAO() {
        return employeesDatabaseDAO;
    }

    public CommonController getCommonController() {
        return commonController;
    }


}
