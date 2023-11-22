package org.example.rmi.server;

import org.example.rmi.rinterface.DepartmentRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DepartmentRemoteImpl extends UnicastRemoteObject implements DepartmentRemote {
    protected DepartmentRemoteImpl() throws RemoteException {
        super();
    }
}
