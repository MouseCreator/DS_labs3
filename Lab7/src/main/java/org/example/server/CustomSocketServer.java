package org.example.server;

import org.example.communicator.ServerCommunicator;
import org.example.communicator.ServerSocketCommunicator;
import org.example.controller.*;
import org.example.dao.DBDepartmentsDAO;
import org.example.dao.DBEmployeesDao;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.service.DepartmentsService;
import org.example.service.DepartmentsServiceImpl;
import org.example.service.EmployeesService;
import org.example.service.EmployeesServiceImpl;
import org.example.util.ConnectionPool;
import org.example.util.ConnectionProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CustomSocketServer implements Server{
    private ServerSocket serverSocket;
    private ThreadPool threadPool;
    private EmployeesController employeesController;
    private DepartmentController departmentController;
    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(7777);
            threadPool = new ThreadPoolImpl(4);
            initService();
            mainServerLoop();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the server", e);
        }
    }

    private void initService() {
        ConnectionProvider provider = ConnectionPool.commonPool(4);
        EmployeesService employeesService = new EmployeesServiceImpl(new DBEmployeesDao(provider));
        DepartmentsService departmentsService = new DepartmentsServiceImpl(new DBDepartmentsDAO(provider));
        employeesController = new SimpleEmployeeController(employeesService);
        departmentController = new SimpleDepartmentController(departmentsService);
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

    private void mainServerLoop() {
        while (true) {
            try {
                acceptAndProcess();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void acceptAndProcess() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Accepted!");
        threadPool.submit(()->processClient(clientSocket));
    }

    private void processClient(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ServerCommunicator communicator = new ServerSocketCommunicator(outputStream, inputStream);
            CommonServerController commonServerController = new CommonServerController();
            commonServerController.init(departmentController, employeesController);
            processRequests(communicator, commonServerController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processRequests(ServerCommunicator serverCommunicator, CommonServerController commonServerController) {
        while (true) {
            Request request = serverCommunicator.receive();
            if (request.getMethod().equalsIgnoreCase("CLOSE")) {
                return;
            }
            Response response = commonServerController.get(request);
            serverCommunicator.send(response);
        }
    }


}
