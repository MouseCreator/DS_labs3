package org.example.rmi.server;

import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.rmi.rinterface.DepartmentRemote;
import org.example.server.CommonServerController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DepartmentRemoteImpl extends UnicastRemoteObject implements DepartmentRemote {
    private CommonServerController serverController;

    public static DepartmentRemote instance() throws RemoteException {
        DepartmentRemoteImpl instance = new DepartmentRemoteImpl();
        ControllerInitializer initializer = new ControllerInitializer();
        instance.init(initializer.simpleDepartmentController());
        return instance;
    }

    public void init(CommonServerController serverController) {
        this.serverController = serverController;
    }
    protected DepartmentRemoteImpl() throws RemoteException {
        super();
    }
    public Response get(Request request) throws RemoteException {
        return serverController.get(request);
    }
}
