package org.example.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DepartmentRemoteImpl extends UnicastRemoteObject implements DepartmentRemote {
    protected DepartmentRemoteImpl() throws RemoteException {
        super();
    }
}
