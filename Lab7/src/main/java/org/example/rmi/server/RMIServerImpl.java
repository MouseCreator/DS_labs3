package org.example.rmi.server;

import org.example.rmi.rinterface.DepartmentRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerImpl implements RMIServer {
    private static final int port = 1234;

    public static void main(String[] args) throws RemoteException {
        DepartmentRemote departmentRemote = new DepartmentRemoteImpl();
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Departments", departmentRemote);
    }
}
